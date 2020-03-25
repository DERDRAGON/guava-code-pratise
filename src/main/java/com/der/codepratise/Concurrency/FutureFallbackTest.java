//package com.der.codepratise.Concurrency;
//
//import com.google.common.util.concurrent.FutureFallback;
//import com.google.common.util.concurrent.ListenableFuture;
//import com.google.common.util.concurrent.SettableFuture;
//
//import java.io.FileNotFoundException;
//
///**
// * FutureFallback: 在Future实例失败后常被用来作为Future的备份或者默认的值
// */
//public class FutureFallbackTest {
//
//    class FutureFallbackImpl implements FutureFallback<String> {
//        @Override
//        public ListenableFuture<String> create(Throwable t) throws
//                Exception {
//            if (t instanceof FileNotFoundException) {
//                SettableFuture<String> settableFuture =
//                        SettableFuture.create();
//                settableFuture.set("Not Found");
//                return settableFuture;
//            }
//            throw new Exception(t);
//        }
//    }
//
//}
