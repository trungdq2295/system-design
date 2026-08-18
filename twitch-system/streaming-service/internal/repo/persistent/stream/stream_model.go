// Package stream implements the GORM persistence for streams.
package stream

import "time"

// Stream is the GORM model mapped to the "streams" table.
type Stream struct {
	ID        int64     `gorm:"column:id;primaryKey;autoIncrement"`
	UserID    string    `gorm:"column:user_id;size:64;not null;index"`
	StreamKey string    `gorm:"column:stream_key;size:64;not null;uniqueIndex"`
	Title     string    `gorm:"column:title;size:255;not null"`
	Status    int       `gorm:"column:status;not null;default:1"`
	OriginURL string    `gorm:"column:origin_url;size:512"`
	StartedAt time.Time `gorm:"column:started_at"`
	EndedAt   time.Time `gorm:"column:ended_at"`
	CreatedAt time.Time `gorm:"column:created_at;not null"`
	UpdatedAt time.Time `gorm:"column:updated_at;not null"`
}

// TableName overrides the default table name.
func (*Stream) TableName() string {
	return "streams"
}
