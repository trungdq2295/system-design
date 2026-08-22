// Package encoder manages one FFmpeg transcode process (FLV -> HLS) per stream.
package encoder

import (
	"bytes"
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"time"

	"github.com/sirupsen/logrus"
	"github.com/yutopp/go-flv"
	flvtag "github.com/yutopp/go-flv/tag"
)

// Encoder wraps an FFmpeg subprocess that reads FLV from stdin and writes HLS.
type Encoder struct {
	key    string
	logger *logrus.Logger
	cmd    *exec.Cmd
	stdin  io.WriteCloser
	flv    *flv.Encoder
}

// Start spawns FFmpeg reading FLV from stdin and transcoding to HLS in hlsDir.
func Start(logger *logrus.Logger, key, hlsDir string) (*Encoder, error) {
	segPattern := filepath.Join(hlsDir, key+"-%03d.ts")
	playlist := filepath.Join(hlsDir, key+".m3u8")

	args := []string{
		"-f", "flv", "-i", "pipe:0",
		"-loglevel", "warning",
		// Transcode video to 720p H.264 with a 2s keyframe interval (GOP 60 @30fps).
		"-c:v", "libx264", "-preset", "veryfast",
		"-b:v", "2500k", "-maxrate", "2500k", "-bufsize", "5000k",
		"-vf", "scale=-2:720",
		"-g", "60", "-keyint_min", "60", "-sc_threshold", "0",
		"-c:a", "aac", "-b:a", "128k", "-ar", "44100",
		"-f", "hls", "-hls_time", "4", "-hls_list_size", "6", "-hls_flags", "delete_segments",
		"-hls_segment_filename", segPattern,
		playlist,
	}

	cmd := exec.Command("ffmpeg", args...)
	cmd.Stderr = os.Stderr

	stdin, err := cmd.StdinPipe()
	if err != nil {
		return nil, fmt.Errorf("failed to get ffmpeg stdin pipe: %w", err)
	}

	if err := cmd.Start(); err != nil {
		return nil, fmt.Errorf("failed to start ffmpeg: %w", err)
	}

	flvEnc, err := flv.NewEncoder(stdin, flv.FlagsAudio|flv.FlagsVideo)
	if err != nil {
		_ = stdin.Close()
		_ = cmd.Process.Kill()
		return nil, fmt.Errorf("failed to create flv encoder: %w", err)
	}

	logger.WithField("key", key).Info("encoder started")

	return &Encoder{
		key:    key,
		logger: logger,
		cmd:    cmd,
		stdin:  stdin,
		flv:    flvEnc,
	}, nil
}

// WriteAudio muxes one audio message into the FLV stream.
func (e *Encoder) WriteAudio(timestamp uint32, payload io.Reader) error {
	var audio flvtag.AudioData
	if err := flvtag.DecodeAudioData(payload, &audio); err != nil {
		return fmt.Errorf("decode audio: %w", err)
	}

	buf, err := drain(audio.Data)
	if err != nil {
		return err
	}
	audio.Data = buf

	return e.flv.Encode(&flvtag.FlvTag{
		TagType:   flvtag.TagTypeAudio,
		Timestamp: timestamp,
		Data:      &audio,
	})
}

// WriteVideo muxes one video message into the FLV stream.
func (e *Encoder) WriteVideo(timestamp uint32, payload io.Reader) error {
	var video flvtag.VideoData
	if err := flvtag.DecodeVideoData(payload, &video); err != nil {
		return fmt.Errorf("decode video: %w", err)
	}

	buf, err := drain(video.Data)
	if err != nil {
		return err
	}
	video.Data = buf

	return e.flv.Encode(&flvtag.FlvTag{
		TagType:   flvtag.TagTypeVideo,
		Timestamp: timestamp,
		Data:      &video,
	})
}

// WriteDataFrame muxes the onMetaData script tag into the FLV stream.
func (e *Encoder) WriteDataFrame(timestamp uint32, payload io.Reader) error {
	var script flvtag.ScriptData
	if err := flvtag.DecodeScriptData(payload, &script); err != nil {
		// Metadata is optional; the A/V sequence headers carry the codec info.
		e.logger.WithError(err).Warn("failed to decode script data, ignoring")
		return nil
	}

	return e.flv.Encode(&flvtag.FlvTag{
		TagType:   flvtag.TagTypeScriptData,
		Timestamp: timestamp,
		Data:      &script,
	})
}

// Stop closes stdin (EOF to FFmpeg) and waits for the process to exit.
func (e *Encoder) Stop() {
	if e.stdin != nil {
		_ = e.stdin.Close()
	}

	done := make(chan struct{})
	go func() {
		_ = e.cmd.Wait()
		close(done)
	}()

	select {
	case <-done:
	case <-time.After(5 * time.Second):
		_ = e.cmd.Process.Kill()
		<-done
	}

	e.logger.WithField("key", e.key).Info("encoder stopped")
}

func drain(r io.Reader) (*bytes.Buffer, error) {
	buf := new(bytes.Buffer)
	if _, err := io.Copy(buf, r); err != nil {
		return nil, err
	}
	return buf, nil
}
