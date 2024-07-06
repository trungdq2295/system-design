package com.trungnguyen.videotranscoderservice.model.mongo;


import com.trungnguyen.videotranscoderservice.enumeration.VideoStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("metadata")
public class Metadata {

    @Id
    @Field("video_id")
    private String videoId;

    @Field("video_hashing_url")
    private String videoHashingUrl;

    @Field("video_title")
    private String videoTitle;

    @Field("uploader_id")
    private Long uploaderId;

    @Field("upload_url")
    private String uploadUrl;

    @Field("upload_status")
    private VideoStatus uploadStatus;

    @Field("720p_url")
    private String url720p;

    @Field("hd_url")
    private int urlHd;

}
