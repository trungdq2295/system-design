package com.trungnguyen.synchornizedataservice.repository;

import com.trungnguyen.synchornizedataservice.model.ElasticsearchShard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticSearchConnectionRepository extends JpaRepository<ElasticsearchShard, Long> {
}
