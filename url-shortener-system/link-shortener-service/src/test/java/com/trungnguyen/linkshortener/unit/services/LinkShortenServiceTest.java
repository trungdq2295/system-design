package com.trungnguyen.linkshortener.unit.services;

import com.trungnguyen.linkshortener.cache.CacheInterface;
import com.trungnguyen.linkshortener.model.Link;
import com.trungnguyen.linkshortener.model.LinkResponse;
import com.trungnguyen.linkshortener.repository.LinkRepository;
import com.trungnguyen.linkshortener.service.LinkShortenService;
import com.trungnguyen.linkshortener.service.implement.LinkShortenServiceImpl;
import com.trungnguyen.linkshortener.utils.UtilsFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkShortenServiceTest {

    @Mock
    LinkRepository linkRepository;

    @Mock
    CacheInterface<String> cacheInterface;

    private LinkShortenService shortenService;

    @BeforeEach
    public void setUp() {
        shortenService = new LinkShortenServiceImpl(linkRepository, cacheInterface);
    }
    private static final String LONG_URL = "https://google.com";
    private static final String SHORT_URL = "https://trungnguyen.com/abc";

    @Test
    public void givenWhen_UserInputLongUrl_NotExistedInDB() {
        Link link = new Link();
        link.setId("123");
        link.setShortUrl(SHORT_URL);
        link.setLongUrl(LONG_URL);
        link.setAccessTime(1);
        when(linkRepository.insert((Link) any())).thenReturn(link);

        when(linkRepository.findLinkByLongUrl(LONG_URL)).thenReturn(Optional.of(new Link()));
        LinkResponse result = shortenService.generateShortUrl(LONG_URL);
        Assertions.assertEquals(SHORT_URL, result.shortUrl());
    }

    @Test
    public void givenWhen_UserInputLongUrl_existed_inDB() {
        Link link = new Link();
        link.setId("123");
        link.setShortUrl(SHORT_URL);
        link.setLongUrl(LONG_URL);
        link.setAccessTime(1);
        when(linkRepository.findLinkByLongUrl(LONG_URL)).thenReturn(Optional.of(link));
        LinkResponse result = shortenService.generateShortUrl(LONG_URL);
        Assertions.assertEquals(SHORT_URL, result.shortUrl());
    }

    @Test
    public void givenWhen_UserInputLongUrl_existed_InCache() {
        Link link = new Link();
        link.setId("123");
        link.setShortUrl(SHORT_URL);
        link.setLongUrl(LONG_URL);
        link.setAccessTime(1);
        String cacheValue = "Test456";
        when(cacheInterface.getKey(UtilsFunction.buildKeyRedisCache(LONG_URL))).thenReturn(cacheValue);
        LinkResponse result = shortenService.generateShortUrl(LONG_URL);
        Assertions.assertEquals(cacheValue, result.shortUrl());
    }
}
