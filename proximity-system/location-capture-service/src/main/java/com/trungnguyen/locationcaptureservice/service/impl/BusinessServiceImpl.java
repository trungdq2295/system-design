package com.trungnguyen.locationcaptureservice.service.impl;


import com.trungnguyen.locationcaptureservice.model.BusinessRequest;
import com.trungnguyen.locationcaptureservice.model.cassandra.Location;
import com.trungnguyen.locationcaptureservice.model.mysql.Business;
import com.trungnguyen.locationcaptureservice.repository.cassandra.LocationRepository;
import com.trungnguyen.locationcaptureservice.repository.mysql.BusinessRepository;
import com.trungnguyen.locationcaptureservice.service.BusinessService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class BusinessServiceImpl implements BusinessService {

    private final LocationRepository locationRepository;

    private final BusinessRepository businessRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(32);


    public BusinessServiceImpl(LocationRepository locationRepository, BusinessRepository businessRepository) {
        this.locationRepository = locationRepository;
        this.businessRepository = businessRepository;
    }

    @Override
    @Transactional
    public boolean insertBusiness(BusinessRequest request) {
        String id = UUID.randomUUID().toString();
        try {
            CompletableFuture.allOf(insertToCassandra(request, id), insertToMySQL(request, id)).get();
            return true;
        } catch (Exception e) {
            CompletableFuture.runAsync(() -> handleRollbackData(id));
            return false;
        }
    }


    private CompletableFuture<Void> insertToCassandra(BusinessRequest request, String id) {
        return CompletableFuture.runAsync(() -> {
            try {
                Location location = new Location();
                location.setBusiness_id(id);
                location.setLatitude(request.latitude());
                location.setLongitude(request.longitude());
                location.setTimestamp(System.currentTimeMillis());
                locationRepository.save(location);
            } catch (Exception e) {
                log.error("Exception happened in insert data to Cassandra", e);
                throw e;
            }
        }, executorService);
    }

    private CompletableFuture<Void> insertToMySQL(BusinessRequest request, String id) {
        return CompletableFuture.runAsync(() -> {
            try {
                Business business = new Business();
                business.setId(id);
                business.setName(request.name());
                business.setDescription(request.description());
                business.setBusinessType(request.businessType().toString());
                businessRepository.save(business);
            } catch (Exception e) {
                log.error("Exception happened in insert data to Cassandra", e);
                throw e;
            }
        }, executorService);
    }

    private void handleRollbackData(String id) {
        try {
            /*
                Sleep for 1s to wait for other data inserted to DB
                It's just an ideal time
             */
            Thread.sleep(1000);
            locationRepository.deleteById(id);
            businessRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
