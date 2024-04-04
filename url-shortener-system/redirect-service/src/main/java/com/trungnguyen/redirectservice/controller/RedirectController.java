package com.trungnguyen.redirectservice.controller;


import com.trungnguyen.redirectservice.service.RedirectService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class RedirectController {

    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("{shortUrl}")
    public ResponseEntity<Void> redirectToAnotherURL(@PathVariable String shortUrl, HttpServletResponse response) {
        if (shortUrl.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String longUrl = redirectService.getLongUrl(shortUrl);
        if (Objects.isNull(longUrl)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        response.setHeader("Location", longUrl);
        return new ResponseEntity<>(HttpStatus.MOVED_PERMANENTLY);
    }
}
