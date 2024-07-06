package com.trungnguyen.videotranscoderservice.services;

public interface VideoConversion {

    byte[] convertTo720p(byte[] video);


    byte[] convertTo480p(byte[] video);
}
