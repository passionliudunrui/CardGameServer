package com.cardgameserver.thread;

import org.springframework.ui.context.Theme;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class Test {


    public static void main(String[] args) throws InterruptedException {

        Executor executor=MyThreadPool.getThreadPool();
        CompletableFuture<Double>cf=CompletableFuture.supplyAsync(()->{
            System.out.println("hello world");
            System.out.println("先睡会");
            try {
                System.out.println(Thread.currentThread().getName()+"先睡会");
                Thread.sleep(8000);
                System.out.println("醒了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1.2;

        },executor);

        System.out.println("主线程继续执行");


        CompletableFuture<String>cf2=cf.thenApplyAsync((result)->{
            System.out.println("第二个线程查看");
            System.out.println(Thread.currentThread().getName()+"也睡会");
            System.out.println(result);
            System.out.println("行");

            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "123";

        });


        System.out.println("主线程接着执行");


        Thread.sleep(1000000);





    }
}
