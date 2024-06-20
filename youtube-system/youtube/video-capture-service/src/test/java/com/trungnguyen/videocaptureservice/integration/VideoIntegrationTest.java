package com.trungnguyen.videocaptureservice.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.videocaptureservice.configuration.MongoDBTestConfiguration;
import com.trungnguyen.videocaptureservice.constant.CommonConstant;
import com.trungnguyen.videocaptureservice.model.GenericResponse;
import com.trungnguyen.videocaptureservice.model.request.PresignedUrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {MongoDBTestConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VideoIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    private RestTemplate restTemplateAbc = new RestTemplate();

    @Test
    void shouldReturnPresignedUrl() throws JsonProcessingException, URISyntaxException {
        String path = "./src/test/resources";
        String fileName = "video.mp4";
        File file = new File(path, fileName);
        PresignedUrlRequest request = new PresignedUrlRequest(file.getName());
        String requestBody = mapper.writeValueAsString(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ParameterizedTypeReference<GenericResponse> responseType = new ParameterizedTypeReference<>() {
        };
        GenericResponse response = restTemplate.exchange(CommonConstant.UPLOAD_ENDPOINT + CommonConstant.PRESIGNED_ENDPOINT, HttpMethod.POST, requestEntity, responseType).getBody();
        assertThat(response).isNotNull();
        String uploadUrl = (String) response.data();
        assertThat(uploadUrl).isNotEmpty();

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        Resource resource = new FileSystemResource(file);
        map.add("file", resource);

        // Step 4: Set the headers for the file upload request
        HttpHeaders headersUploadToS3 = new HttpHeaders();
        headersUploadToS3.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntityMultipart = new HttpEntity<>(map, headersUploadToS3);

        // Step 5: Upload the file using the presigned URL
        restTemplateAbc.exchange(
                new URI(uploadUrl),
                HttpMethod.PUT,
                requestEntityMultipart,
                Void.class
        );

    }

}
