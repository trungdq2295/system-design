package com.trungnguyen.urlshortener.controller;


import com.trungnguyen.urlshortener.constant.CommonConstant;
import com.trungnguyen.urlshortener.enumeration.StatusCode;
import com.trungnguyen.urlshortener.model.GenericResponse;
import com.trungnguyen.urlshortener.model.LinkRequest;
import com.trungnguyen.urlshortener.model.LinkResponse;
import com.trungnguyen.urlshortener.service.LinkShortenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonConstant.VERSION_V1 + CommonConstant.URL_LINK_SHORTEN)
public class LinkShortenController {


    private LinkShortenService linkShortenService;

    public LinkShortenController(LinkShortenService linkShortenService) {
        this.linkShortenService = linkShortenService;
    }

    @PostMapping
    public GenericResponse shortenLink(@RequestBody LinkRequest request) {
        if (request.url().isEmpty()) {
            return GenericResponse.builder()
                    .status(StatusCode.BAD_REQUEST.getStatusCode())
                    .message("Invalid Url")
                    .build();
        }
        LinkResponse data = linkShortenService.generateShortUrl(request.url());
        return GenericResponse.builder()
                .status(StatusCode.OK.getStatusCode())
                .message("Generated Successfully")
                .data(data)
                .build();
    }
}
