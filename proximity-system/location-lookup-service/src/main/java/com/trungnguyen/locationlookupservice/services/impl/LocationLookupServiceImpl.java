package com.trungnguyen.locationlookupservice.services.impl;

import com.trungnguyen.locationlookupservice.model.LocationResponse;
import com.trungnguyen.locationlookupservice.model.LookUpRequest;
import com.trungnguyen.locationlookupservice.repository.LocationRepository;
import com.trungnguyen.locationlookupservice.services.LocationLookupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationLookupServiceImpl implements LocationLookupService {

    LocationRepository locationRepository;

    public LocationLookupServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationResponse> lookUp(LookUpRequest request) {
        List<LocationResponse> list = locationRepository.findByTypeAndLocation(request.type().name(), request.currentLatitude(), request.currentLongitude())
                .stream().map(LocationResponse::convert).collect(Collectors.toList());
        return list;
    }
}
