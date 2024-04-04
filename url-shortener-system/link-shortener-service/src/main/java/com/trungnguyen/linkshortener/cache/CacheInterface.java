package com.trungnguyen.linkshortener.cache;

public interface CacheInterface<T> {

    T getKey(String key);

    boolean addCache(String key, T value);
}
