//package com.der.codepratise.Concurrency;
//
//import com.google.common.base.Preconditions;
//import com.google.common.util.concurrent.*;
//
//import java.util.concurrent.*;
//
///**
// * 不太懂回头再看
// * @author K0790016
// **/
//public class ListenableFutureClient {
//
//    private static final ThreadFactory ThreadFactory = new ThreadFactoryBuilder().setNameFormat("listenableFuture-%s").build();
//
//    //真正干活的线程池
//    private static final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
//            5,
//            5,
//            0,
//            TimeUnit.SECONDS,
//            new ArrayBlockingQueue<>(100),
//            ThreadFactory,
//            new ThreadPoolExecutor.DiscardPolicy());
//
//    //guava的接口ListeningExecutorService继承了jdk原生ExecutorService接口，重写了submit方法，修改返回值类型为ListenableFuture
//    private static final ListeningExecutorService listeningExecutor = MoreExecutors.listeningDecorator(poolExecutor);
//
//    //获得一个随着jvm关闭而关闭的线程池，通过Runtime.getRuntime().addShutdownHook(hook)实现
//    //修改ThreadFactory为创建守护线程，默认jvm关闭时最多等待120秒关闭线程池，重载方法可以设置时间
//    private static final ExecutorService newPoolExecutor = MoreExecutors.getExitingExecutorService(poolExecutor);
//
//    static {
//        //只增加关闭线程池的钩子，不改变ThreadFactory
//        MoreExecutors.addDelayedShutdownHook(poolExecutor, 120, TimeUnit.SECONDS);
//    }
//
//
//    public static void main(String[] args) {
//
//    }
//
//    private static void testListenableFuture() {
//        //像线程池提交任务，并得到ListenableFuture
//        ListenableFuture<String> listenableFuture = listeningExecutor.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return "";
//            }
//        });
//        //可以通过addListener对listenableFuture注册回调，但是通常使用Futures中的工具方法
//        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//
//        /**
//         * Futures.addCallback源码，其实就是包装了一层addListener，可以不加executor参数，使用上文说的DirectExecutor
//         * 需要说明的是不加Executor的情况，只适用于轻型的回调方法，如果回调方法很耗时占资源，会造成线程阻塞
//         * 因为DirectExecutor有可能在主线程中执行回调
//         */
//        public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
//            Preconditions.checkNotNull(callback);
//            Runnable callbackListener =
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            final V value;
//                            try {
//                                value = getDone(future);
//                            } catch (ExecutionException e) {
//                                callback.onFailure(e.getCause());
//                                return;
//                            } catch (RuntimeException e) {
//                                callback.onFailure(e);
//                                return;
//                            } catch (Error e) {
//                                callback.onFailure(e);
//                                return;
//                            }
//                            callback.onSuccess(value);
//                        }
//                    };
//            future.addListener(callbackListener, executor);
//        }
//    }
//
//}
