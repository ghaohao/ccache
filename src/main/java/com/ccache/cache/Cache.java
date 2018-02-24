package com.ccache.cache;

/**
 *
 * @param <K>
 * @param <V>
 */
public interface Cache<K, V> {
    /**
     * 获取元素
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 添加元素
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * 添加元素
     * @param key
     * @param value
     * @param expire
     */
    void put(K key, V value, long expire);

    /**
     * 移除缓存
     * @param key
     * @return
     */
    V remove(K key);

    /**
     * 缓存是否满了
     * @return
     */
    boolean isFull();

    /**
     * 清除所有元素
     */
    void clear();

    /**
     * 缓存大小
     * @return
     */
    int size();

    /**
     * 缓存淘汰
     */
    void eliminate();
}
