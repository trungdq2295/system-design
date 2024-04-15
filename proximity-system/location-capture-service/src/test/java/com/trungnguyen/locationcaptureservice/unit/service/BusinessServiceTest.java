package com.trungnguyen.locationcaptureservice.unit.service;


import com.trungnguyen.locationcaptureservice.constant.MockDataConstant;
import com.trungnguyen.locationcaptureservice.enumeration.BusinessType;
import com.trungnguyen.locationcaptureservice.model.BusinessRequest;
import com.trungnguyen.locationcaptureservice.model.cassandra.Location;
import com.trungnguyen.locationcaptureservice.model.mysql.Business;
import com.trungnguyen.locationcaptureservice.repository.cassandra.LocationRepository;
import com.trungnguyen.locationcaptureservice.repository.mysql.BusinessRepository;
import com.trungnguyen.locationcaptureservice.service.BusinessService;
import com.trungnguyen.locationcaptureservice.service.impl.BusinessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTest {


    @Mock
    private LocationRepository locationRepository;

    @Mock
    private BusinessRepository businessRepository;

    private BusinessService businessService;


    @BeforeEach
    public void setup() {
        businessService = new BusinessServiceImpl(locationRepository, businessRepository);
    }


    @Test
    public void givenWhen_UserInputNewLocation_shouldReturnTrueIfInsertSuccess() {
        BusinessRequest request = new BusinessRequest(
                "",
                "Trung Nguyen Test",
                "This is a test",
                BusinessType.CONVENIENCE_STORE,
                MockDataConstant.LONGITUDE,
                MockDataConstant.LATITUDE
        );

        when(locationRepository.save(any())).thenReturn(new Location());
        when(businessRepository.save(any())).thenReturn(new Business());

        Boolean result = businessService.insertBusiness(request);
        assertThat(result).isEqualTo(true);
    }

    
}
