// Package rest implements the HTTP handlers for the streaming service.
package rest

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/trungdq2295/streaming-service/internal/domain"
	"github.com/trungdq2295/streaming-service/internal/usecase"
	"github.com/trungdq2295/streaming-service/pkg/apperror"
	"github.com/trungdq2295/streaming-service/pkg/logger"
)

// StreamHandler handles stream HTTP requests.
type StreamHandler struct {
	streamUsecase usecase.Stream
	logger        logger.Logger
}

// NewStreamHandler creates a stream handler.
func NewStreamHandler(uc usecase.Stream, lg logger.Logger) *StreamHandler {
	return &StreamHandler{streamUsecase: uc, logger: lg}
}

type createStreamRequest struct {
	UserID string `json:"user_id"`
	Title  string `json:"title"`
}

type streamResponse struct {
	ID        int64  `json:"id"`
	UserID    string `json:"user_id"`
	StreamKey string `json:"stream_key"`
	Title     string `json:"title"`
	Status    string `json:"status"`
	OriginURL string `json:"origin_url,omitempty"`
}

// CreateStream handles POST /api/v1/streams.
func (h *StreamHandler) CreateStream(w http.ResponseWriter, r *http.Request) {
	var req createStreamRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		writeError(w, apperror.Validation("invalid request body", err))
		return
	}

	stream, err := h.streamUsecase.CreateStream(r.Context(), req.UserID, req.Title)
	if err != nil {
		writeError(w, err)
		return
	}

	writeJSON(w, http.StatusCreated, toResponse(stream))
}

// StartStream handles POST /api/v1/streams/{id}/start.
func (h *StreamHandler) StartStream(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err != nil {
		writeError(w, err)
		return
	}

	stream, err := h.streamUsecase.StartStream(r.Context(), id)
	if err != nil {
		writeError(w, err)
		return
	}

	writeJSON(w, http.StatusOK, toResponse(stream))
}

// StopStream handles POST /api/v1/streams/{id}/stop.
func (h *StreamHandler) StopStream(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err != nil {
		writeError(w, err)
		return
	}

	stream, err := h.streamUsecase.StopStream(r.Context(), id)
	if err != nil {
		writeError(w, err)
		return
	}

	writeJSON(w, http.StatusOK, toResponse(stream))
}

// GetStream handles GET /api/v1/streams/{id}.
func (h *StreamHandler) GetStream(w http.ResponseWriter, r *http.Request) {
	id, err := pathID(r)
	if err != nil {
		writeError(w, err)
		return
	}

	stream, err := h.streamUsecase.GetStream(r.Context(), id)
	if err != nil {
		writeError(w, err)
		return
	}

	writeJSON(w, http.StatusOK, toResponse(stream))
}

func pathID(r *http.Request) (int64, error) {
	id, err := strconv.ParseInt(r.PathValue("id"), 10, 64)
	if err != nil {
		return 0, apperror.Validation("invalid stream id", err)
	}
	return id, nil
}

func toResponse(s *domain.Stream) streamResponse {
	return streamResponse{
		ID:        s.ID,
		UserID:    s.UserID,
		StreamKey: s.StreamKey,
		Title:     s.Title,
		Status:    s.Status.String(),
		OriginURL: s.OriginURL,
	}
}

func writeJSON(w http.ResponseWriter, status int, body any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(body)
}

func writeError(w http.ResponseWriter, err error) {
	writeJSON(w, statusFromKind(apperror.KindOf(err)), map[string]string{"error": err.Error()})
}

func statusFromKind(k apperror.Kind) int {
	switch k {
	case apperror.KindValidation:
		return http.StatusBadRequest
	case apperror.KindNotFound:
		return http.StatusNotFound
	case apperror.KindConflict:
		return http.StatusConflict
	default:
		return http.StatusInternalServerError
	}
}
