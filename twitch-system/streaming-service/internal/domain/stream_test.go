package domain

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestIsValidTransition(t *testing.T) {
	tests := []struct {
		name   string
		from   StreamStatus
		to     StreamStatus
		expect bool
	}{
		{"offline to live", StreamStatusOffline, StreamStatusLive, true},
		{"live to ended", StreamStatusLive, StreamStatusEnded, true},
		{"offline to ended", StreamStatusOffline, StreamStatusEnded, false},
		{"live to live", StreamStatusLive, StreamStatusLive, false},
		{"ended to live", StreamStatusEnded, StreamStatusLive, false},
		{"offline to invalid", StreamStatusOffline, StreamStatusInvalid, false},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			s := &Stream{Status: tt.from}
			assert.Equal(t, tt.expect, s.IsValidTransition(tt.to))
		})
	}
}

func TestStreamStatusString(t *testing.T) {
	assert.Equal(t, "offline", StreamStatusOffline.String())
	assert.Equal(t, "live", StreamStatusLive.String())
	assert.Equal(t, "ended", StreamStatusEnded.String())
	assert.Equal(t, "invalid", StreamStatusInvalid.String())
}
