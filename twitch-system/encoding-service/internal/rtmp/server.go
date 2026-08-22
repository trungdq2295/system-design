// Package rtmp implements the RTMP ingest server.
package rtmp

import (
	"fmt"
	"io"
	"net"

	"github.com/sirupsen/logrus"
	rtmp "github.com/yutopp/go-rtmp"
)

// Server is the RTMP ingest server.
type Server struct {
	logger *logrus.Logger
	hlsDir string
	srv    *rtmp.Server
}

// New creates an RTMP ingest server.
func New(logger *logrus.Logger, hlsDir string) *Server {
	return &Server{logger: logger, hlsDir: hlsDir}
}

// Run listens on addr and serves RTMP until the listener closes.
func (s *Server) Run(addr string) error {
	tcpAddr, err := net.ResolveTCPAddr("tcp", addr)
	if err != nil {
		return fmt.Errorf("resolve tcp addr: %w", err)
	}

	listener, err := net.ListenTCP("tcp", tcpAddr)
	if err != nil {
		return fmt.Errorf("listen tcp: %w", err)
	}

	s.srv = rtmp.NewServer(&rtmp.ServerConfig{
		OnConnect: func(conn net.Conn) (io.ReadWriteCloser, *rtmp.ConnConfig) {
			return conn, &rtmp.ConnConfig{
				Handler: newHandler(s.logger, s.hlsDir),
				ControlState: rtmp.StreamControlStateConfig{
					DefaultBandwidthWindowSize: 6 * 1024 * 1024 / 8,
				},
				Logger: s.logger,
			}
		},
	})

	s.logger.WithField("addr", addr).Info("RTMP server listening")
	return s.srv.Serve(listener)
}

// Close stops the RTMP server.
func (s *Server) Close() error {
	if s.srv == nil {
		return nil
	}
	return s.srv.Close()
}
