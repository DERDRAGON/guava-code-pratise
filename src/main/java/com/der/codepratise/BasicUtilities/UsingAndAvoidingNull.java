package com.der.codepratise.BasicUtilities;

import com.google.common.base.Optional;
import org.junit.Assert;

import java.util.Set;

/**
 * 使用和避免null
 * @author K0790016
 **/
public class UsingAndAvoidingNull {

    public static void main(String[] args) {
        Optional<Integer> optional = Optional.of(5);
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(optional.get().toString(), String.valueOf(5));

        Optional<String> absent = Optional.absent();
        Assert.assertFalse(absent.isPresent());

        Optional<String> nullable = Optional.fromNullable(null);
        Assert.assertEquals("666", nullable.or("666"));

        Assert.assertNull(nullable.orNull());

        Set<String> asSet =  nullable.asSet();

        Assert.assertEquals(String.valueOf(0), String.valueOf(asSet.size()));

        Assert.assertFalse(optional.asSet().isEmpty());

        Assert.assertEquals("4444", Optional.fromNullable(null).or("4444"));
    }
}
