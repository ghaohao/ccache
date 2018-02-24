package com.ccache.cache.strategy;

import com.ccache.cache.Cache;
import com.ccache.listen.CacheListener;
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存接口
 *
 * Created by gu on 18/2/9
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {
    private CacheListener listener;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();
    protected Map<K, Entry<K, V>> cacheMap;

    /**
     * 缓存初始大小
     */
    protected int cacheSize;

    /**
     * 默认过期时间,0表示永不过期
     */
    protected long defaultExpire;

    public AbstractCache(int cacheSize, long defaultExpire) throws Exception {
        if(cacheSize <= 0){
            throw new Exception("invalid cache size");
        }

        this.cacheSize = cacheSize;
        this.defaultExpire = defaultExpire;
        listener = new CacheListener(this);
        listener.start();
    }

    @Override
    public V get(K key) {
        try{
            readLock.lock();
            Entry<K, V> entry = cacheMap.get(key);
            if(entry == null){
                return null;
            }

            if (entry.isExpired()){
                cacheMap.remove(key);
                return null;
            }
            return entry.getObject();
        }finally {
            readLock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        put(key, value, defaultExpire);
    }

    @Override
    public void put(K key, V value, long expire) {
        try {
            writeLock.lock();
            Entry entry = new Entry(key, value, expire);

            //缓存淘汰
            if(isFull()){
                eliminate();
            }

            cacheMap.put(key, entry);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V remove(K key) {
        try{
            writeLock.lock();
            return (V) cacheMap.remove(key);
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean isFull() {
        return cacheSize == cacheMap.size();
    }

    @Override
    public void clear() {
        try{
            writeLock.lock();
            cacheMap.clear();
            listener.shutdown();
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public void eliminate(){
        try {
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + "缓存淘汰");
            if (MapUtils.isEmpty(cacheMap)){
                return;
            }
            eliminateCache();
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public int size() {
        return cacheMap.size();
    }

    public abstract void eliminateCache();

    class Entry<K, V> {
        K key;
        V value;

        /** 上次访问时间 */
        long lastAccessTime;

        /** 访问次数 */
        long accessCount;

        /** 存活时间 */
        long ttl;

        Entry(K key, V value, long ttl) {
            this.key = key;
            this.value = value;
            this.lastAccessTime = System.currentTimeMillis();
            this.ttl = ttl;
        }

        boolean isExpired(){
            if (ttl == 0){
                return false;
            }
            return lastAccessTime + ttl < System.currentTimeMillis();
        }

        V getObject(){
            lastAccessTime = System.currentTimeMillis();
            accessCount++;
            return value;
        }
    }
}
