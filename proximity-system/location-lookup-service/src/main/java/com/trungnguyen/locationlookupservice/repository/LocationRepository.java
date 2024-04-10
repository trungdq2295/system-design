package com.trungnguyen.locationlookupservice.repository;

import com.trungnguyen.locationlookupservice.model.elasticsearch.Location;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends ElasticsearchRepository<Location, String> {

    List<Location> findAll();

    @Query("{\"bool\": {\"must\": {\"match\": {\"type\": ?0}}, \"filter\": {\"geo_distance\": {\"distance\": \"500m\", \"geoPoint\": {\"lat\": ?1, \"lon\": ?2}}}}")
    List<Location> findByTypeAndLocation(String type, double latitude, double longitude);

}
