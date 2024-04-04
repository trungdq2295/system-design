package com.trungnguyen.redirectservice.unit.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.redirectservice.constant.FakeConstant;
import com.trungnguyen.redirectservice.controller.RedirectController;
import com.trungnguyen.redirectservice.service.RedirectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedirectController.class)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    RedirectService redirectService;


    @Test
    void shouldReturn404IfUrlIsEmpty() throws Exception {
        this.mockMvc.perform(
                        get("/")
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldReturn404IfUrlIsNotAvailableInDBOrCache() throws Exception {
        when(redirectService.getLongUrl(FakeConstant.SHORT_URL)).thenReturn(null);
        this.mockMvc.perform(
                        get("/testString")
                ).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void given_WhenUserInputValid_LongUrl_And_DBHasIt() throws Exception {
        when(redirectService.getLongUrl(FakeConstant.SHORT_URL)).thenReturn(FakeConstant.LONG_URL);
        this.mockMvc.perform(
                        get("/" + FakeConstant.SHORT_URL)
                ).andExpect(status().isMovedPermanently())
                .andExpect(MockMvcResultMatchers.header().stringValues("Location", FakeConstant.LONG_URL))
                .andReturn();
    }
}
