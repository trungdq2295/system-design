package com.trungnguyen.urlshortener.service;

import com.trungnguyen.urlshortener.model.LinkResponse;

public interface LinkShortenService {

    LinkResponse generateShortUrl(String url);
}
