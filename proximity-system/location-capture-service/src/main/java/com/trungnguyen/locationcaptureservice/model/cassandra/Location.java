package com.trungnguyen.locationcaptureservice.model.cassandra;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("location_capture")
public class Location {

    @PrimaryKeyColumn(
            name = "business_id",
            type = PrimaryKeyType.PARTITIONED
    )
    @Id
    private String business_id;

    @Column
    private double latitude;

    @Column(value = "longitude2")
    private double longitude;

    @Column(value = "timestamp")
    private Long timestamp;
}
