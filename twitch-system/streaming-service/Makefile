ifneq ($(wildcard .env),)
include .env
export
endif

UNIT_TEST_PATHS ?= ./internal/... ./pkg/...

.PHONY: help build run test-unit lint-go clean

help:
	@echo "Streaming Service - Commands:"
	@echo "  build      build the binary"
	@echo "  run        run the service"
	@echo "  test-unit  run unit tests"
	@echo "  lint-go    run linters"
	@echo "  clean      remove build artifacts"

build:
	go build -o bin/streaming-service ./cmd/app

run:
	go run ./cmd/app

test-unit:
	go test -v -race -timeout 30s $(UNIT_TEST_PATHS)

lint-go:
	golangci-lint run

clean:
	rm -rf bin/ coverage.txt
	go clean
