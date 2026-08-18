package rest

import (
	"net/http"

	"github.com/trungdq2295/streaming-service/pkg/logger"
)

// HealthHandler handles health-check requests.
type HealthHandler struct {
	logger logger.Logger
}

// NewHealthHandler creates a health handler.
func NewHealthHandler(lg logger.Logger) *HealthHandler {
	return &HealthHandler{logger: lg}
}

// Check handles GET /healthz.
func (h *HealthHandler) Check(w http.ResponseWriter, r *http.Request) {
	h.logger.Debug("Health check")
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	_, _ = w.Write([]byte(`{"status":"ok"}`))
}
