package com.trungnguyen.videocaptureservice.services.impl;


import com.trungnguyen.videocaptureservice.services.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@Service
@Log4j2
public class S3ServiceImpl implements S3Service {


    public String createPresignedUrl(String name) {
        String accessAWSKey = System.getenv("ACCESS_AWS_KEY");
        String secretAccessAWSKey = System.getenv("SECRET_ACCESS_AWS_KEY");
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessAWSKey,secretAccessAWSKey);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCreds);


        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.AP_SOUTHEAST_2)
                .credentialsProvider(credentialsProvider)
                .build()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket("youtube-system")
                    .key(name)
                    .metadata(new HashMap<>())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL expires in 10 minutes.
                    .putObjectRequest(objectRequest)
                    .build();


            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String myURL = presignedRequest.url().toString();
            log.info("Presigned URL to upload a file to: [{}]", myURL);
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }
}
