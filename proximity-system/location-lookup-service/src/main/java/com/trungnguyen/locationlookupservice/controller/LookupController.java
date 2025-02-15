package com.trungnguyen.locationlookupservice.controller;

import com.trungnguyen.locationlookupservice.constant.CommonConstant;
import com.trungnguyen.locationlookupservice.model.GenericResponse;
import com.trungnguyen.locationlookupservice.model.LookUpRequest;
import com.trungnguyen.locationlookupservice.services.LocationLookupService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(CommonConstant.VERSION_V1 + CommonConstant.LOOKUP_ENDPOINT)
public class LookupController {

    private final LocationLookupService locationLookupService;

    public LookupController(LocationLookupService locationLookupService) {
        this.locationLookupService = locationLookupService;
    }

    @GetMapping
    public GenericResponse lookUp(LookUpRequest request) {
        if (Objects.isNull(request.type())) {
            return new GenericResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid type", null);
        }
        return new GenericResponse<>(200, "", locationLookupService.lookUp(request));
    }
}
