package com.trungnguyen.redirectservice.service.impl;

import com.trungnguyen.redirectservice.cache.CacheComponent;
import com.trungnguyen.redirectservice.model.Link;
import com.trungnguyen.redirectservice.repository.LinkRepository;
import com.trungnguyen.redirectservice.service.RedirectService;
import com.trungnguyen.redirectservice.utils.UtilsFunction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class RedirectServiceImpl implements RedirectService {

    private final LinkRepository linkRepository;

    private final CacheComponent<String> cacheComponent;

    private final MongoTemplate mongoTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(32);

    public RedirectServiceImpl(LinkRepository linkRepository, CacheComponent<String> cacheComponent, MongoTemplate mongoTemplate) {
        this.linkRepository = linkRepository;
        this.cacheComponent = cacheComponent;
        this.mongoTemplate = mongoTemplate;
    }

    public String getLongUrl(String shortUrl) {
        String redisKey = UtilsFunction.buildKeyRedisCache(shortUrl);
        if (Objects.nonNull(cacheComponent.getKey(redisKey))) {
            updateAccessTime(shortUrl);
            return cacheComponent.getKey(redisKey);
        }
        Optional<Link> link = linkRepository.findLinkByShortUrl(shortUrl);
        if (link.isPresent()) {
            cacheComponent.addCache(redisKey, link.get().getLongUrl());
            updateAccessTime(shortUrl);
            return link.get().getLongUrl();
        }
        return null;
    }

    private void updateAccessTime(String shortUrl) {
        CompletableFuture.runAsync(() -> {
            Query query = new Query(Criteria.where("short_url").is(shortUrl));
            Update update = new Update().inc("access_time", 1);
            mongoTemplate.updateFirst(query, update, Link.class);
        }, executorService);
    }
}
