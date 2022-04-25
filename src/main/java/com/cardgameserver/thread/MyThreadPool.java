package com.cardgameserver.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool {

    public static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(
            2,
            4,
            12,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(12)

    );

    public static Executor getThreadPool(){
        return threadPoolExecutor;
    }
}
