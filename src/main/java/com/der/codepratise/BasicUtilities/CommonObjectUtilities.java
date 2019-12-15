package com.der.codepratise.BasicUtilities;

import com.der.codepratise.entity.Book;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import org.junit.Assert;

/**
 * @author K0790016
 **/
public class CommonObjectUtilities {

    private static final ComparisonChain comparisonChain = ComparisonChain.start().compare("a", "b").compare('1', '3').compare(true, false);

    public static void main(String[] args) {
        Assert.assertTrue(Objects.equal("a", "a"));

        System.out.println(Objects.hashCode("sdfsfsdf"));

        //最新版本Objects无ToStringHelper方法

        System.out.println(MoreObjects.toStringHelper(Book.class).add("id", 333).add("name", "guava学习").toString());

        // 比较链 -- 按比较顺序，前一个非0的话就返回，否则就继续比较，
        // 可用于一个类两个对象的多属性比较
        System.out.println(comparisonChain.result());
    }
}
