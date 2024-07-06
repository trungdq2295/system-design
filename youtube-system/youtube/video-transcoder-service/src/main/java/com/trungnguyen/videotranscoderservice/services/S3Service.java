package com.trungnguyen.videotranscoderservice.services;

public interface S3Service {

    byte[] getS3Video(String name);

    void putS3Video(byte[] data, String fileName);
}
