package com.trungnguyen.locationlookupservice.model.elasticsearch;


import com.trungnguyen.locationlookupservice.enumeration.BusinessType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "location")
@Data
public class Location {

    @Id
    private String id;

    @Field(type = FieldType.Object)
    @GeoPointField
    private GeoPoint geoPoint;

    @Field(type = FieldType.Text)
    private BusinessType type;
}
