package com.trungnguyen.urlshortener.enumeration;


import lombok.Data;

public enum StatusCode {

    OK(200, "OK"),

    BAD_REQUEST(401, "BAD_REQUEST");


    private final int statusCode;

    private final String statusMessage;

    StatusCode(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
