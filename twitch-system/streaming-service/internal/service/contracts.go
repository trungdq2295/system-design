// Package service defines service (business logic) interfaces.
package service

import (
	"context"

	"github.com/trungdq2295/streaming-service/internal/domain"
)

// StreamService is the business-logic contract for streams.
type StreamService interface {
	CreateStream(ctx context.Context, userID, title string) (*domain.Stream, error)
	StartStream(ctx context.Context, id int64) (*domain.Stream, error)
	StopStream(ctx context.Context, id int64) (*domain.Stream, error)
	GetStream(ctx context.Context, id int64) (*domain.Stream, error)
}
