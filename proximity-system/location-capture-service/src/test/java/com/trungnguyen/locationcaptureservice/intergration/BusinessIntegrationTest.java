package com.trungnguyen.locationcaptureservice.intergration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.locationcaptureservice.configuration.CassandraTestConfiguration;
import com.trungnguyen.locationcaptureservice.constant.CommonConstant;
import com.trungnguyen.locationcaptureservice.constant.MockDataConstant;
import com.trungnguyen.locationcaptureservice.enumeration.BusinessType;
import com.trungnguyen.locationcaptureservice.model.BusinessRequest;
import com.trungnguyen.locationcaptureservice.model.GenericResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {CassandraTestConfiguration.class})
public class BusinessIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturn200IfInsertOk() throws Exception {
        BusinessRequest request = new BusinessRequest(
                "",
                "Trung Nguyen Test",
                "This is a test",
                BusinessType.CONVENIENCE_STORE,
                MockDataConstant.LONGITUDE,
                MockDataConstant.LATITUDE
        );
        String requestBody = mapper.writeValueAsString(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        GenericResponse response = restTemplate.exchange(CommonConstant.VERSION_V1 + CommonConstant.BUSINESS_ENDPOINT, HttpMethod.POST, requestEntity, GenericResponse.class).getBody();
        assertThat(response.status()).isEqualTo(200);
    }
}
