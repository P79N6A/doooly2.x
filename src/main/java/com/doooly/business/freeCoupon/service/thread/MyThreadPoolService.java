package com.doooly.business.freeCoupon.service.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-10-19
 */
public interface MyThreadPoolService {

    Future submitTask(Callable callable);

    void submitRunalbeTask(Runnable callable);
}
