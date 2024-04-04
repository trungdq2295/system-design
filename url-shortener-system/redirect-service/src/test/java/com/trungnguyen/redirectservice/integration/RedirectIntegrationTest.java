package com.trungnguyen.redirectservice.integration;


import com.trungnguyen.redirectservice.configuration.MongoDBTestConfiguration;
import com.trungnguyen.redirectservice.configuration.RedisTestConfiguration;
import com.trungnguyen.redirectservice.constant.FakeConstant;
import com.trungnguyen.redirectservice.model.Link;
import com.trungnguyen.redirectservice.utils.UtilsFunction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {MongoDBTestConfiguration.class, RedisTestConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedirectIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void shouldReturn404IfEmptyShortUrl() {
        ParameterizedTypeReference<Void> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Void> response = restTemplate.exchange("/", HttpMethod.GET, null, responseType);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void shouldReturn404IfNoLongUrlExistedInDB() {
        /**
         * Insert Data to DB first
         */
        Link link = new Link();
        link.setId("test-id-1");
        link.setShortUrl(FakeConstant.SHORT_URL);
        link.setLongUrl(FakeConstant.LONG_URL);
        mongoTemplate.insert(link);

        String fakeURL = "12312312";
        ParameterizedTypeReference<Void> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Void> response = restTemplate.exchange("/" + fakeURL, HttpMethod.GET, null, responseType);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnLongURLIfLongUrlExistedInDB() {
        /**
         * Insert Data to DB first
         */
        Link link = new Link();
        link.setId("test-id-2");
        link.setShortUrl(FakeConstant.SHORT_URL_2);
        link.setLongUrl(FakeConstant.LONG_URL_2);
        mongoTemplate.insert(link);
        ParameterizedTypeReference<Void> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Void> response = restTemplate.exchange("/" + FakeConstant.SHORT_URL_2, HttpMethod.GET, null, responseType);
        assertThat(response.getHeaders().getFirst("Location")).isEqualTo(FakeConstant.LONG_URL_2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
    }

    @Test
    public void shouldReturnLongURLIfLongUrlExistedInCache() {
        /**
         * Insert Data to DB and Cache first
         */
        Link link = new Link();
        link.setId("test-id-3");
        link.setShortUrl(FakeConstant.SHORT_URL);
        link.setLongUrl(FakeConstant.LONG_URL);
        mongoTemplate.insert(link);
        redisTemplate.opsForValue().set(UtilsFunction.buildKeyRedisCache(FakeConstant.SHORT_URL), FakeConstant.LONG_URL_IN_CACHE);

        ParameterizedTypeReference<Void> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Void> response = restTemplate.exchange("/" + FakeConstant.SHORT_URL, HttpMethod.GET, null, responseType);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY);
        assertThat(response.getHeaders().getFirst("Location")).isEqualTo(FakeConstant.LONG_URL_IN_CACHE);
        assertThat(response.getHeaders().getFirst("Location")).isNotEqualTo(FakeConstant.LONG_URL);
    }
}
