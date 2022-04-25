package com.cardgameserver.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 拒绝策略
 * 八种拒绝模型
 * 参考了Dubbo和activeMQ的设计
 * 尽最大努力完成
 * 如果完不成 那就要抛出异常 同时记录当前的线程信息 记录堆栈信息
 * 在主线程中try catch 这个异常
 */
@Slf4j
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
        try {
            executor.getQueue().offer(r, 5, TimeUnit.SECONDS);


        } catch (InterruptedException e) {
            throw new RejectedExecutionException("Interrupted waiting for BrokerService.worker");
        }

        String msg = String.format("Thread pool is EXHAUSTED!" +
                        " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                        " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s), in %s://%s:%d!",
                Thread.currentThread().getName(), executor.getPoolSize(), executor.getActiveCount(), executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getLargestPoolSize(),
                executor.getTaskCount(), executor.getCompletedTaskCount(), executor.isShutdown(), executor.isTerminated(), executor.isTerminating());
        log.warn(msg);

        throw new RejectedExecutionException("Timed Out while attempting to enqueue Task.");
    }

}
