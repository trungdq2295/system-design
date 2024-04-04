package com.trungnguyen.linkshortener.service;

import com.trungnguyen.linkshortener.model.LinkResponse;

public interface LinkShortenService {

    LinkResponse generateShortUrl(String url);
}
