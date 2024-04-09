package com.trungnguyen.locationcaptureservice.constant;

import com.trungnguyen.locationcaptureservice.enumeration.BusinessType;

import java.util.UUID;

public class MockDataConstant {

    public static final String ID = UUID.randomUUID().toString();

    public static final double LONGITUDE = 6.02;

    public static final double LATITUDE = 13.08;

    public static final long TIMESTAMP = System.currentTimeMillis();

    public static final String NAME = "Trung Nguyen";

    public static final String DESCRIPTION = "This is a Description test";

    public static final BusinessType BUSINESS_TYPE = BusinessType.CONVENIENCE_STORE;


}
