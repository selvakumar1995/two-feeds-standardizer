# 2 Feeds - Backend Engineer Home Assignment (Java 8)

Spring Boot **2.7.x** service (Java 8 compatible) that ingests two providers (Alpha & Beta),
normalizes their messages into one internal schema, and publishes to a mocked in-memory queue.

## Prerequisites
- **Java 8 (1.8)**
- **Maven 3.6+**

## Build & Run
```bash
mvn clean package
mvn spring-boot:run
# or
java -jar target/two-feeds-standardizer-0.0.1-SNAPSHOT.jar
```

## Endpoints
- `POST /provider-alpha/feed`
- `POST /provider-beta/feed`
- `GET  /debug/queue`

## Standardized Schemas
**ODDS_CHANGE**
```json
{ "messageType": "ODDS_CHANGE", "eventId": "ev123", "home": 2.0, "draw": 3.1, "away": 3.8 }
```
**BET_SETTLEMENT**
```json
{ "messageType": "BET_SETTLEMENT", "eventId": "ev123", "outcome": "home" }
```

## Example cURL
```bash
# ProviderAlpha - ODDS
curl -sS http://localhost:8080/provider-alpha/feed -H "Content-Type: application/json" -d '{
  "msg_type": "odds_update",
  "event_id": "ev123",
  "values": {"1": 2.0, "X": 3.1, "2": 3.8}
}' | jq .

# ProviderAlpha - SETTLEMENT
curl -sS http://localhost:8080/provider-alpha/feed -H "Content-Type: application/json" -d '{
  "msg_type": "settlement",
  "event_id": "ev123",
  "outcome": "1"
}' | jq .

# ProviderBeta - ODDS
curl -sS http://localhost:8080/provider-beta/feed -H "Content-Type: application/json" -d '{
  "type": "ODDS",
  "event_id": "ev456",
  "odds": {"home": 1.95, "draw": 3.2, "away": 4.0}
}' | jq .

# ProviderBeta - SETTLEMENT
curl -sS http://localhost:8080/provider-beta/feed -H "Content-Type: application/json" -d '{
  "type": "SETTLEMENT",
  "event_id": "ev456",
  "result": "away"
}' | jq .

# Inspect mocked queue
curl -sS http://localhost:8080/debug/queue | jq .
```
