package com.trungnguyen.linkshortener.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("links")
@Data
public class Link {

    @Id
    private String id;

    @Field("short_url")
    private String shortUrl;

    @Field("long_url")
    private String longUrl;

    @Field("access_time")
    private int accessTime;
}
