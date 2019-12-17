package com.der.codepratise.BasicUtilities;

import com.google.common.base.Defaults;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2019-12-16 11:03
 */
public class DefaultsClient {

    public static void main(String[] args) {
        assertEquals(false, Defaults.defaultValue(boolean.class).booleanValue());
        assertEquals('\0', Defaults.defaultValue(char.class).charValue());
        assertEquals(0, Defaults.defaultValue(byte.class).byteValue());
        assertEquals(0, Defaults.defaultValue(short.class).shortValue());
        assertEquals(0, Defaults.defaultValue(int.class).intValue());
        assertEquals(0, Defaults.defaultValue(long.class).longValue());
        assertEquals((Float) 0.0f, (Float) Defaults.defaultValue(float.class).floatValue());
        assertEquals((Double) 0.0d, (Double) Defaults.defaultValue(double.class).doubleValue());
        assertNull(Defaults.defaultValue(void.class));
        assertNull(Defaults.defaultValue(String.class));
    }
}
