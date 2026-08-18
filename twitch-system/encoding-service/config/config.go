// Package config loads encoding-service configuration from environment variables.
package config

import (
	"fmt"
	"os"
)

// Config holds encoding-service configuration.
type Config struct {
	RTMPPort string // RTMP ingest port (OBS pushes here)
	HTTPPort string // HTTP port serving HLS files (origin)
	HLSDir   string // directory where FFmpeg writes HLS output
	LogLevel string // logrus log level
}

// New loads config from environment variables and prepares the HLS directory.
func New() (*Config, error) {
	cfg := &Config{
		RTMPPort: getEnv("RTMP_PORT", "1935"),
		HTTPPort: getEnv("HTTP_PORT", "8080"),
		HLSDir:   getEnv("HLS_DIR", "/data/hls"),
		LogLevel: getEnv("LOG_LEVEL", "info"),
	}

	if err := os.MkdirAll(cfg.HLSDir, 0o755); err != nil {
		return nil, fmt.Errorf("failed to create HLS dir %s: %w", cfg.HLSDir, err)
	}

	return cfg, nil
}

func getEnv(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}
