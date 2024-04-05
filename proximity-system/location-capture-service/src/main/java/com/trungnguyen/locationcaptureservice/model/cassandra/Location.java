package com.trungnguyen.locationcaptureservice.model.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("location_capture")
@Data
public class Location {

    @PrimaryKeyColumn(
            name = "business_id",
            type = PrimaryKeyType.PARTITIONED
    )
    private String id;

    @Column
    private double lat;

    @Column(value = "long")
    private double longtitude;

    @Column(value="timestamp")
    private Long timestamp;
}
