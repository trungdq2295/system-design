package com.trungnguyen.redirectservice.repository;

import com.trungnguyen.redirectservice.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends MongoRepository<Link, String> {

    @Query("{'short_url': '?0'}")
    Optional<Link> findLinkByShortUrl(String url);

}
