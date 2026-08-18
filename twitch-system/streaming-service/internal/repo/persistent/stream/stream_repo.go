package stream

import (
	"context"
	"errors"
	"time"

	"github.com/trungdq2295/streaming-service/internal/domain"
	"github.com/trungdq2295/streaming-service/pkg/apperror"
	"github.com/trungdq2295/streaming-service/pkg/logger"
	"gorm.io/gorm"
)

// Repo implements repo.StreamRepo using GORM + Postgres.
type Repo struct {
	db     *gorm.DB
	logger logger.Logger
}

// NewStreamRepo creates a Stream repo.
func NewStreamRepo(db *gorm.DB, lg logger.Logger) *Repo {
	return &Repo{db: db, logger: lg}
}

// Create inserts a new stream.
func (r *Repo) Create(ctx context.Context, s *domain.Stream) error {
	now := time.Now().UTC()
	model := Stream{
		UserID:    s.UserID,
		StreamKey: s.StreamKey,
		Title:     s.Title,
		Status:    int(s.Status),
		OriginURL: s.OriginURL,
		StartedAt: s.StartedAt,
		EndedAt:   s.EndedAt,
		CreatedAt: now,
		UpdatedAt: now,
	}
	if err := r.db.WithContext(ctx).Create(&model).Error; err != nil {
		r.logger.ErrorCtx(ctx, "repo Stream.Create failed", err, "userID", s.UserID)
		return apperror.Internal("failed to create stream", err)
	}

	s.ID = model.ID
	s.CreatedAt = model.CreatedAt
	s.UpdatedAt = model.UpdatedAt
	return nil
}

// GetByID fetches a stream by primary key.
func (r *Repo) GetByID(ctx context.Context, id int64) (*domain.Stream, error) {
	var model Stream
	if err := r.db.WithContext(ctx).First(&model, id).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, apperror.NotFound("stream not found", domain.ErrNotFound)
		}
		r.logger.ErrorCtx(ctx, "repo Stream.GetByID failed", err, "id", id)
		return nil, apperror.Internal("failed to get stream", err)
	}
	return toDomain(&model), nil
}

// GetByStreamKey fetches a stream by its ingest key.
func (r *Repo) GetByStreamKey(ctx context.Context, key string) (*domain.Stream, error) {
	var model Stream
	if err := r.db.WithContext(ctx).Where("stream_key = ?", key).First(&model).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, apperror.NotFound("stream not found", domain.ErrNotFound)
		}
		r.logger.ErrorCtx(ctx, "repo Stream.GetByStreamKey failed", err, "key", key)
		return nil, apperror.Internal("failed to get stream", err)
	}
	return toDomain(&model), nil
}

// Update persists changes to an existing stream.
func (r *Repo) Update(ctx context.Context, s *domain.Stream) error {
	model := Stream{
		ID:        s.ID,
		UserID:    s.UserID,
		StreamKey: s.StreamKey,
		Title:     s.Title,
		Status:    int(s.Status),
		OriginURL: s.OriginURL,
		StartedAt: s.StartedAt,
		EndedAt:   s.EndedAt,
		CreatedAt: s.CreatedAt,
		UpdatedAt: time.Now().UTC(),
	}
	if err := r.db.WithContext(ctx).Save(&model).Error; err != nil {
		r.logger.ErrorCtx(ctx, "repo Stream.Update failed", err, "id", s.ID)
		return apperror.Internal("failed to update stream", err)
	}

	s.UpdatedAt = model.UpdatedAt
	return nil
}

func toDomain(m *Stream) *domain.Stream {
	return &domain.Stream{
		ID:        m.ID,
		UserID:    m.UserID,
		StreamKey: m.StreamKey,
		Title:     m.Title,
		Status:    domain.StreamStatus(m.Status),
		OriginURL: m.OriginURL,
		StartedAt: m.StartedAt,
		EndedAt:   m.EndedAt,
		CreatedAt: m.CreatedAt,
		UpdatedAt: m.UpdatedAt,
	}
}
