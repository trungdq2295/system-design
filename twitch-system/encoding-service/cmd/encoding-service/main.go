// Package main is the entry point for the encoding service (RTMP ingest + HLS origin).
package main

import (
	"mime"
	"net/http"
	"os"
	"os/signal"
	"syscall"

	"github.com/sirupsen/logrus"

	"github.com/trungdq2295/encoding-service/config"
	"github.com/trungdq2295/encoding-service/internal/origin"
	rtmpsrv "github.com/trungdq2295/encoding-service/internal/rtmp"
)

func main() {
	logger := logrus.New()

	cfg, err := config.New()
	if err != nil {
		logger.Fatalf("config error: %v", err)
	}
	setLogLevel(logger, cfg.LogLevel)

	// Register HLS MIME types so http.FileServer serves them correctly.
	mime.AddExtensionType(".m3u8", "application/vnd.apple.mpegurl")
	mime.AddExtensionType(".ts", "video/mp2t")

	// HLS origin HTTP server.
	originServer := origin.New(logger, cfg.HLSDir)
	go func() {
		logger.WithField("port", cfg.HTTPPort).Info("origin HTTP server listening")
		if err := http.ListenAndServe(":"+cfg.HTTPPort, originServer.Handler()); err != nil {
			logger.Fatalf("origin server error: %v", err)
		}
	}()

	// RTMP ingest server.
	ingest := rtmpsrv.New(logger, cfg.HLSDir)
	go func() {
		if err := ingest.Run(":" + cfg.RTMPPort); err != nil {
			logger.Fatalf("rtmp server error: %v", err)
		}
	}()

	// Block until shutdown.
	interrupt := make(chan os.Signal, 1)
	signal.Notify(interrupt, os.Interrupt, syscall.SIGTERM)
	<-interrupt

	logger.Info("shutting down")
	_ = ingest.Close()
}

func setLogLevel(logger *logrus.Logger, level string) {
	lvl, err := logrus.ParseLevel(level)
	if err != nil {
		lvl = logrus.InfoLevel
	}
	logger.SetLevel(lvl)
}
