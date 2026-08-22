// Package logger provides a structured logger backed by stdlib log/slog.
package logger

import (
	"context"
	"io"
	"log/slog"
)

// Logger is the application logging interface.
type Logger interface {
	Info(msg string, keyvals ...any)
	InfoCtx(ctx context.Context, msg string, keyvals ...any)
	Error(msg string, err error)
	ErrorCtx(ctx context.Context, msg string, err error, keyvals ...any)
	Warn(msg string, keyvals ...any)
	Debug(msg string, keyvals ...any)
}

// Format selects the log output format.
type Format int

const (
	FormatJSON Format = iota
	FormatText
)

type slogLogger struct {
	l *slog.Logger
}

// New creates a Logger writing to w in the given format and level.
func New(format Format, level string, w io.Writer) Logger {
	opts := &slog.HandlerOptions{Level: parseLevel(level)}
	var h slog.Handler
	if format == FormatJSON {
		h = slog.NewJSONHandler(w, opts)
	} else {
		h = slog.NewTextHandler(w, opts)
	}
	return &slogLogger{l: slog.New(h)}
}

func parseLevel(s string) slog.Level {
	switch s {
	case "debug":
		return slog.LevelDebug
	case "warn":
		return slog.LevelWarn
	case "error":
		return slog.LevelError
	default:
		return slog.LevelInfo
	}
}

func (l *slogLogger) Info(msg string, keyvals ...any) {
	l.l.Info(msg, keyvals...)
}

func (l *slogLogger) InfoCtx(ctx context.Context, msg string, keyvals ...any) {
	l.l.InfoContext(ctx, msg, keyvals...)
}

func (l *slogLogger) Error(msg string, err error) {
	l.l.Error(msg, "error", err)
}

func (l *slogLogger) ErrorCtx(ctx context.Context, msg string, err error, keyvals ...any) {
	args := append([]any{"error", err}, keyvals...)
	l.l.ErrorContext(ctx, msg, args...)
}

func (l *slogLogger) Warn(msg string, keyvals ...any) {
	l.l.Warn(msg, keyvals...)
}

func (l *slogLogger) Debug(msg string, keyvals ...any) {
	l.l.Debug(msg, keyvals...)
}

var _ Logger = (*slogLogger)(nil)
