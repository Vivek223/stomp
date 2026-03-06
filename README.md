# Spring Boot REST + STOMP WebSocket Demo

This project provides:

- REST endpoints for health and message echo.
- STOMP-over-WebSocket endpoint for chat-style publish/subscribe messaging.
- A Locust script that load tests both REST and STOMP endpoints.

## Prerequisites

- Java 17+
- Maven 3.9+
- Python 3.10+
- Locust (`pip install locust websocket-client`)

## Run the application

```bash
mvn spring-boot:run
```

Application runs on `http://localhost:8080`.

## Endpoints

### REST

- `GET /api/health` → returns status and server timestamp.
- `POST /api/messages` with body:

```json
{
  "from": "alice",
  "content": "hello"
}
```

### WebSocket (STOMP)

- WebSocket endpoint: `/ws`
- Application destination prefix: `/app`
- Broker destinations: `/topic`, `/queue`
- Send messages to: `/app/chat`
- Subscribe to: `/topic/messages`

## Load testing with Locust

Start the Spring Boot app first, then run:

```bash
locust -f locustfile.py --host=http://localhost:8080
```

Open `http://localhost:8089` and start a test. The script creates mixed traffic:

- REST `GET /api/health`
- REST `POST /api/messages`
- STOMP `SEND` to `/app/chat` and receive from `/topic/messages`

