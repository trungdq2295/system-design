package com.trungnguyen.videocaptureservice.services.impl;


import com.trungnguyen.videocaptureservice.enumeration.VideoStatus;
import com.trungnguyen.videocaptureservice.model.GenericResponse;
import com.trungnguyen.videocaptureservice.model.ProducerMessage;
import com.trungnguyen.videocaptureservice.model.mongo.Metadata;
import com.trungnguyen.videocaptureservice.model.request.MetadataRequest;
import com.trungnguyen.videocaptureservice.model.request.PresignedUrlRequest;
import com.trungnguyen.videocaptureservice.producer.VideoProducer;
import com.trungnguyen.videocaptureservice.repository.MetadataRepository;
import com.trungnguyen.videocaptureservice.services.S3Service;
import com.trungnguyen.videocaptureservice.services.UploadService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UploadServiceImpl implements UploadService {


    private final S3Service s3Service;

    private final MetadataRepository metadataRepository;

    private final VideoProducer producer;

    public UploadServiceImpl(S3Service s3Service, MetadataRepository metadataRepository, VideoProducer producer) {
        this.s3Service = s3Service;
        this.metadataRepository = metadataRepository;
        this.producer = producer;
    }

    @Override
    public GenericResponse<String> createPresignedUrl(PresignedUrlRequest request) {
        return GenericResponse.ok(s3Service.createPresignedUrl(request.fileName()));
    }

    @Override
    public GenericResponse insertMetadata(MetadataRequest request) {
        Metadata metadata = new Metadata();
        metadata.setVideoId(UUID.randomUUID().toString());
        metadata.setVideoTitle(request.title());
        metadata.setUploadUrl(request.uploadUrl());
        metadata.setUploaderId(request.uploaderId());
        metadata.setUploadStatus(VideoStatus.PENDING);
        metadataRepository.save(metadata);
        ProducerMessage<Metadata> data = ProducerMessage.buildMessage(metadata);
        CompletableFuture.runAsync(() -> producer.produceMessage(data));
        return GenericResponse.ok(true);
    }
}
