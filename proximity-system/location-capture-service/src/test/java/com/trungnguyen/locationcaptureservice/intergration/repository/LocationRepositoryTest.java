package com.trungnguyen.locationcaptureservice.intergration.repository;


import com.trungnguyen.locationcaptureservice.configuration.CassandraTestConfiguration;
import com.trungnguyen.locationcaptureservice.constant.MockDataConstant;
import com.trungnguyen.locationcaptureservice.model.cassandra.Location;
import com.trungnguyen.locationcaptureservice.repository.cassandra.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataCassandraTest
@ContextConfiguration(classes = {CassandraTestConfiguration.class})
public class LocationRepositoryTest {

    @Autowired
    LocationRepository locationRepository;

    @Test
    public void given_WhenUserAddLocation() {
        Location location = new Location();
        location.setBusiness_id(MockDataConstant.ID);
        location.setLatitude(MockDataConstant.LATITUDE);
        location.setLongitude(MockDataConstant.LONGITUDE);
        location.setTimestamp(MockDataConstant.TIMESTAMP);
        Location savedLocation = locationRepository.save(location);

        assertThat(savedLocation.getBusiness_id()).isEqualTo(MockDataConstant.ID);
        assertThat(savedLocation.getLatitude()).isEqualTo(MockDataConstant.LATITUDE);
        assertThat(savedLocation.getLongitude()).isEqualTo(MockDataConstant.LONGITUDE);
    }
}
