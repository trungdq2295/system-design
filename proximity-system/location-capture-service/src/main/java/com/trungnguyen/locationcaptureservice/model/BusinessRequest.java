package com.trungnguyen.locationcaptureservice.model;

import com.trungnguyen.locationcaptureservice.enumeration.BusinessType;

public record  BusinessRequest(
        String businessId,
        String name,
        String description,

        BusinessType businessType,

        double longitude,

        double latitude
) {
}
