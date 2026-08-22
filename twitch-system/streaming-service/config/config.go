// Package config provides application configuration from environment variables.
package config

import (
	"fmt"

	"github.com/caarlos0/env/v11"
)

type (
	// Config holds all configuration for the application.
	Config struct {
		App      App
		Log      Log
		HTTP     HTTP
		Database Database
		CDN      CDN
	}

	// App contains application identification configuration.
	App struct {
		Name        string `env:"APP_NAME,required"`
		Version     string `env:"APP_VERSION,required"`
		Environment string `env:"ENVIRONMENT" envDefault:"local"`
	}

	// Log contains logging configuration.
	Log struct {
		Level string `env:"LOG_LEVEL" envDefault:"info"`
	}

	// HTTP contains the REST server configuration.
	HTTP struct {
		Port string `env:"HTTP_PORT" envDefault:"8082"`
	}

	// Database contains Postgres connection configuration.
	Database struct {
		Host     string `env:"POSTGRES_HOST" envDefault:"localhost"`
		Port     string `env:"POSTGRES_PORT" envDefault:"5432"`
		User     string `env:"POSTGRES_USER" envDefault:"postgres"`
		Password string `env:"POSTGRES_PASSWORD" envDefault:"postgres"`
		Name     string `env:"POSTGRES_DB" envDefault:"streaming"`
		SSLMode  string `env:"POSTGRES_SSLMODE" envDefault:"disable"`
	}

	// CDN contains the base URL used to build playback URLs.
	CDN struct {
		BaseURL string `env:"CDN_BASE_URL" envDefault:"http://localhost:8081"`
	}
)

// NewConfig loads and returns application configuration from environment variables.
func NewConfig() (*Config, error) {
	cfg := &Config{}
	if err := env.Parse(cfg); err != nil {
		return nil, fmt.Errorf("config error: %w", err)
	}

	return cfg, nil
}
