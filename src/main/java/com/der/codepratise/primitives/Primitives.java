package com.der.codepratise.primitives;

import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.SignedBytes;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author K0790016
 **/
public class Primitives {

    private static final Joiner joiner = Joiner.on(",");

    public static void main(String[] args) {
        testBytes();
        testSignedBytes();
    }

    private static void testSignedBytes() {
        byte[] byteArray = {1,2,3,4,5,5,7,9,9};
        byte[] byteArray2 = {0, 6, 8, 10};

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
        byte[] byteArray = {1,2,3,4,5,5,7,9,9};
        byte[] byteArray2 = {0, 6, 8, 10};

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
