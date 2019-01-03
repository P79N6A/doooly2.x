package com.doooly.business.freeCoupon.service.thread.impl;

import com.doooly.business.freeCoupon.service.thread.MyThreadPoolService;
import com.doooly.common.constants.ThreadPoolConstants;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池实现
 * @author: qing.zhang
 * @date: 2018-10-19
 */
@Service
public class MyThreadPoolServiceImpl implements MyThreadPoolService {

    private ThreadPoolExecutor executors;

    private void init() {
        if (executors == null) {
            executors = new ThreadPoolExecutor(ThreadPoolConstants.CORE_POOL_SIZE, ThreadPoolConstants.MAX_POOL_SIZE,
                    ThreadPoolConstants.KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(ThreadPoolConstants.WORK_QUEUE_SIZE),
                    Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }

    public ThreadPoolExecutor getExecutors() {
        return executors;
    }

    /**
     * 提交任务
     *
     * @param task
     */
    public Future submitTask(Callable task) {
        init();
        return executors.submit(task);
    }

    @Override
    public void submitRunalbeTask(Runnable task) {
        init();
        executors.submit(task);
    }
}
