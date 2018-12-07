package com.doooly.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.reach.redis.interceptor.CacheInterceptor;

public class SpringInterceptor extends CacheInterceptor implements MethodInterceptor  {
 
 
    @Override
    public Object invoke(MethodInvocation invo) throws Throwable {
        return super.intercept(invo);
    }
}