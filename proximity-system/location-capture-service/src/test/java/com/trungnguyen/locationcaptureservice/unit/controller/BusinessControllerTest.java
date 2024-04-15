package com.trungnguyen.locationcaptureservice.unit.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungnguyen.locationcaptureservice.constant.CommonConstant;
import com.trungnguyen.locationcaptureservice.constant.MockDataConstant;
import com.trungnguyen.locationcaptureservice.controller.BusinessController;
import com.trungnguyen.locationcaptureservice.enumeration.BusinessType;
import com.trungnguyen.locationcaptureservice.model.BusinessRequest;
import com.trungnguyen.locationcaptureservice.service.BusinessService;
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

@WebMvcTest(BusinessController.class)
public class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BusinessService businessService;

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
        when(businessService.insertBusiness(request)).thenReturn(true);

        this.mockMvc.perform(
                        post(CommonConstant.VERSION_V1 + CommonConstant.BUSINESS_ENDPOINT)
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andReturn();
    }

    @Test
    void shouldReturnStatusCode500IfInsertFailed() throws Exception {
        BusinessRequest request = new BusinessRequest(
                "",
                "Trung Nguyen Test",
                "This is a test",
                BusinessType.CONVENIENCE_STORE,
                MockDataConstant.LONGITUDE,
                MockDataConstant.LATITUDE
        );
        when(businessService.insertBusiness(request)).thenReturn(false);

        this.mockMvc.perform(
                        post(CommonConstant.VERSION_V1 + CommonConstant.BUSINESS_ENDPOINT)
                                .content(mapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
                .andReturn();
    }
}
