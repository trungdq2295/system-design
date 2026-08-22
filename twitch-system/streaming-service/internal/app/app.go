// Package app composes the application and manages its lifecycle.
package app

import (
	"fmt"
	"os"
	"os/signal"
	"syscall"

	"github.com/trungdq2295/streaming-service/config"
	"github.com/trungdq2295/streaming-service/internal/deps"
	"github.com/trungdq2295/streaming-service/pkg/logger"
)

// App is the application container.
type App struct {
	cfg    *config.Config
	logger logger.Logger
	deps   *deps.Dependencies
}

// New initializes the application and its dependencies.
func New(cfg *config.Config, lg logger.Logger) (*App, error) {
	lg.Info("Initializing application...")

	dependencies, err := deps.Initialize(cfg, lg)
	if err != nil {
		return nil, fmt.Errorf("app - New - deps.Initialize: %w", err)
	}

	return &App{cfg: cfg, logger: lg, deps: dependencies}, nil
}

// Run starts the server and blocks until shutdown.
func (a *App) Run() error {
	a.logger.Info("Starting Streaming Service...")

	server := a.deps.HTTPServer
	server.Start()

	a.logger.Info("Streaming Service started", "http_port", a.cfg.HTTP.Port)

	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt, syscall.SIGTERM)

	select {
	case s := <-interrupt:
		a.logger.Info("app - Run - signal received", "signal", s.String())
	case err := <-server.Notify():
		a.logger.Error("app - Run - server.Notify", err)
	}

	a.logger.Info("Shutting down Streaming Service...")

	if err := server.Shutdown(); err != nil {
		a.logger.Error("app - Run - server.Shutdown", err)
	}

	if err := a.deps.CleanUp(); err != nil {
		a.logger.Error("app - Run - deps.CleanUp", err)
	}

	a.logger.Info("Streaming Service shutdown complete")
	return nil
}
