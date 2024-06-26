package com.trungnguyen.linkshortener.controller;


import com.trungnguyen.linkshortener.constant.CommonConstant;
import com.trungnguyen.linkshortener.enumeration.StatusCode;
import com.trungnguyen.linkshortener.model.GenericResponse;
import com.trungnguyen.linkshortener.model.LinkRequest;
import com.trungnguyen.linkshortener.model.LinkResponse;
import com.trungnguyen.linkshortener.service.LinkShortenService;
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
