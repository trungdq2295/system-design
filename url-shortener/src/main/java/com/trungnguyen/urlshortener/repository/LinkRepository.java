package com.trungnguyen.urlshortener.repository;

import com.trungnguyen.urlshortener.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends MongoRepository<Link, String> {

    @Query("{'short_url': '?0'}")
    Optional<Link> findLinkByShortUrl(String url);

    @Query("{'long_url': '?0'}")
    Optional<Link> findLinkByLongUrl(String url);
}
