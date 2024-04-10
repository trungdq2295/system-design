package com.trungnguyen.locationlookupservice.services;

import com.trungnguyen.locationlookupservice.model.LocationResponse;
import com.trungnguyen.locationlookupservice.model.LookUpRequest;

import java.util.List;

public interface LocationLookupService {

    List<LocationResponse> lookUp(LookUpRequest request);
}
