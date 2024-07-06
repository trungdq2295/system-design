package com.trungnguyen.videotranscoderservice.services.impl;


import com.trungnguyen.videotranscoderservice.enumeration.VideoResolution;
import com.trungnguyen.videotranscoderservice.services.VideoConversion;
import org.springframework.stereotype.Service;
import ws.schild.jave.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
public class VideoConversionImpl implements VideoConversion {
    @Override
    public byte[] convertTo720p(byte[] videoBytes) {
        return convertVideo(VideoResolution.HD_720, videoBytes);
    }

    @Override
    public byte[] convertTo480p(byte[] videoBytes) {
        return convertVideo(VideoResolution.HD_480, videoBytes);
    }

    private byte[] convertVideo(VideoResolution videoResolution, byte[] videoBytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File tempFile = null;
        File output = null;
        try {
            // Write the byte[] to a temporary file
            tempFile = File.createTempFile("temp-video", ".mp4");
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(videoBytes);
            fos.close();

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            VideoAttributes video = new VideoAttributes();
            switch (videoResolution) {
                case HD_720 -> {
                    video.setCodec("h264");
                    video.setBitRate(1500000); // Adjust bitrate as needed
                    video.setFrameRate(60);
                    video.setSize(new VideoSize(1280, 720)); // Set resolution to 720p
                    output = new File("output-720p");
                }
                case HD_480 -> {
                    video.setCodec("h264");
                    video.setBitRate(800000);
                    video.setFrameRate(30);
                    video.setSize(new VideoSize(640, 480));
                    output = new File("output-480p");
                }
            }

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp4");
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(tempFile), output, attrs);
            // Read the converted video into a byte[]
            FileInputStream fis = new FileInputStream(output);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {

        } finally {
            // Clean up temporary files
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
        return baos.toByteArray();
    }
}
