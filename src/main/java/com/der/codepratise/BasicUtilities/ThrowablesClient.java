package com.der.codepratise.BasicUtilities;

import com.google.common.base.Throwables;

/**
 * 简化异常和错误的传播与检查
 * @author K0790016
 **/
public class ThrowablesClient {

    public static void main(String[] args) {

        try {
            throw new RuntimeException("sddf");
        } catch (Exception e) {
            Throwables.propagateIfPossible(e, RuntimeException.class);
        }
    }
}
