package com.der.codepratise.Concurrency;

import com.google.common.util.concurrent.*;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author K0790016
 **/
public class ListenableFutureTest {

    public static void main(String[] args) {
//        testListenableFuture();
        testTransFormAsync();
    }

    private static void testListenableFuture() {
        /** 1.典型用法：可监听的future,带回调方法 */
        // 定义监听执行服务
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        // 定义可监听的带返回值的任务
        ListenableFuture<String> callableListenableFuture1 = listeningExecutorService.submit(() -> {
            System.out.println("callable1 call!");
            return "1";
        });
        // 添加回调，由指定监听执行服务来执行，监听可监听的future,监听到事件时执行对应回调方法。
        Futures.addCallback(callableListenableFuture1, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String result) {
                System.out.println("success,result=" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("fail!");
            }
        }, MoreExecutors.directExecutor());
    }

    private static void testTransFormAsync() {
        /** 2.非典型用法：异步转换 */
        // 定义监听执行服务
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        // 定义可监听的带返回值的任务
        ListenableFuture<String> callableListenableFuture1 = listeningExecutorService.submit(() -> {
            System.out.println("callable1 call!");
            return "1";
        });
        // 异步转换，参数1是需要转换的listenableFuture,参数2是转换方法，参数3是执行转换的线程执行器（Runnable）。
        ListenableFuture<Integer> transform = Futures.transformAsync(callableListenableFuture1, new AsyncFunction<String, Integer>() {
            @Override
            public ListenableFuture<Integer> apply(@Nullable String input) {
                return Futures.immediateFuture(Integer.parseInt(input));
            }
        }, MoreExecutors.directExecutor());
        // 阻塞线程得到结果
        try {
            System.out.println("阻塞获取转换后任务的结果："+transform.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
