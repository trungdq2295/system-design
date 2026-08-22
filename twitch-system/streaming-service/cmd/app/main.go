// Package main is the entry point for the streaming service.
package main

import (
	"log"
	"os"

	"github.com/joho/godotenv"

	"github.com/trungdq2295/streaming-service/config"
	"github.com/trungdq2295/streaming-service/internal/app"
	"github.com/trungdq2295/streaming-service/pkg/logger"
)

func main() {
	// Load .env if present (ignored in production where env vars are set directly).
	if err := godotenv.Load(); err != nil {
		log.Println("No .env file found, using environment variables")
	}

	cfg, err := config.NewConfig()
	if err != nil {
		log.Fatalf("Config error: %s", err)
	}

	appLogger := logger.New(logger.FormatJSON, cfg.Log.Level, os.Stdout)

	application, err := app.New(cfg, appLogger)
	if err != nil {
		log.Fatalf("Application initialization error: %s", err)
	}

	if err := application.Run(); err != nil {
		log.Fatalf("Application error: %s", err)
	}
}
