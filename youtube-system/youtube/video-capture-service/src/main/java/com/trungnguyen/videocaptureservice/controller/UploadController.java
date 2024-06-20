package com.trungnguyen.videocaptureservice.controller;


import com.trungnguyen.videocaptureservice.constant.CommonConstant;
import com.trungnguyen.videocaptureservice.model.GenericResponse;
import com.trungnguyen.videocaptureservice.model.request.MetadataRequest;
import com.trungnguyen.videocaptureservice.model.request.PresignedUrlRequest;
import com.trungnguyen.videocaptureservice.services.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonConstant.UPLOAD_ENDPOINT)
public class UploadController {

    private UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(CommonConstant.PRESIGNED_ENDPOINT)
    public GenericResponse createPresignedUrl(@RequestBody PresignedUrlRequest request) {
        return uploadService.createPresignedUrl(request);
    }


    @PostMapping
    public GenericResponse insertMetadata(@RequestBody MetadataRequest request) {
        try {
            return uploadService.insertMetadata(request);
        } catch (Exception e) {
            return GenericResponse.failed();
        }
    }
}
