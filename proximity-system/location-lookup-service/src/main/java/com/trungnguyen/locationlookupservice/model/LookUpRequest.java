package com.trungnguyen.locationlookupservice.model;

import com.trungnguyen.locationlookupservice.enumeration.BusinessType;

public record LookUpRequest(
        BusinessType type,
        Double currentLongitude,

        Double currentLatitude
) {
}
