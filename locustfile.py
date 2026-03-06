import json
import time
import uuid
from locust import HttpUser, task, between
from locust.exception import StopUser

try:
    import websocket
except ImportError as exc:
    raise RuntimeError(
        "websocket-client package is required for STOMP load testing. "
        "Install it with: pip install websocket-client"
    ) from exc


class RestAndStompUser(HttpUser):
    wait_time = between(0.5, 2.0)

    def on_start(self):
        self.ws_url = self.host.replace("http://", "ws://").replace("https://", "wss://") + "/ws"
        self.ws = None

        try:
            self.ws = websocket.create_connection(self.ws_url, timeout=5)
            self._stomp_connect()
            self._stomp_subscribe("/topic/messages")
        except Exception as exc:
            self.environment.events.request.fire(
                request_type="WS",
                name="connect",
                response_time=0,
                response_length=0,
                exception=exc,
            )
            raise StopUser()

    def on_stop(self):
        if self.ws:
            try:
                self.ws.send("DISCONNECT\nreceipt:77\n\n\x00")
                self.ws.close()
            except Exception:
                pass

    @task(3)
    def rest_health(self):
        self.client.get("/api/health", name="GET /api/health")

    @task(3)
    def rest_message(self):
        payload = {
            "from": "locust-rest",
            "content": f"hello-{uuid.uuid4()}"
        }
        self.client.post("/api/messages", json=payload, name="POST /api/messages")

    @task(4)
    def stomp_message(self):
        if not self.ws:
            return

        body = json.dumps({"from": "locust-ws", "content": f"msg-{uuid.uuid4()}"})
        frame = (
            "SEND\n"
            "destination:/app/chat\n"
            "content-type:application/json\n"
            f"content-length:{len(body)}\n\n"
            f"{body}\x00"
        )

        start = time.perf_counter()
        exc = None
        response_length = 0
        try:
            self.ws.send(frame)
            response = self.ws.recv()
            response_length = len(response)
        except Exception as error:
            exc = error

        elapsed_ms = (time.perf_counter() - start) * 1000
        self.environment.events.request.fire(
            request_type="WS",
            name="SEND /app/chat",
            response_time=elapsed_ms,
            response_length=response_length,
            exception=exc,
        )

    def _stomp_connect(self):
        connect_frame = "CONNECT\naccept-version:1.2\nhost:localhost\n\n\x00"
        self.ws.send(connect_frame)
        response = self.ws.recv()
        if "CONNECTED" not in response:
            raise RuntimeError(f"STOMP CONNECT failed: {response}")

    def _stomp_subscribe(self, destination):
        subscribe_frame = (
            "SUBSCRIBE\n"
            "id:sub-0\n"
            f"destination:{destination}\n"
            "ack:auto\n\n\x00"
        )
        self.ws.send(subscribe_frame)
