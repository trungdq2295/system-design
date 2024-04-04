package com.trungnguyen.linkshortener.service.implement;


import com.trungnguyen.linkshortener.cache.CacheInterface;
import com.trungnguyen.linkshortener.model.Link;
import com.trungnguyen.linkshortener.model.LinkResponse;
import com.trungnguyen.linkshortener.repository.LinkRepository;
import com.trungnguyen.linkshortener.service.LinkShortenService;
import com.trungnguyen.linkshortener.utils.UtilsFunction;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class LinkShortenServiceImpl implements LinkShortenService {

    private final LinkRepository linkRepository;

    private final CacheInterface<String> cacheInterface;

    public LinkShortenServiceImpl(LinkRepository linkRepository, CacheInterface<String> cacheInterface) {
        this.linkRepository = linkRepository;
        this.cacheInterface = cacheInterface;
    }

    @Override
    public LinkResponse generateShortUrl(String url) {
        Link linkInDB = findByLongUrl(url);
        if(Objects.nonNull(cacheInterface.getKey(UtilsFunction.buildKeyRedisCache(url)))){
            return new LinkResponse(cacheInterface.getKey(UtilsFunction.buildKeyRedisCache(url)));
        }
        if(!Objects.isNull(linkInDB.getId())){
            cacheInterface.addCache(UtilsFunction.buildKeyRedisCache(url), linkInDB.getShortUrl());
            return new LinkResponse(linkInDB.getShortUrl());
        }
        String id = UUID.randomUUID().toString();
        String shortUrl = id.substring(0,5);
        boolean isExisted = true;
        while(isExisted){
            isExisted = linkRepository.findLinkByShortUrl(shortUrl).isPresent();
            id = UUID.randomUUID().toString();
            shortUrl = id.substring(0,5);
        }
        Link link = new Link();
        link.setId(id);
        link.setShortUrl(shortUrl);
        link.setLongUrl(url);
        link.setAccessTime(1);
        link = linkRepository.insert(link);
        cacheInterface.addCache(UtilsFunction.buildKeyRedisCache(url), link.getShortUrl());
        return new LinkResponse(link.getShortUrl());
    }

    public Link findByLongUrl(String longUrl){
        Optional<Link> link = linkRepository.findLinkByLongUrl(longUrl);
        return link.orElseGet(Link::new);
    }
}
