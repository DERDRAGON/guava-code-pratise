package com.der.codepratise.Concurrency;

import com.google.common.util.concurrent.SettableFuture;
import org.junit.Test;

/**
 * SettableFuture：可以为Future设置返回值
 * User: Realfighter
 * Date: 2014/11/9
 * Time: 20:23
 */
public class SettableFutureTest {

    @Test
    public void testSettableFuture(){
        SettableFuture<String> sf = SettableFuture.create();
        //设置成功后返回指定的信息
        sf.set("SUCCESS");
        //设置失败后返回特定的异常信息
        sf.setException(new RuntimeException("Fails"));
    }

}
