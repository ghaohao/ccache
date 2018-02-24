package com.ccache.cache.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 最近最少使用
 *
 * Created by gu on 18/2/9
 */
public class LRUCache<K, V> extends AbstractCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LRUCache.class);

    public LRUCache(int cacheSize, long defaultExpire) throws Exception {
        super(cacheSize, defaultExpire);

        //重写linkedHashMap的removeEldestEntry方法
        cacheMap = new LinkedHashMap<K, Entry<K, V>>(cacheSize,1,true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, Entry<K, V>> eldest) {
                return LRUCache.this.size() > cacheSize;
            }
        };
    }

    @Override
    public void eliminateCache() {
        Iterator<Entry<K, V>> iterator = cacheMap.values().iterator();
        while(iterator.hasNext()){
            Entry<K, V> entry = iterator.next();
            if(entry.isExpired()){
                LOGGER.debug("LRU缓存过期, key:{}, value:{}", entry.key, entry.value);
                iterator.remove();
            }
        }
    }
}
