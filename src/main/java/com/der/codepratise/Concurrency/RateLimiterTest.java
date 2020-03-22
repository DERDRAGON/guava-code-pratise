package com.der.codepratise.Concurrency;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RateLimiter:
 */
public class RateLimiterTest {

    private ExecutorService executor;

    @Before
    public void setUp() {
        executor = Executors.newCachedThreadPool();
    }

    @Test
    public void testRateLimiter() {
        RateLimiter limiter = RateLimiter.create(4.0);

        limiter.acquire();
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "Hello RateLimiter";
            }
        });

        if(limiter.tryAcquire()){
            //有资源访问的许可
            System.out.println("someThing");
        }else{
            //没有资源访问的许可
            System.out.println("anotherThing");
        }

    }

}
