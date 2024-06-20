package com.trungnguyen.videocaptureservice.services;

import com.trungnguyen.videocaptureservice.model.GenericResponse;
import com.trungnguyen.videocaptureservice.model.request.MetadataRequest;
import com.trungnguyen.videocaptureservice.model.request.PresignedUrlRequest;

public interface UploadService {

    GenericResponse<String> createPresignedUrl(PresignedUrlRequest request);

    GenericResponse insertMetadata(MetadataRequest request);
}
