package com.trungnguyen.urlshortener.controller;


import com.trungnguyen.urlshortener.constant.CommonConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonConstant.VERSION_V1 + "test")
public class TestController {

    @GetMapping
    public String helloWorld(){
        return "Hello World";
    }
}
