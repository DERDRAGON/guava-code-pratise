package com.der.codepratise.primitives;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.primitives.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

    private static final long[] longArray = {1,2,3,4,5,5,7,9,9};
    private static final long[] longArray2 = {0, 6, 8, 10};

    private static final float[] floatArray = {1,2,3,4,5,5,7,9,9};
    private static final float[] floatArray2 = {0, 6, 8, 10};

    public static void main(String[] args) {
        testBytes();
        testSignedBytes();
        testUnsignedBytes();
        testShorts();
        testInts();
        testUnsignedInteger();
        testUnsignedInts();
        testLongs();
        testUnsignedLong();
        testUnsignedLongs();
        testFloats();
    }

    private static void testFloats() {
        assertTrue(4 == Floats.BYTES);

        float[] floats = Floats.toArray(Floats.asList(floatArray));
        float[] floats2 = Floats.toArray(Floats.asList(floatArray2));

        assertTrue(1 == Floats.compare(3, 2));

        float[] concat = Floats.concat(floats, floats2);
        String join = Floats.join("@", concat);
        assertEquals("1.0@2.0@3.0@4.0@5.0@5.0@7.0@9.0@9.0@0.0@6.0@8.0@10.0", join);

        float aFloat = new Random().nextFloat() * 10;
        float constrainToRange = Floats.constrainToRange(aFloat, 5, 9);
        assertTrue(constrainToRange >= 5 && constrainToRange <= 9);

        assertTrue(Floats.contains(concat, 8));

        float[] ensureCapacity = Floats.ensureCapacity(floats, 10, 5);
        assertTrue(0 == ensureCapacity[ensureCapacity.length-1]);

        assertTrue(4 == Floats.indexOf(floats, 5));

        assertTrue(6 == Floats.indexOf(floats, new float[]{7,9,9}));

        assertTrue(6 == Floats.lastIndexOf(floats, 7));

        //是否不为无限数
        assertFalse(Floats.isFinite(1f/0f));
        assertTrue(Floats.isFinite(4f));

        Comparator<float[]> comparator = Floats.lexicographicalComparator();
        int compare = comparator.compare(concat, floats2);
        assertTrue(1 == compare);

        assertTrue(10f == Floats.max(concat));
        assertTrue(0f == Floats.min(concat));

        Floats.reverse(floats, 3, 7);
        join = Floats.join("`", floats);
        assertEquals("1.0`2.0`3.0`7.0`5.0`5.0`4.0`9.0`9.0", join);

        Floats.reverse(floats);
        join = Floats.join("`", floats);
        assertEquals("9.0`9.0`4.0`5.0`5.0`7.0`3.0`2.0`1.0", join);

        Floats.sortDescending(floats, 3, 8);
        join = Floats.join("`", floats);
        assertEquals("9.0`9.0`4.0`7.0`5.0`5.0`3.0`2.0`1.0", join);

        Floats.sortDescending(floats);
        join = Floats.join("`", floats);
        assertEquals("9.0`9.0`7.0`5.0`5.0`4.0`3.0`2.0`1.0", join);

        Converter<String, Float> stringConverter = Floats.stringConverter();
        @Nullable Float convert = stringConverter.convert("434334");
        assertTrue(434334f == convert);

        @Nullable Float tryParse = Floats.tryParse("434");
        assertTrue(434f == tryParse.floatValue());

        @Nullable Float tryParse1 = Floats.tryParse("sfd");
        assertTrue(null == tryParse1);
    }

    private static void testUnsignedLongs() {
        assertTrue(1 == UnsignedLongs.compare(8, 2));

        long divide = UnsignedLongs.divide(8, 3);
        assertTrue(2 == divide);

        String join = UnsignedLongs.join(")", longArray);
        assertEquals("1)2)3)4)5)5)7)9)9", join);
        long[] longs = Longs.ensureCapacity(longArray, 10, 0);
        longs[longs.length - 1] = -1;
        join = UnsignedLongs.join("!", longs);
        assertEquals("1!2!3!4!5!5!7!9!9!18446744073709551615", join);

        long max = UnsignedLongs.max(longs);
        assertTrue(-1 == max);

        long min = UnsignedLongs.min(longs);
        assertTrue(1 == min);

        long parseUnsignedLong = UnsignedLongs.parseUnsignedLong("3333");
        assertTrue(3333 == parseUnsignedLong);

        long unsignedLong = UnsignedLongs.parseUnsignedLong("32332", 5);
        assertTrue(2217 == unsignedLong);

        long remainder = UnsignedLongs.remainder(3333, 23);
        assertTrue(21 == remainder);

        Longs.reverse(longs);
        UnsignedLongs.sort(longs, 2, 8);
        join = UnsignedLongs.join("+", longs);
        assertEquals("18446744073709551615+9+3+4+5+5+7+9+2+1", join);

        UnsignedLongs.sort(longs);
        join = UnsignedLongs.join("+", longs);
        assertEquals("1+2+3+4+5+5+7+9+9+18446744073709551615", join);

        UnsignedLongs.sortDescending(longs, 3, 9);
        join = UnsignedLongs.join("+", longs);
        assertEquals("1+2+3+9+9+7+5+5+4+18446744073709551615", join);

        UnsignedLongs.sortDescending(longs);
        join = UnsignedLongs.join("+", longs);
        assertEquals("18446744073709551615+9+9+7+5+5+4+3+2+1", join);

    }

    private static void testUnsignedLong() {

        assertEquals("-1", String.valueOf(UnsignedLongs.MAX_VALUE));
        assertEquals("18446744073709551615", String.valueOf(UnsignedLong.MAX_VALUE));

        assertTrue(UnsignedLong.ZERO.equals(UnsignedLong.valueOf(0)));

        assertTrue(UnsignedLong.ONE.equals(UnsignedLong.valueOf(1)));

        assertTrue(1 == UnsignedLong.ONE.compareTo(UnsignedLong.ZERO));

        UnsignedLong unsignedLong = UnsignedLong.valueOf(783);
        UnsignedLong unsignedLong2 = UnsignedLong.valueOf(8);
        UnsignedLong dividedBy = unsignedLong.dividedBy(UnsignedLong.valueOf(9));
        assertTrue(dividedBy.equals(UnsignedLong.valueOf(87)));

        UnsignedLong minus = unsignedLong.minus(UnsignedLong.valueOf(3));
        assertTrue(minus.equals(UnsignedLong.valueOf(780)));

    }

    private static void testLongs() {
        assertTrue(8 == Longs.BYTES);

//        System.out.println(Longs.MAX_POWER_OF_TWO);

        long[] longs = Longs.toArray(Longs.asList(longArray));
        long[] longs2 = Longs.toArray(Longs.asList(longArray2));

        assertTrue(-1 == Longs.compare(3, 4));

        long[] concat = Longs.concat(longs, longs2);
        String join = Longs.join("-", concat);
        assertEquals("1-2-3-4-5-5-7-9-9-0-6-8-10", join);

        double aDouble = new Random().nextDouble()*10;
        long x = (long)aDouble;
        long constrainToRange = Longs.constrainToRange(x, 5, 9);
        assertTrue(constrainToRange >= 5 && constrainToRange <= 9);

        assertTrue(Longs.contains(longs, 4));

        long[] ensureCapacity = Longs.ensureCapacity(longs, 10, 5);
        assertTrue(15 == ensureCapacity.length);

        long fromByteArray = Longs.fromByteArray(byteArray);
        assertEquals("72623859790317321", String.valueOf(fromByteArray));

        long fromBytes = Longs.fromBytes(byteArray2[0], byteArray2[1], byteArray2[2], byteArray2[3], byteArray[2], byteArray[3], byteArray[4], byteArray[6]);
        assertEquals("1697688953554183", String.valueOf(fromBytes));

        assertTrue(6 == Longs.indexOf(concat, 7));
        assertTrue(7 == Longs.indexOf(concat, new long[]{9,9,0}));
        assertTrue(8 == Longs.lastIndexOf(concat, 9));

        assertTrue(10 == Longs.max(concat));
        assertTrue(0 == Longs.min(concat));

        Longs.reverse(concat, 3, 8);
        join = Longs.join("-", concat);
        assertEquals("1-2-3-9-7-5-5-4-9-0-6-8-10", join);

        Longs.reverse(concat);
        join = Longs.join("-", concat);
        assertEquals("10-8-6-0-9-4-5-5-7-9-3-2-1", join);

        Longs.sortDescending(concat, 3,6);
        join = Longs.join("-", concat);
        assertEquals("10-8-6-9-4-0-5-5-7-9-3-2-1", join);

        Longs.sortDescending(concat);
        join = Longs.join("-", concat);
        assertEquals("10-9-9-8-7-6-5-5-4-3-2-1-0", join);

        Converter<String, Long> stringConverter = Longs.stringConverter();
        @Nullable Long convert = stringConverter.convert("8475397");
        assertTrue(convert.equals(8475397L));

        List<Byte> bytes = Bytes.asList(Longs.toByteArray(convert));
        assertEquals("0,0,0,0,0,-127,83,5", joiner.appendTo(new StringBuilder(), bytes).toString());

        @Nullable Long tryParse = Longs.tryParse("89834");
        assertTrue(tryParse.equals(89834L));

        @Nullable Long parse = Longs.tryParse("1010111", 2);
        assertTrue(parse.equals(87L));
        @Nullable Long parse2 = Longs.tryParse("78943", 2);
        assertTrue(null == parse2);
    }

    private static void testUnsignedInts() {

        String join = UnsignedInts.join("^", intArray);
        assertEquals("1^2^3^4^5^5^7^9^9", join);

        int checkedCast = UnsignedInts.checkedCast(77L);
        assertTrue(checkedCast == 77);

        assertTrue(-1 == UnsignedInts.compare(4, 6));

        assertTrue(UnsignedInts.decode("4") == 4);

        assertTrue(3 == UnsignedInts.divide(33, 11));

        Comparator<int[]> comparator = UnsignedInts.lexicographicalComparator();
        assertTrue(1 == comparator.compare(intArray, intArray2));

        int max = UnsignedInts.max(intArray);
        assertTrue(max == 9);

        int min = UnsignedInts.min(intArray2);
        assertTrue(0 == min);

        //返回给定十进制字符串表示的无符号int值
        int parseUnsignedInt = UnsignedInts.parseUnsignedInt("32");
        assertTrue(32  == parseUnsignedInt);

        int parseUnsignedInt1 = UnsignedInts.parseUnsignedInt("645456", 8);
        assertTrue(215854 == parseUnsignedInt1);

        //求余
        int remainder = UnsignedInts.remainder(44, 12);
        assertTrue(8 == remainder);

        int saturatedCast = UnsignedInts.saturatedCast(2332323243L);
        assertTrue(-1962644053 == saturatedCast);

        UnsignedInts.sort(intArray);
        join = UnsignedInts.join("_", intArray);
        assertEquals("1_2_3_4_5_5_7_9_9", join);

        int[] ints = Ints.toArray(Ints.asList(intArray));
        UnsignedInts.sortDescending(ints);
        join = UnsignedInts.join("*", ints);
        assertEquals("9*9*7*5*5*4*3*2*1", join);

        assertEquals("734534589", UnsignedInts.toString(734534589));

        assertEquals("101001000000000111001", UnsignedInts.toString(1343545, 2));
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
