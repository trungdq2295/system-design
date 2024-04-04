package com.trungnguyen.linkshortener.intergration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.linkshortener.configuration.MongoDBTestConfiguration;
import com.trungnguyen.linkshortener.constant.CommonConstant;
import com.trungnguyen.linkshortener.constant.FakeConstant;
import com.trungnguyen.linkshortener.model.GenericResponse;
import com.trungnguyen.linkshortener.model.Link;
import com.trungnguyen.linkshortener.model.LinkRequest;
import com.trungnguyen.linkshortener.model.LinkResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {MongoDBTestConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LinkShortenIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReturn401StatusIfUrlRequestIs_Empty() throws JsonProcessingException {
        LinkRequest linkRequest = new LinkRequest("");
        String requestBody = mapper.writeValueAsString(linkRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        GenericResponse response = restTemplate.exchange(CommonConstant.VERSION_V1 + CommonConstant.URL_LINK_SHORTEN, HttpMethod.POST, requestEntity, GenericResponse.class).getBody();
        assertThat(response.status()).isEqualTo(401);
    }

    @Test
    void shouldReturnValueIfUrlQuestIs_NotEmpty() throws JsonProcessingException {
        LinkRequest linkRequest = new LinkRequest(FakeConstant.LONG_URL);
        String requestBody = mapper.writeValueAsString(linkRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ParameterizedTypeReference<GenericResponse<LinkResponse>> responseType = new ParameterizedTypeReference<GenericResponse<LinkResponse>>() {
        };
        GenericResponse<LinkResponse> response = restTemplate.exchange(CommonConstant.VERSION_V1 + CommonConstant.URL_LINK_SHORTEN, HttpMethod.POST, requestEntity, responseType).getBody();
        assertThat(response).isNotNull();
        LinkResponse linkResponse = response.data();
        assertThat(linkResponse.shortUrl()).isNotEmpty();
    }

    @Test
    void shouldReturnValueInDBIfUrlIsAlreadyExists() throws JsonProcessingException {
        Link link = new Link();
        link.setId("test-id");
        link.setShortUrl(FakeConstant.SHORT_URL);
        link.setLongUrl(FakeConstant.LONG_URL);
        mongoTemplate.insert(link);

        LinkRequest linkRequest = new LinkRequest(FakeConstant.LONG_URL);
        String requestBody = mapper.writeValueAsString(linkRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ParameterizedTypeReference<GenericResponse<LinkResponse>> responseType = new ParameterizedTypeReference<GenericResponse<LinkResponse>>() {
        };
        GenericResponse<LinkResponse> response = restTemplate.exchange(CommonConstant.VERSION_V1 + CommonConstant.URL_LINK_SHORTEN, HttpMethod.POST, requestEntity, responseType).getBody();
        assertThat(response).isNotNull();
        LinkResponse linkResponse = response.data();
        assertThat(linkResponse.shortUrl()).isEqualTo(link.getShortUrl());
    }

}
