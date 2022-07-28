package com.wen.commutil.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建公用线程池 工具类
 * 懒汉式 单利模式
 * 公用线程池不允许被关闭
 *
 * @author calwen
 */
public class ThreadPoolUtil {
    private static volatile ThreadPoolExecutor threadPool = null;


    private ThreadPoolUtil() {
    }

    /**
     * 双重加锁
     * 发现线程池被关闭再次创建
     *
     * @return 公用线程池
     */
    public static ThreadPoolExecutor getThreadPool() {
        if (threadPool == null || threadPool.isShutdown()) {
            synchronized (ThreadPoolUtil.class) {
                if (threadPool == null || threadPool.isShutdown()) {
                    threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                            Runtime.getRuntime().availableProcessors() * 2,
                            3,
                            TimeUnit.SECONDS,
                            new LinkedBlockingDeque<>(3),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.DiscardOldestPolicy());
                }
            }
        }
        return threadPool;
    }
}
