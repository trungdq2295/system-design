package com.trungnguyen.videocaptureservice.repository;


import com.trungnguyen.videocaptureservice.model.mongo.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends MongoRepository<Metadata, String> {
}
