package com.trungnguyen.videotranscoderservice.repository;


import com.trungnguyen.videotranscoderservice.model.mongo.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends MongoRepository<Metadata, String> {
}
