// Package origin serves HLS files over HTTP (the origin that the CDN caches).
package origin

import (
	"net/http"

	"github.com/sirupsen/logrus"
)

// Server is the HLS origin HTTP server.
type Server struct {
	logger *logrus.Logger
	mux    *http.ServeMux
}

// New creates the origin HTTP server serving hlsDir under /hls/.
func New(logger *logrus.Logger, hlsDir string) *Server {
	mux := http.NewServeMux()

	fileServer := http.StripPrefix("/hls/", http.FileServer(http.Dir(hlsDir)))
	mux.Handle("/hls/", withCORS(fileServer))

	return &Server{logger: logger, mux: mux}
}

// Handler returns the root HTTP handler.
func (s *Server) Handler() http.Handler {
	return s.mux
}

func withCORS(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		next.ServeHTTP(w, r)
	})
}
