package com.trungnguyen.urlshortener.unit.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.urlshortener.constant.CommonConstant;
import com.trungnguyen.urlshortener.constant.FakeConstant;
import com.trungnguyen.urlshortener.controller.LinkShortenController;
import com.trungnguyen.urlshortener.model.LinkRequest;
import com.trungnguyen.urlshortener.model.LinkResponse;
import com.trungnguyen.urlshortener.service.LinkShortenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkShortenController.class)
public class LinkShortenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    LinkShortenService linkShortenService;

    @Test
    void shouldReturnStatus401ifUrlIsEmpty() throws Exception {
        LinkRequest request = new LinkRequest("");

        this.mockMvc.perform(
                        post(CommonConstant.VERSION_V1 + CommonConstant.URL_LINK_SHORTEN)
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(401))
                .andReturn();

    }

    @Test
    void shouldReturnShortUrl() throws Exception {
        LinkRequest request = new LinkRequest(FakeConstant.LONG_URL);
        LinkResponse response = new LinkResponse(FakeConstant.SHORT_URL);
        when(linkShortenService.generateShortUrl(FakeConstant.LONG_URL)).thenReturn(response);

        this.mockMvc.perform(
                        post(CommonConstant.VERSION_V1 + CommonConstant.URL_LINK_SHORTEN)
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shortUrl").value(FakeConstant.SHORT_URL))
                .andReturn();
    }
}
