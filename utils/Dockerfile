version: '3'
services:
  healthcheck-collector:
    build: ./healthcheck-collector
    ports:
      - "8193:8193"

  healthcheck-gate:
    build: ./healthcheck-gate
    ports:
      - "8199:8199"
    depends_on:
      - healthcheck-collector

  daily-api:
    build: ./daily-API
    ports:
      - "8194:8194"

  predict-next-value:
    build: ./predict_next_value
    ports:
      - "5000:5000"

  healthcheck-dashboard:
    build: ./healthcheck-dashboard
    ports:
      - "3001:3000"
    depends_on:
      - healthcheck-gate

  redis:
    image: redis
    ports:
      - "6379:6379"

  postgres:
    image: postgres
    environment:
      POSTGRES_DB: healthcheck
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"

  rabbitmq:
    image: rabbitmq:management
    ports:
      - "15672:15672"
      - "5672:5672"
