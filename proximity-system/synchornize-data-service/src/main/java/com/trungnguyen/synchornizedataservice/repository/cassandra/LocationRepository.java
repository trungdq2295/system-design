package com.trungnguyen.synchornizedataservice.repository.cassandra;

import com.trungnguyen.synchornizedataservice.model.cassandra.Location;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CassandraRepository<Location, String> {

    Optional<List<Location>> findAllByTimestamp(Long timestamp);

    Optional<List<Location>> findAllByTimestampAfter(Long timestamp);
}
