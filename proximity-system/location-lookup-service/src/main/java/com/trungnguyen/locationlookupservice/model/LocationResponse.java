package com.trungnguyen.locationlookupservice.model;

import com.trungnguyen.locationlookupservice.model.elasticsearch.Location;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public record LocationResponse(
        String id,

        GeoPoint location
) {


    public static LocationResponse convert(Location location){
        return new LocationResponse(location.getId(), location.getGeoPoint());
    }
}
