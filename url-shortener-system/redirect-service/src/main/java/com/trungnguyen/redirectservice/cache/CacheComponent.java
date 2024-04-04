package com.trungnguyen.redirectservice.cache;

public interface CacheComponent<T> {

    T getKey(String key);

    boolean addCache(String key, T value);
}
