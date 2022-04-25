package com.cardgameserver.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomTheadPoolExecutor {

    private static ThreadPoolExecutor pool=new ThreadPoolExecutor(
                4,
                        6,
                        30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2),
                new CustomThreadFactory(),
                new CustomRejectedExecutionHandler()

        );

    public CustomTheadPoolExecutor(){
    }

    public static ThreadPoolExecutor getPool(){
        return pool;
    }



    public static void destory(){
        if(pool!=null){
            pool.shutdownNow();
        }

    }

}
