package com.trungnguyen.synchornizedataservice.repository.cassandra;

import com.trungnguyen.synchornizedataservice.model.cassandra.Location;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CassandraRepository<Location, String> {

    Optional<List<Location>> findAllByTimestamp(Long timestamp);

    @AllowFiltering
    Optional<List<Location>> findAllByTimestampAfter(long timestamp);
}
