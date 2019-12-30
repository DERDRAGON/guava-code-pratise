package com.der.codepratise.primitives;

import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author K0790016
 **/
public class Primitives {

    private static final Joiner joiner = Joiner.on(",");

    private static final byte[] byteArray = {1,2,3,4,5,5,7,9,9};
    private static final byte[] byteArray2 = {0, 6, 8, 10};

    public static void main(String[] args) {
        testBytes();
        testSignedBytes();
        testUnsignedBytes();
    }

    private static void testUnsignedBytes() {

        byte[] byteArray = Bytes.toArray(Bytes.asList(Primitives.byteArray));
        byte[] byteArray2 = Bytes.toArray(Bytes.asList(Primitives.byteArray2));

        //适合无符号字节的最大值。
//        UnsignedBytes.MAX_VALUE
        //可以表示为无符号字节的2的最大幂。
//        UnsignedBytes.MAX_POWER_OF_TWO

        //返回字节值，当它被视为无符号时，它等于值（如果可能）。
        byte checkedCast = UnsignedBytes.checkedCast(23L);
        assertTrue(Integer.valueOf(23).byteValue() == checkedCast);

        //比较两个指定的字节值，将它们视为0到255之间（含0和255）的无符号值。
        assertTrue(Integer.valueOf(7).equals(UnsignedBytes.compare(byteArray2[3], byteArray[2])));
        // 7 - (256 - 3) = -246
        assertTrue(Integer.valueOf(-246).equals(UnsignedBytes.compare(byteArray[6], (byte) (-3))));

        //返回由带给定基数的字符串表示的无符号字节值。
        /**
         * radix 进制数(2<=radix<=36)
         */
        //string 类型的 in:0 - 127 out:0 - 127 and in:128 -- 255 out:-128 - -1 的byte;不可输入负值
        //返回由给定的十进制字符串表示的无符号字节值。
        assertTrue(Integer.valueOf(127).byteValue() == UnsignedBytes.parseUnsignedByte("127"));
        assertTrue(Integer.valueOf(-128).byteValue() == UnsignedBytes.parseUnsignedByte("128"));
        assertTrue(Integer.valueOf(-1).byteValue() == UnsignedBytes.parseUnsignedByte("255"));
        assertEquals("11111111", Integer.toBinaryString(255));
        assertTrue(Integer.valueOf(-1).byteValue() == UnsignedBytes.parseUnsignedByte("11111111", 2));

        //返回当被视为无符号时，其值与value最接近的字节值。
        //in:0 - 127 out:0 - 127 and in:128 -- 255 out:-128 - -1 and in -∞ - -1 out:0 and in:256 - +∞ out:-1
        assertTrue(Integer.valueOf(0).byteValue() == UnsignedBytes.saturatedCast(-44));
        assertTrue(Integer.valueOf(-1).byteValue() == UnsignedBytes.saturatedCast(300));

        UnsignedBytes.sortDescending(byteArray, 3, 7);
        String join = UnsignedBytes.join("%", byteArray);
        assertEquals("1%2%3%7%5%5%4%9%9", join);

        UnsignedBytes.sortDescending(byteArray);
        join = UnsignedBytes.join("%", byteArray);
        assertEquals("9%9%7%5%5%4%3%2%1", join);

        assertEquals("22", UnsignedBytes.toString(byteArray2[3], 4));
    }

    private static void testSignedBytes() {

        byte[] byteArray = Bytes.toArray(Bytes.asList(Primitives.byteArray));
        byte[] byteArray2 = Bytes.toArray(Bytes.asList(Primitives.byteArray2));

        //如果可能，返回等于value的字节值。(不超过byte范围)
        SignedBytes.checkedCast(23L);

        assertTrue(Integer.valueOf(-1).equals(SignedBytes.compare((byte)3, (byte)4)));

        String join = SignedBytes.join("%", byteArray);
        assertEquals("1%2%3%4%5%5%7%9%9", join);

        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();
        assertTrue(Integer.valueOf(1).equals(comparator.compare(byteArray, byteArray2)));

        byte max = SignedBytes.max(byteArray);
        assertTrue(Integer.valueOf(9).byteValue() == max);
        byte min = SignedBytes.min(byteArray);
        assertTrue(Integer.valueOf(1).byteValue() == min);

        SignedBytes.sortDescending(byteArray, 2, 5);
        join = SignedBytes.join("%", byteArray);
        assertEquals("1%2%5%4%3%5%7%9%9", join);

        SignedBytes.sortDescending(byteArray);
        join = SignedBytes.join("%", byteArray);
        assertEquals("9%9%7%5%5%4%3%2%1", join);
    }

    private static void testBytes() {
        byte[] byteArray = Bytes.toArray(Bytes.asList(Primitives.byteArray));
        byte[] byteArray2 = Bytes.toArray(Bytes.asList(Primitives.byteArray2));

        //convert array of primitives to array of objects
        List<Byte> objectArray = Bytes.asList(byteArray);
        assertEquals("[1, 2, 3, 4, 5, 5, 7, 9, 9]", objectArray.toString());

        //convert array of objects to array of primitives
        byteArray = Bytes.toArray(objectArray);
        StringBuilder append = joiner.appendTo(new StringBuilder(), objectArray);
        assertEquals("1,2,3,4,5,5,7,9,9", append.toString());
        byte data = 5;
        //check if element is present in the list of primitives or not
        assertTrue(Bytes.contains(byteArray, data));

        //Returns the index
        assertTrue(Integer.valueOf(4).equals(Bytes.indexOf(byteArray,data)));

        //Returns the last index maximum
        assertTrue(Integer.valueOf(5).equals(Bytes.lastIndexOf(byteArray,data)));

        byte[] concat = Bytes.concat(byteArray, byteArray2);
        assertTrue(Integer.valueOf(13).equals(Bytes.asList(concat).size()));

        //新建一个byte[] 如果原有数组长度不小于最小长度，使用原数组；否则copy一个最小长度+增长长度的数组
        byte[] ensureCapacity = Bytes.ensureCapacity(byteArray2, 10, 1);
        StringBuilder sb = joiner.appendTo(new StringBuilder(), Bytes.asList(ensureCapacity));
        assertTrue(Integer.valueOf(11).equals(ensureCapacity.length));
        assertEquals("0,6,8,10,0,0,0,0,0,0,0", sb.toString());

        assertTrue(Integer.valueOf(7).equals(Bytes.indexOf(byteArray, (byte)9)));
        byte[] bytes = {7,9};
        assertTrue(Integer.valueOf(6).equals(Bytes.indexOf(byteArray, bytes)));

        Bytes.reverse(byteArray);
        assertEquals("[9, 9, 7, 5, 5, 4, 3, 2, 1]", Bytes.asList(byteArray).toString());
    }
}
