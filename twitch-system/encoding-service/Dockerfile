# --- Build stage ---
FROM golang:1.24-alpine AS builder
WORKDIR /app

COPY . .
RUN go mod tidy

RUN CGO_ENABLED=0 GOOS=linux go build -o /bin/encoding-service ./cmd/encoding-service

# --- Runtime stage ---
FROM alpine:3.20
# ffmpeg is the actual transcoder; the Go binary orchestrates it.
RUN apk --no-cache add ca-certificates tzdata ffmpeg

COPY --from=builder /bin/encoding-service /encoding-service

EXPOSE 1935 8080
CMD ["/encoding-service"]
