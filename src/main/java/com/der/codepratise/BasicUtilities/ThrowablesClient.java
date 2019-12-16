package com.der.codepratise.BasicUtilities;

import com.google.common.base.Throwables;

import java.io.InputStream;
import java.net.URL;

/**
 * 简化异常和错误的传播与检查
 * @author K0790016
 **/
public class ThrowablesClient {

    public static void main(String[] args) {

//        try {
//            throw new IOException("sddf");
//        } catch (Throwable e) {
//            System.out.println(Throwables.getStackTraceAsString(e));
//            Throwables.propagateIfPossible(e, RuntimeException.class);
//            Throwables.getCauseAs()
//        }

        //针对一些检查异常，在需要的时候将检查异常转化为非检查异常

        try {
            URL url = new URL("http://ociweb.com");
            final InputStream in = url.openStream();
            // read from the input stream
            in.close();
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }
    }
}
