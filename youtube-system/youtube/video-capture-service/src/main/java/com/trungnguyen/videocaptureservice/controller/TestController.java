package com.trungnguyen.videocaptureservice.controller;

import com.trungnguyen.videocaptureservice.constant.CommonConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonConstant.TEST_ENDPOINT)
public class TestController {

    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }
}
