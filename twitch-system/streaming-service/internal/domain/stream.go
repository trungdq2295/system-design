// Package domain holds pure business entities and rules (no dependencies).
package domain

import (
	"errors"
	"time"
)

// StreamStatus represents the lifecycle state of a stream.
type StreamStatus int

const (
	StreamStatusInvalid StreamStatus = 0
	StreamStatusOffline StreamStatus = 1
	StreamStatusLive    StreamStatus = 2
	StreamStatusEnded   StreamStatus = 3
)

// String returns a human-readable status name.
func (s StreamStatus) String() string {
	switch s {
	case StreamStatusOffline:
		return "offline"
	case StreamStatusLive:
		return "live"
	case StreamStatusEnded:
		return "ended"
	default:
		return "invalid"
	}
}

var (
	// ErrNotFound is returned when a stream does not exist.
	ErrNotFound = errors.New("stream not found")
	// ErrInvalidStream is returned when required stream fields are missing.
	ErrInvalidStream = errors.New("invalid stream: missing required fields")
	// ErrInvalidTransition is returned for an illegal status change.
	ErrInvalidTransition = errors.New("invalid stream status transition")
)

// Stream is the core entity describing a live stream.
type Stream struct {
	ID        int64
	UserID    string
	StreamKey string
	Title     string
	Status    StreamStatus
	OriginURL string
	StartedAt time.Time
	EndedAt   time.Time
	CreatedAt time.Time
	UpdatedAt time.Time
}

// IsValidTransition reports whether moving to target is allowed from the current status.
//
// Allowed: Offline -> Live (start), Live -> Ended (stop).
func (s *Stream) IsValidTransition(target StreamStatus) bool {
	switch target {
	case StreamStatusLive:
		return s.Status == StreamStatusOffline
	case StreamStatusEnded:
		return s.Status == StreamStatusLive
	default:
		return false
	}
}
