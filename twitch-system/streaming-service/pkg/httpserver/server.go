// Package httpserver wraps net/http with start/notify/shutdown lifecycle.
package httpserver

import (
	"context"
	"errors"
	"net/http"
	"time"

	"github.com/trungdq2295/streaming-service/pkg/logger"
)

// Server wraps an http.Server with a lifecycle similar to the gRPC server.
type Server struct {
	httpServer *http.Server
	logger     logger.Logger
	notify     chan error
}

// Option configures a Server.
type Option func(*Server)

// Port sets the listen port.
func Port(p string) Option {
	return func(s *Server) {
		s.httpServer.Addr = ":" + p
	}
}

// Handler sets the root http.Handler.
func Handler(h http.Handler) Option {
	return func(s *Server) {
		s.httpServer.Handler = h
	}
}

// New creates a Server.
func New(lg logger.Logger, opts ...Option) *Server {
	s := &Server{
		httpServer: &http.Server{ReadHeaderTimeout: 10 * time.Second},
		logger:     lg,
		notify:     make(chan error, 1),
	}
	for _, o := range opts {
		o(s)
	}
	return s
}

// Start begins serving in a goroutine.
func (s *Server) Start() {
	s.logger.Info("http server started", "addr", s.httpServer.Addr)
	go func() {
		if err := s.httpServer.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			s.notify <- err
		}
	}()
}

// Notify returns a channel that receives a fatal server error.
func (s *Server) Notify() <-chan error {
	return s.notify
}

// Shutdown gracefully stops the server.
func (s *Server) Shutdown() error {
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	return s.httpServer.Shutdown(ctx)
}
