package com.cardgameserver.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {

    private AtomicInteger count=new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        System.out.println("-----------来到线程池的工厂中----------------");
        System.out.println("线程工厂生产了第"+count+"个线程");

        Thread thread=new Thread(r);
        String threadName=CustomTheadPoolExecutor.class.getSimpleName()+count.addAndGet(1);
        thread.setName(threadName);

        return thread;
    }
}
