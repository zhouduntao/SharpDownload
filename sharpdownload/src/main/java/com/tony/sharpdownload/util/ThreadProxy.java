package com.tony.sharpdownload.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tony
 * @version 1.0
 *          <p><strong>Features draft description.主要功能介绍<></p>
 * @since 2017/4/28 16:24
 */
public class ThreadProxy {

    private final ThreadPoolExecutor mExecutor;

    private ThreadProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        mExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }


    public void run(Runnable r) {
        mExecutor.execute(r);
    }

    public Future<?> submit(Runnable r) {
        return mExecutor.submit(r);
    }

    public void cancel(Runnable r) {
        mExecutor.remove(r);
    }

    public static class Builder {

        public long keepAliveTime = 6000;
        public TimeUnit unit = TimeUnit.MILLISECONDS;
        public BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
        public int corePoolSize = Runtime.getRuntime().availableProcessors();
        public int maximumPoolSize = Runtime.getRuntime().availableProcessors();

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }

        public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
            this.workQueue = workQueue;
        }

        public ThreadProxy build() {
            return new ThreadProxy(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }
    }
}
