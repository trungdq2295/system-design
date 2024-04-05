package com.trungnguyen.locationcaptureservice.repository.cassandra;

import com.trungnguyen.locationcaptureservice.model.cassandra.Location;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CassandraRepository<Location, String> {
}
