// Package stream implements the stream business logic.
package stream

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"time"

	"github.com/trungdq2295/streaming-service/internal/domain"
	"github.com/trungdq2295/streaming-service/internal/repo"
	"github.com/trungdq2295/streaming-service/pkg/apperror"
	"github.com/trungdq2295/streaming-service/pkg/logger"
)

// Service implements service.StreamService.
type Service struct {
	streamRepo repo.StreamRepo
	cdnBaseURL string
	logger     logger.Logger
}

// NewService creates a stream service.
func NewService(streamRepo repo.StreamRepo, cdnBaseURL string, lg logger.Logger) *Service {
	return &Service{
		streamRepo: streamRepo,
		cdnBaseURL: cdnBaseURL,
		logger:     lg,
	}
}

// CreateStream validates input, generates a stream key, and persists an offline stream.
func (s *Service) CreateStream(ctx context.Context, userID, title string) (*domain.Stream, error) {
	if userID == "" {
		return nil, apperror.Validation("user_id is required", domain.ErrInvalidStream)
	}
	if title == "" {
		return nil, apperror.Validation("title is required", domain.ErrInvalidStream)
	}

	key, err := generateStreamKey()
	if err != nil {
		return nil, apperror.Internal("failed to generate stream key", err)
	}

	stream := &domain.Stream{
		UserID:    userID,
		StreamKey: key,
		Title:     title,
		Status:    domain.StreamStatusOffline,
	}

	if err := s.streamRepo.Create(ctx, stream); err != nil {
		return nil, err
	}

	s.logger.InfoCtx(ctx, "service CreateStream succeeded", "streamID", stream.ID, "userID", userID)
	return stream, nil
}

// StartStream transitions an offline stream to live and sets the playback URL.
func (s *Service) StartStream(ctx context.Context, id int64) (*domain.Stream, error) {
	stream, err := s.streamRepo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}

	if !stream.IsValidTransition(domain.StreamStatusLive) {
		return nil, apperror.Conflict(
			fmt.Sprintf("cannot start stream in status %s", stream.Status),
			domain.ErrInvalidTransition,
		)
	}

	stream.Status = domain.StreamStatusLive
	stream.StartedAt = time.Now().UTC()
	stream.OriginURL = fmt.Sprintf("%s/hls/%s.m3u8", s.cdnBaseURL, stream.StreamKey)

	if err := s.streamRepo.Update(ctx, stream); err != nil {
		return nil, err
	}

	s.logger.InfoCtx(ctx, "service StartStream succeeded", "streamID", id)
	return stream, nil
}

// StopStream transitions a live stream to ended.
func (s *Service) StopStream(ctx context.Context, id int64) (*domain.Stream, error) {
	stream, err := s.streamRepo.GetByID(ctx, id)
	if err != nil {
		return nil, err
	}

	if !stream.IsValidTransition(domain.StreamStatusEnded) {
		return nil, apperror.Conflict(
			fmt.Sprintf("cannot stop stream in status %s", stream.Status),
			domain.ErrInvalidTransition,
		)
	}

	stream.Status = domain.StreamStatusEnded
	stream.EndedAt = time.Now().UTC()

	if err := s.streamRepo.Update(ctx, stream); err != nil {
		return nil, err
	}

	s.logger.InfoCtx(ctx, "service StopStream succeeded", "streamID", id)
	return stream, nil
}

// GetStream returns stream metadata.
func (s *Service) GetStream(ctx context.Context, id int64) (*domain.Stream, error) {
	return s.streamRepo.GetByID(ctx, id)
}

// generateStreamKey returns a 32-char hex key for RTMP ingest.
func generateStreamKey() (string, error) {
	b := make([]byte, 16)
	if _, err := rand.Read(b); err != nil {
		return "", err
	}
	return hex.EncodeToString(b), nil
}
