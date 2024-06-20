package com.trungnguyen.videocaptureservice.model.request;

public record MetadataRequest(
        String title,
        Long uploaderId,
        String uploadUrl
) {


}
