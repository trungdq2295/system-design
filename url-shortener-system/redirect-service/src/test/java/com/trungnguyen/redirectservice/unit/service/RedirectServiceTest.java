package com.trungnguyen.redirectservice.unit.service;


import com.trungnguyen.redirectservice.cache.CacheComponent;
import com.trungnguyen.redirectservice.constant.FakeConstant;
import com.trungnguyen.redirectservice.model.Link;
import com.trungnguyen.redirectservice.repository.LinkRepository;
import com.trungnguyen.redirectservice.service.RedirectService;
import com.trungnguyen.redirectservice.service.impl.RedirectServiceImpl;
import com.trungnguyen.redirectservice.utils.UtilsFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RedirectServiceTest {


    @Mock
    LinkRepository linkRepository;

    @Mock
    CacheComponent<String> cacheComponent;

    @Mock
    MongoTemplate mongoTemplate;

    private RedirectService redirectService;

    @BeforeEach
    public void setUp() {
        redirectService = new RedirectServiceImpl(linkRepository, cacheComponent, mongoTemplate);
    }


    @Test
    public void given_WhenNoDataInCache_ButHaveDataInDB(){
        Link link = new Link();
        link.setId("123");
        link.setShortUrl(FakeConstant.SHORT_URL);
        link.setLongUrl(FakeConstant.LONG_URL);
        link.setAccessTime(1);
        String shortUrl = FakeConstant.SHORT_URL;
        when(linkRepository.findLinkByShortUrl(shortUrl)).thenReturn(Optional.of(link));
        when(cacheComponent.getKey(UtilsFunction.buildKeyRedisCache(shortUrl))).thenReturn(null);
        String longUrl = redirectService.getLongUrl(shortUrl);

        assertThat(longUrl).isEqualTo(FakeConstant.LONG_URL);
    }

    @Test
    public void given_WhenNoDataInCache_And_NoDataInDB() {
        String shortUrl = FakeConstant.SHORT_URL;
        when(linkRepository.findLinkByShortUrl(shortUrl)).thenReturn(Optional.ofNullable(null));
        when(cacheComponent.getKey(UtilsFunction.buildKeyRedisCache(shortUrl))).thenReturn(null);
        String longUrl = redirectService.getLongUrl(shortUrl);

        assertThat(longUrl).isNull();
    }

    @Test
    public void given_WhenHaveDataInCache() {
        String shortUrl = FakeConstant.SHORT_URL;
        when(cacheComponent.getKey(UtilsFunction.buildKeyRedisCache(shortUrl))).thenReturn(FakeConstant.LONG_URL_IN_CACHE);
        String longUrl = redirectService.getLongUrl(shortUrl);
        assertThat(longUrl).isEqualTo(FakeConstant.LONG_URL_IN_CACHE);
    }
}
