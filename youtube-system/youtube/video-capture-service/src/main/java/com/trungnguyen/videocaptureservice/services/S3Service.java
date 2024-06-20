package com.trungnguyen.videocaptureservice.services;

public interface S3Service {

    String createPresignedUrl(String name);
}
