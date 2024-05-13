package com.trungnguyen.synchornizedataservice.model.elasticsearch;


import com.trungnguyen.synchornizedataservice.enumeration.BusinessType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "location")
@Data
public class LocationIndex {

    @Id
    private String id;

    @Field(type = FieldType.Object)
    @GeoPointField
    private GeoPoint geoPoint;

    @Field(type = FieldType.Keyword)
    private BusinessType type;
}
