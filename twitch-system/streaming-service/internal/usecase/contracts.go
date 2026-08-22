// Package usecase defines use-case (orchestration) interfaces.
package usecase

import (
	"context"

	"github.com/trungdq2295/streaming-service/internal/domain"
)

// Stream is the application use-case contract for streams.
type Stream interface {
	CreateStream(ctx context.Context, userID, title string) (*domain.Stream, error)
	StartStream(ctx context.Context, id int64) (*domain.Stream, error)
	StopStream(ctx context.Context, id int64) (*domain.Stream, error)
	GetStream(ctx context.Context, id int64) (*domain.Stream, error)
}
