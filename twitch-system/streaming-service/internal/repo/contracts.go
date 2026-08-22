// Package repo defines repository interfaces.
package repo

import (
	"context"

	"github.com/trungdq2295/streaming-service/internal/domain"
)

// StreamRepo is the persistence contract for streams.
type StreamRepo interface {
	Create(ctx context.Context, stream *domain.Stream) error
	GetByID(ctx context.Context, id int64) (*domain.Stream, error)
	GetByStreamKey(ctx context.Context, key string) (*domain.Stream, error)
	Update(ctx context.Context, stream *domain.Stream) error
}
