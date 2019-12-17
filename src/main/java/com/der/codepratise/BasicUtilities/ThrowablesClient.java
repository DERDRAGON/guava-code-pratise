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
        try {
            URL url = new URL("http://www.dianping.com");
            InputStream in = url.openStream();
            in.close();
        } catch (Exception e) {
            Throwables.propagateIfPossible(e);
        }
    }
}
