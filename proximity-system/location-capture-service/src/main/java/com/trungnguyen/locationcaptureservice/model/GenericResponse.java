package com.trungnguyen.locationcaptureservice.model;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenericResponse<T>(
        int status,

        String message,

        T data
) {
}
