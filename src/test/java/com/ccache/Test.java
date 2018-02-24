package com.ccache;

import com.ccache.cache.Cache;
import com.ccache.cache.strategy.FIFOCache;
import com.ccache.cache.strategy.LFUCache;
import com.ccache.cache.strategy.LRUCache;
import com.ccache.manage.CacheManage;

/**
 * Created by gu on 18/2/8
 *
 */
public class Test {
    public static void main(String[] args) throws Exception {
        CacheManage cacheManage = CacheManage.getInstance();
//        Cache lfuCache = new LFUCache(1000, 5000);
//        cacheManage.put("LFU", lfuCache);

        Cache fifoCache = new FIFOCache(10000, 1000);
        cacheManage.put("FIFO", fifoCache);

        for(int i = 0;i< 100000;i++){
            fifoCache.put(i, i);
        }


        Cache lruCache = new LRUCache(3, 5000);
        cacheManage.put("LRU", lruCache);
        lruCache.put(1, 1);
        lruCache.put(2, 1);
        lruCache.put(3, 1);
        lruCache.put(4, 1);
        lruCache.put(5, 1);
        lruCache.put(6, 1);

        cacheManage.clear();
        System.out.println(cacheManage.size());
    }
}
