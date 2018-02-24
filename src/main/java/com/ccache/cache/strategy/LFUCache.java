package com.ccache.cache.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * 最不常使用
 *
 * Created by gu on 18/2/9
 */
public class LFUCache<K, V> extends AbstractCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LFUCache.class);

    public LFUCache(int cacheSize, long defaultExpire) throws Exception {
        super(cacheSize, defaultExpire);
        cacheMap = new LinkedHashMap<>(cacheSize);
    }

    @Override
    public void eliminateCache() {
        long minAccessCount = Long.MAX_VALUE;
        Iterator<Entry<K, V>> iterator = cacheMap.values().iterator();
        while(iterator.hasNext()){
            Entry<K, V> entry = iterator.next();
            if(entry.isExpired()){
                LOGGER.debug("LFU缓存过期, key:{}, value:{}", entry.key, entry.value);
                iterator.remove();
            }else {
                minAccessCount = Math.min(entry.accessCount, minAccessCount);
            }
        }

        //过期元素删除后返回
        if(!isFull()){
            return;
        }

        iterator = cacheMap.values().iterator();
        while (iterator.hasNext()){
            Entry<K, V> entry = iterator.next();
            if (entry.accessCount - minAccessCount <= 0){
                LOGGER.debug("LFU淘汰使用频率低的, key:{}, value:{}, accessCount:{}", entry.key, entry.value, entry.accessCount);
                iterator.remove();
            }
        }
    }
}
