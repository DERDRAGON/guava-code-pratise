package com.der.codepratise.primitives;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.primitives.*;

import java.math.BigInteger;
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

    private static final short[] shortArray = {1,2,3,4,5,5,7,9,9};
    private static final short[] shortArray2 = {0, 6, 8, 10};

    private static final int[] intArray = {1,2,3,4,5,5,7,9,9};
    private static final int[] intArray2 = {0, 6, 8, 10};

    public static void main(String[] args) {
        testBytes();
        testSignedBytes();
        testUnsignedBytes();
        testShorts();
        testInts();
        testUnsignedInteger();
        testUnsignedInts();
    }

    private static void testUnsignedInts() {
        int checkedCast = UnsignedInts.checkedCast(77L);
        assertTrue(checkedCast == 77);

        assertTrue(-1 == UnsignedInts.compare(4, 6));

        assertTrue(UnsignedInts.decode("4") == 4);
    }

    private static void testUnsignedInteger() {
        assertEquals("4294967295", String.valueOf(UnsignedInteger.MAX_VALUE));
        assertTrue(UnsignedInteger.ZERO.intValue() == 0);
        assertTrue(UnsignedInteger.ONE.intValue() == 1);

        UnsignedInteger unsignedInteger = UnsignedInteger.valueOf("7777");
        assertTrue(unsignedInteger.intValue() == 7777);
        BigInteger bigInteger = unsignedInteger.bigIntegerValue();
        assertTrue(bigInteger.equals(BigInteger.valueOf(7777L)));

        assertTrue(UnsignedInteger.valueOf(bigInteger).equals(unsignedInteger));

        //相除
        UnsignedInteger dividedBy = unsignedInteger.dividedBy(UnsignedInteger.valueOf(7L));
        assertTrue(UnsignedInteger.valueOf(1111L).equals(dividedBy));

        UnsignedInteger fromIntBits = UnsignedInteger.fromIntBits(7777);
        assertTrue(unsignedInteger.equals(fromIntBits));

        //求余
        UnsignedInteger mod = fromIntBits.mod(UnsignedInteger.fromIntBits(10));
        assertTrue(mod.intValue() == 7);

        //相减
        UnsignedInteger minus = fromIntBits.minus(UnsignedInteger.valueOf(7000));
        assertTrue(minus.intValue() == 777);

        //相乘
        UnsignedInteger plus = fromIntBits.plus(UnsignedInteger.valueOf(223));
        assertTrue(plus.intValue() == 8000);

        assertEquals("222102", fromIntBits.toString(5));
    }

    private static void testInts() {
        assertTrue(Integer.valueOf(4).equals(Ints.BYTES));
        assertTrue(Integer.valueOf(1073741824).equals(Ints.MAX_POWER_OF_TWO));

        int[] ints = Ints.toArray(Ints.asList(intArray));
        int[] ints2 = Ints.toArray(Ints.asList(intArray2));

        assertTrue(3345 == Ints.checkedCast(3345L));

        assertTrue(-1 == Ints.compare(4, 5));

        int[] concat = Ints.concat(ints, ints2);
        String join = Ints.join("$", concat);
        assertEquals("1$2$3$4$5$5$7$9$9$0$6$8$10", join);

        int constrainToRange = Ints.constrainToRange(5, 2, 11);
        assertTrue(5 == constrainToRange);

        assertTrue(Ints.contains(concat, 6));

        int[] ensureCapacity = Ints.ensureCapacity(ints2, 10, 4);

        assertTrue(ensureCapacity.length == 14);

        assertTrue(ensureCapacity[13] == 0);

        //返回其big-endian表示形式存储在字节的前4个字节中的int值；等效于ByteBuffer.wrap（bytes）.getInt（）。
        //没懂
        int fromByteArray = Ints.fromByteArray(byteArray);
        assertTrue(fromByteArray == 16909060);
        //以大尾数顺序返回int值，其字节表示形式为给定的4个字节；
        //等效于Ints.fromByteArray（new byte [] {b1，b2，b3，b4}）。一样没懂
        int fromBytes = Ints.fromBytes(byteArray2[0], byteArray2[1], byteArray2[2], byteArray2[3]);
        assertTrue(395274 == fromBytes);

//        返回值的哈希码；等于调用（（Integer）value）.hashCode（）的结果。
//        Ints.hashCode(3)

        int indexOf = Ints.indexOf(concat, 7);
        assertTrue(6 == indexOf);

        int indexOf1 = Ints.indexOf(concat, new int[]{5, 7, 9});
        assertTrue(5 == indexOf1);

        int indexOf2 = Ints.lastIndexOf(concat, 2);
        assertTrue(1 == indexOf2);

        Comparator<int[]> lexicographicalComparator = Ints.lexicographicalComparator();
        assertTrue(1 == lexicographicalComparator.compare(ints, ints2));

        assertTrue(9 == Ints.max(ints));

        assertTrue(0 == Ints.min(ints2));

        Ints.reverse(ints);

        join = Ints.join("#", ints);
        assertEquals("9#9#7#5#5#4#3#2#1", join);

        Converter<String, Integer> stringConverter = Ints.stringConverter();
        assertTrue(333 == stringConverter.convert("333"));

        byte[] bytes = Ints.toByteArray(666);
        List<Byte> byteList = Bytes.asList(bytes);
        assertEquals("0,0,2,-102", joiner.appendTo(new StringBuilder(), byteList).toString());

        assertTrue(33 == Ints.tryParse("33"));

        assertTrue(null == Ints.tryParse("ds"));

        assertTrue(15 == Ints.tryParse("33", 4));
    }

    /* short -32768 - +32767 */
    private static void testShorts() {
        assertTrue(Integer.valueOf(2).equals(Shorts.BYTES));
        assertTrue(Integer.valueOf(16384).shortValue() == Shorts.MAX_POWER_OF_TWO);

        short[] shortArray = Shorts.toArray(Shorts.asList(Primitives.shortArray));
        short[] shortArray2 = Shorts.toArray(Shorts.asList(Primitives.shortArray2));

        assertTrue(Short.valueOf("-767").equals(Shorts.checkedCast(-767)));

        assertTrue(Integer.valueOf(-3).equals(Shorts.compare(shortArray[5], shortArray2[2])));

        short[] concat = Shorts.concat(shortArray, shortArray2);
        String join = Shorts.join("^", concat);
        assertEquals("1^2^3^4^5^5^7^9^9^0^6^8^10", join);

        // 5 -- 8 -- 10
        assertTrue(Integer.valueOf(8).shortValue() == Shorts.constrainToRange(shortArray[4], shortArray2[2], shortArray2[3]));

        assertTrue(Shorts.contains(shortArray, (short)9));

        short[] ensureCapacity = Shorts.ensureCapacity(shortArray2, 10, 3);
        assertTrue(Integer.valueOf(13).equals(ensureCapacity.length));

        assertTrue(Integer.valueOf(0).shortValue() == ensureCapacity[10]);

        assertTrue(Integer.valueOf(29384).shortValue() == Shorts.saturatedCast(29384));
        assertTrue(Integer.valueOf(32767).shortValue() == Shorts.saturatedCast(32768));
        assertTrue(Integer.valueOf(-32768).shortValue() == Shorts.saturatedCast(-32769));

        Shorts.sortDescending(shortArray, 3, 6);

        join = Shorts.join("^", shortArray);
        assertEquals("1^2^3^5^5^4^7^9^9", join);

        Shorts.sortDescending(shortArray);
        join = Shorts.join("^", shortArray);
        assertEquals("9^9^7^5^5^4^3^2^1", join);

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
