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
@RequestMapping(CommonConstant.VERSION_V1 + CommonConstant.BUSINESS_ENDPOINT)
public class BusinessController {

    private final BusinessService businessService;


    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public GenericResponse insertBusiness(@RequestBody BusinessRequest request) {
        boolean result = businessService.insertBusiness(request);
        if (result) {
            return new GenericResponse(HttpStatus.OK.value(), "", null);
        } else {
            return new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        }

    }

}
