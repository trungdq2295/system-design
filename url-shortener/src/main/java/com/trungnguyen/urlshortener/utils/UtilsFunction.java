package com.trungnguyen.urlshortener.utils;


import com.trungnguyen.urlshortener.constant.CommonConstant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UtilsFunction {

    public String buildKeyRedisCache(String url) {
        return CommonConstant.KEY_REDIS_LINK + ":" + url;
    }
}
