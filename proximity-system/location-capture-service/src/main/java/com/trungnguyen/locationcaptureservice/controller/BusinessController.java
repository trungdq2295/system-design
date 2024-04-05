package com.trungnguyen.locationcaptureservice.controller;


import com.trungnguyen.locationcaptureservice.constant.CommonConstant;
import com.trungnguyen.locationcaptureservice.model.BusinessRequest;
import com.trungnguyen.locationcaptureservice.model.GenericResponse;
import com.trungnguyen.locationcaptureservice.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CommonConstant.VERSION_V1 + CommonConstant.URL_BUSINESS)
public class BusinessController {

    private final BusinessService businessService;


    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public GenericResponse insertBusiness(@RequestBody BusinessRequest request) {
        businessService.insertBusiness(request);
        return new GenericResponse(HttpStatus.OK.value(), "", null);
    }

}
