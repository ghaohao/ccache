package com.ccache.cache.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 先进先出
 *
 * Created by gu on 18/2/9
 */
public class FIFOCache<K, V> extends AbstractCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FIFOCache.class);

    public FIFOCache(int cacheSize, long defaultExpire) throws Exception {
        super(cacheSize, defaultExpire);
        cacheMap = new LinkedHashMap<K, Entry<K, V>>(cacheSize,1,false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, Entry<K, V>> eldest) {
                return FIFOCache.this.size() > cacheSize;
            }
        };
    }

    @Override
    public void eliminateCache() {
        Iterator<Entry<K, V>> iterator = cacheMap.values().iterator();
        while(iterator.hasNext()){
            Entry<K, V> entry = iterator.next();
            if(entry.isExpired()){
                LOGGER.debug("FIFO缓存过期, key:{}, value:{}", entry.key, entry.value);
                iterator.remove();
            }
        }
    }
}
