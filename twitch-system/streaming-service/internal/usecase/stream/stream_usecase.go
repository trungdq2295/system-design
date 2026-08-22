// Package stream implements the stream use-cases (thin orchestration layer).
//
// The use-case layer orchestrates one or more services and enforces application
// flow. It is thin today because each operation maps 1:1 to a service method;
// it grows when an operation must coordinate multiple services (e.g. StartStream
// also notifying the encoding service, or emitting a chat event).
package stream

import (
	"context"

	"github.com/trungdq2295/streaming-service/internal/domain"
	"github.com/trungdq2295/streaming-service/internal/service"
	"github.com/trungdq2295/streaming-service/pkg/logger"
)

// Usecase implements usecase.Stream.
type Usecase struct {
	streamService service.StreamService
	logger        logger.Logger
}

// NewUsecase creates a stream use-case.
func NewUsecase(streamService service.StreamService, lg logger.Logger) *Usecase {
	return &Usecase{streamService: streamService, logger: lg}
}

func (uc *Usecase) CreateStream(ctx context.Context, userID, title string) (*domain.Stream, error) {
	return uc.streamService.CreateStream(ctx, userID, title)
}

func (uc *Usecase) StartStream(ctx context.Context, id int64) (*domain.Stream, error) {
	return uc.streamService.StartStream(ctx, id)
}

func (uc *Usecase) StopStream(ctx context.Context, id int64) (*domain.Stream, error) {
	return uc.streamService.StopStream(ctx, id)
}

func (uc *Usecase) GetStream(ctx context.Context, id int64) (*domain.Stream, error) {
	return uc.streamService.GetStream(ctx, id)
}
