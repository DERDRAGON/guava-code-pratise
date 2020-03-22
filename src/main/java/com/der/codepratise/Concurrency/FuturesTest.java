package com.der.codepratise.Concurrency;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Futures：用于处理Future实例的工具类
 */
public class FuturesTest {

    private ExecutorService executor;
    private ListeningExecutorService executorService;
    private ListenableFuture<String> listenableFuture;
    private Person person;

    @Before
    public void setUp() {
        executor = Executors.newCachedThreadPool();
        executorService = MoreExecutors.listeningDecorator(executor);
        person = new Person();
    }

    @Test
    public void testFuturesTransform() throws ExecutionException, InterruptedException {
        listenableFuture = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "张三";
            }
        });
        AsyncFunction<String, Person> asyncFunction = new AsyncFunction<String, Person>() {
            @Override
            public ListenableFuture<Person> apply(String input) throws Exception {
                person.setName(input);
                return executorService.submit(new Callable<Person>() {
                    @Override
                    public Person call() throws Exception {
                        return person;
                    }
                });
            }
        };
        ListenableFuture<Person> lf =
                Futures.transform(listenableFuture, new Function<String, Person>() {
                    @Override
                    public Person apply(@Nullable String input) {
                        return new Person();
                    }
                }, MoreExecutors.directExecutor());
        // 异步转换，参数1是需要转换的listenableFuture,参数2是转换方法，参数3是执行转换的线程执行器（Runnable）。
        ListenableFuture<Integer> transform = Futures.transformAsync(listenableFuture, new AsyncFunction<String, Integer>() {
            @Override
            public ListenableFuture<Integer> apply(@javax.annotation.Nullable String input) {
                return Futures.immediateFuture(Integer.parseInt(input));
            }
        }, MoreExecutors.directExecutor());
        assertThat(lf.get().getName(), is("张三"));
    }

//    @Test
//    public void testFuturesFallback() throws ExecutionException, InterruptedException {
//        listenableFuture = executorService.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                throw new RuntimeException();
//            }
//        });
//        FutureFallback<String> futureFallback = new FutureFallback<String>() {
//            @Override
//            public ListenableFuture create(Throwable t) throws Exception {
//                if (t instanceof RuntimeException) {
//                    SettableFuture<String> settableFuture =
//                            SettableFuture.create();
//                    settableFuture.set("Not Found");
//                    return settableFuture;
//                }
//                throw new Exception(t);
//            }
//        };
//        ListenableFuture<String> lf =
//                Futures.withFallback(listenableFuture,futureFallback);
//        assertThat(lf.get(), is("Not Found"));
//    }

    class Person {
        int age;
        String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
