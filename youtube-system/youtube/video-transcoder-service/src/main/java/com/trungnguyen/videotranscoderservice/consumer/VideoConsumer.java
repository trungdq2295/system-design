package com.trungnguyen.videotranscoderservice.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.videotranscoderservice.model.mongo.Metadata;
import com.trungnguyen.videotranscoderservice.services.S3Service;
import com.trungnguyen.videotranscoderservice.services.VideoConversion;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VideoConsumer {

    private final String TOPIC_VIDEO_CAPTURE = "video-capture-topic";

    private final String GROUP_720P_HD = "720p-group";

    private final String GROUP_480P_HD = "480-group";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final S3Service s3Service;

    private final VideoConversion videoConversion;

    public VideoConsumer(S3Service s3Service, VideoConversion videoConversion) {
        this.s3Service = s3Service;
        this.videoConversion = videoConversion;
    }

    @KafkaListener(topics = TOPIC_VIDEO_CAPTURE, groupId = GROUP_720P_HD)
    public void listen720pGroup(String message) throws JsonProcessingException {
        var metadata = objectMapper.readValue(message, Metadata.class);
        byte[] data = s3Service.getS3Video(metadata.getVideoTitle());
        byte[] dataConversion = videoConversion.convertTo720p(data);
        s3Service.putS3Video(dataConversion, metadata.getVideoTitle() + "-720p");
    }

    @KafkaListener(topics = TOPIC_VIDEO_CAPTURE, groupId = GROUP_480P_HD)
    public void listen480Group(String message) throws JsonProcessingException {
        var metadata = objectMapper.readValue(message, Metadata.class);
        byte[] data = s3Service.getS3Video(metadata.getVideoTitle());
        byte[] dataConversion = videoConversion.convertTo480p(data);
        s3Service.putS3Video(dataConversion, metadata.getVideoTitle() + "-480p");
    }

}
