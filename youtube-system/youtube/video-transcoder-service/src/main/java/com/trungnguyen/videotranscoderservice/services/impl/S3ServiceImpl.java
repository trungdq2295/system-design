package com.trungnguyen.videotranscoderservice.services.impl;


import com.trungnguyen.videotranscoderservice.services.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@Log4j2
public class S3ServiceImpl implements S3Service {

    @Override
    public byte[] getS3Video(String name) {
        S3Client s3 = provideS3Client();
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(name)
                .bucket("youtube-system")
                .build();
        ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
        return objectBytes.asByteArray();
    }

    public void putS3Video(byte[] data, String fileName) {
        S3Client s3 = provideS3Client();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("youtube-system")
                .key(fileName)
                .build();
        PutObjectResponse response = s3.putObject(putObjectRequest, RequestBody.fromBytes(data));
        System.out.println(response.responseMetadata());
    }

    private static S3Client provideS3Client() {
        String accessAWSKey = System.getenv("ACCESS_AWS_KEY");
        String secretAccessAWSKey = System.getenv("SECRET_ACCESS_AWS_KEY");
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessAWSKey, secretAccessAWSKey);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCreds);
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.AP_SOUTHEAST_2)
                .build();

    }
}
