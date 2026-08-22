package rtmp

import (
	"bytes"
	"fmt"
	"io"

	"github.com/sirupsen/logrus"
	rtmp "github.com/yutopp/go-rtmp"
	rtmpmsg "github.com/yutopp/go-rtmp/message"

	"github.com/trungdq2295/encoding-service/internal/encoder"
)

var _ rtmp.Handler = (*Handler)(nil)

// Handler handles one RTMP connection (one published stream).
type Handler struct {
	rtmp.DefaultHandler
	logger *logrus.Logger
	hlsDir string
	enc    *encoder.Encoder
}

func newHandler(logger *logrus.Logger, hlsDir string) *Handler {
	return &Handler{logger: logger, hlsDir: hlsDir}
}

func (h *Handler) OnConnect(timestamp uint32, cmd *rtmpmsg.NetConnectionConnect) error {
	h.logger.WithField("app", cmd.Command.App).Debug("OnConnect")
	return nil
}

func (h *Handler) OnCreateStream(timestamp uint32, cmd *rtmpmsg.NetConnectionCreateStream) error {
	h.logger.Debug("OnCreateStream")
	return nil
}

// OnPublish starts a new encoder when a streamer begins publishing.
func (h *Handler) OnPublish(_ *rtmp.StreamContext, timestamp uint32, cmd *rtmpmsg.NetStreamPublish) error {
	key := cmd.PublishingName
	if key == "" {
		return fmt.Errorf("publishing name (stream key) is empty")
	}

	h.logger.WithField("key", key).Info("stream publishing")

	enc, err := encoder.Start(h.logger, key, h.hlsDir)
	if err != nil {
		return err
	}
	h.enc = enc
	return nil
}

func (h *Handler) OnSetDataFrame(timestamp uint32, data *rtmpmsg.NetStreamSetDataFrame) error {
	if h.enc == nil {
		return nil
	}
	return h.enc.WriteDataFrame(timestamp, bytes.NewReader(data.Payload))
}

func (h *Handler) OnAudio(timestamp uint32, payload io.Reader) error {
	if h.enc == nil {
		return nil
	}
	return h.enc.WriteAudio(timestamp, payload)
}

func (h *Handler) OnVideo(timestamp uint32, payload io.Reader) error {
	if h.enc == nil {
		return nil
	}
	return h.enc.WriteVideo(timestamp, payload)
}

func (h *Handler) OnFCUnpublish(timestamp uint32, cmd *rtmpmsg.NetStreamFCUnpublish) error {
	h.logger.Info("stream unpublished")
	h.stopEncoder()
	return nil
}

func (h *Handler) OnClose() {
	h.logger.Info("connection closed")
	h.stopEncoder()
}

func (h *Handler) stopEncoder() {
	if h.enc != nil {
		h.enc.Stop()
		h.enc = nil
	}
}
