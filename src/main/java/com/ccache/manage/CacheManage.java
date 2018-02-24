package com.ccache.manage;

import com.ccache.cache.Cache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存管理器
 * Created by gu on 18/2/11
 */
public class CacheManage {
    private static CacheManage INSTANCE = new CacheManage();
    private Map<String, Cache> cacheSpaceMap = new ConcurrentHashMap<>();

    private CacheManage(){}

    public static CacheManage getInstance(){
        return INSTANCE;
    }

    public Cache get(String namespace){
        return cacheSpaceMap.get(namespace);
    }

    public void put(String namespace, Cache cache){
        cacheSpaceMap.put(namespace, cache);
    }

    public void remove(String namespace){
        Cache cache = cacheSpaceMap.remove(namespace);
        cache.clear();
    }

    public void clear(){
        Iterator<Cache> iterator = cacheSpaceMap.values().iterator();
        while (iterator.hasNext()){
            iterator.next().clear();
            iterator.remove();
        }
    }

    public int size(){
        return cacheSpaceMap.size();
    }
}
