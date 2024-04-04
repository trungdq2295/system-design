package com.trungnguyen.redirectservice.utils;


import com.trungnguyen.redirectservice.constant.CommonConstant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UtilsFunction {

    public String buildKeyRedisCache(String url) {
        return CommonConstant.KEY_REDIS_LINK + ":" + url;
    }
}
