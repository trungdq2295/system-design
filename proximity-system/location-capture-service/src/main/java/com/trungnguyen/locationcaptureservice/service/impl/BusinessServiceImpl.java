package com.trungnguyen.locationcaptureservice.service.impl;


import com.trungnguyen.locationcaptureservice.model.BusinessRequest;
import com.trungnguyen.locationcaptureservice.model.cassandra.Location;
import com.trungnguyen.locationcaptureservice.model.mysql.Business;
import com.trungnguyen.locationcaptureservice.repository.cassandra.LocationRepository;
import com.trungnguyen.locationcaptureservice.repository.mysql.BusinessRepository;
import com.trungnguyen.locationcaptureservice.service.BusinessService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final LocationRepository locationRepository;

    private final BusinessRepository businessRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(32);

    public BusinessServiceImpl(LocationRepository locationRepository, BusinessRepository businessRepository) {
        this.locationRepository = locationRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    public void insertBusiness(BusinessRequest request) {
        try{
            String id = UUID.randomUUID().toString();
            CompletableFuture.allOf(insertToCassandra(request, id), insertToMySQL(request, id));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private CompletableFuture<Void> insertToCassandra(BusinessRequest request, String id) {
        return CompletableFuture.runAsync(() -> {
            Location location = new Location();
            location.setId(id);
            location.setLongtitude(request.longtitue());
            location.setLongtitude(request.latitue());
            location.setTimestamp(System.currentTimeMillis());
            locationRepository.save(location);
        }, executorService);
    }

    private CompletableFuture<Void> insertToMySQL(BusinessRequest request, String id) {
        return CompletableFuture.runAsync(() -> {
            Business business = new Business();
            business.setId(id);
            business.setName(request.name());
            business.setDescription(request.description());
            business.setBusinessType(request.businessType().toString());
            businessRepository.save(business);
        }, executorService);
    }
}
