# --- Build stage ---
FROM golang:1.24-alpine AS builder
WORKDIR /app

# Copy source and resolve dependencies (go mod tidy generates go.sum).
COPY . .
RUN go mod tidy

RUN CGO_ENABLED=0 GOOS=linux go build -o /bin/streaming-service ./cmd/app

# --- Runtime stage ---
FROM alpine:3.20
RUN apk --no-cache add ca-certificates tzdata

COPY --from=builder /bin/streaming-service /streaming-service

EXPOSE 8082
CMD ["/streaming-service"]
