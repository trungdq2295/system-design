// Package apperror provides typed application errors mapped to HTTP statuses.
package apperror

import (
	"errors"
	"fmt"
)

// Kind classifies an error so the handler can map it to an HTTP status.
type Kind string

const (
	KindValidation Kind = "validation"
	KindNotFound   Kind = "not_found"
	KindConflict   Kind = "conflict"
	KindInternal   Kind = "internal"
)

// Error is a typed application error.
type Error struct {
	Kind Kind
	Msg  string
	Err  error
}

func (e *Error) Error() string {
	if e.Err != nil {
		return fmt.Sprintf("%s: %v", e.Msg, e.Err)
	}
	return e.Msg
}

func (e *Error) Unwrap() error {
	return e.Err
}

// Validation returns a 400-class error.
func Validation(msg string, err error) error {
	return &Error{Kind: KindValidation, Msg: msg, Err: err}
}

// NotFound returns a 404-class error.
func NotFound(msg string, err error) error {
	return &Error{Kind: KindNotFound, Msg: msg, Err: err}
}

// Conflict returns a 409-class error.
func Conflict(msg string, err error) error {
	return &Error{Kind: KindConflict, Msg: msg, Err: err}
}

// Internal returns a 500-class error.
func Internal(msg string, err error) error {
	return &Error{Kind: KindInternal, Msg: msg, Err: err}
}

// KindOf extracts the Kind from err, defaulting to KindInternal.
func KindOf(err error) Kind {
	var e *Error
	if errors.As(err, &e) {
		return e.Kind
	}
	return KindInternal
}
