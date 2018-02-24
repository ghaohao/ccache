package com.ccache.listen;

import com.ccache.cache.Cache;

/**
 * Created by hao.g on 18/2/23.
 */
public class CacheListener extends Thread{
    private static final long INTERVAL = 2000;
    private Cache cache;
    private boolean running;

    public CacheListener(Cache cache){
        this.cache = cache;
        running = true;
    }

    @Override
    public void run(){
        try {
            while (running){
                Thread.sleep(INTERVAL);
                cache.eliminate();
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        running = false;
    }
}
