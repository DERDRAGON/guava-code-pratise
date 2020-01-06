package com.der.codepratise.math;

import com.google.common.math.BigIntegerMath;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.*;

/**
 * @author K0790016
 **/
public class GuavaMathClient {

    public static void main(String[] args) {
        testIntMath();
        testLongMath();
        testDoubleMath();
        testBigIntegerMath();
    }

    private static void testBigIntegerMath() {
        //二项式系数（形如(1 + x)ⁿ展开后x的系数（其中n为自然数，k为整数）） -- 相当于从n件物件中，不分先后地选取k件的方法总数，因此也叫做组合数
        BigInteger bigInteger = BigIntegerMath.binomial(100, 8);
        assertEquals(186087894300L, bigInteger.longValue());

        //返回大于或等于x的2的最小幂。
        BigInteger ceilingPowerOfTwo = BigIntegerMath.ceilingPowerOfTwo(BigInteger.valueOf(99));
        assertEquals(128L, ceilingPowerOfTwo.longValue());

        //返回p除以q的结果，使用指定的RoundingMode进行舍入。
        BigInteger divide = BigIntegerMath.divide(BigInteger.valueOf(77), BigInteger.valueOf(11), RoundingMode.UNNECESSARY);
        assertEquals(divide.intValue(), 7);

        //返回n !，即前n个正整数的乘积；如果n == 0，则返回1。
        BigInteger factorial = BigIntegerMath.factorial(7);
        assertEquals(factorial.intValue(), 5040);

        //返回小于或等于x的2的最大幂。
        BigInteger floorPowerOfTwo = BigIntegerMath.floorPowerOfTwo(BigInteger.valueOf(300));
        assertEquals(floorPowerOfTwo.intValue(), 256);

        //如果x表示2的幂，则返回true。
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(512)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(513)));

        int log2 = BigIntegerMath.log2(BigInteger.valueOf(77), RoundingMode.DOWN);
        assertEquals(6, log2);

        int log10 = BigIntegerMath.log10(BigInteger.valueOf(101), RoundingMode.UP);
        assertEquals(log10, 3);

        BigInteger sqrt = BigIntegerMath.sqrt(BigInteger.valueOf(28937), RoundingMode.DOWN);
        assertEquals(170, sqrt.intValue());
    }

    private static void testDoubleMath() {
        //返回n !，即前n个正整数的乘积；如果n == 0，则返回1；否则，返回n !；如果n !，则返回Double.POSITIVE_INFINITY。> Double.MAX_VALUE。
        assertEquals(120.0, DoubleMath.factorial(5), 0.0);

        //“模糊地”比较a和b，其容差接近相等。
        int fuzzyCompare = DoubleMath.fuzzyCompare(3.0, 4.0, 1.0);
        assertEquals(0, fuzzyCompare);
        fuzzyCompare = DoubleMath.fuzzyCompare(6.0, 4.0, 1.0);
        assertEquals(1, fuzzyCompare);

        //如果x表示数学整数，则返回true。
        boolean mathematicalInteger = DoubleMath.isMathematicalInteger(44.3);
        assertFalse(mathematicalInteger);
        mathematicalInteger = DoubleMath.isMathematicalInteger(44.0);
        assertTrue(mathematicalInteger);

        //如果x对于某些有限整数k恰好等于2 ^ k，则返回true。-- x是2的冥
        assertTrue(DoubleMath.isPowerOfTwo(4));
        assertFalse(DoubleMath.isPowerOfTwo(7));

        double log2 = DoubleMath.log2(8);
        assertEquals(log2, 3.0, 0.0);

        log2 = DoubleMath.log2(63, RoundingMode.UP);
        assertEquals(log2, 6.0, 0.0);
        log2 = DoubleMath.log2(65, RoundingMode.UP);
        assertEquals(7.0, log2, 0.0);

        ///算术平均值 -- 不推荐使用
        double mean = DoubleMath.mean(1.0, 4.0, 7.0);
        assertEquals(4.0, mean, 0.0);

        //返回BigInteger值，该值等于用指定的舍入模式舍入的x。
        BigInteger bigInteger = DoubleMath.roundToBigInteger(44.9, RoundingMode.HALF_EVEN);
        assertEquals(bigInteger.intValue(), 45);

        int roundToInt = DoubleMath.roundToInt(23.9, RoundingMode.HALF_UP);
        assertEquals(24, roundToInt);

        long roundToLong = DoubleMath.roundToLong(444.8, RoundingMode.FLOOR);
        assertEquals(444, roundToLong);
    }

    private static void testLongMath() {
        //二项式系数（形如(1 + x)ⁿ展开后x的系数（其中n为自然数，k为整数）） -- 相当于从n件物件中，不分先后地选取k件的方法总数，因此也叫做组合数
        long binomial = LongMath.binomial(7, 3);
        assertEquals(35, binomial);
        //返回大于或等于x的2的最小幂。
        long ceilingPowerOfTwo = LongMath.ceilingPowerOfTwo(4);
        assertEquals(4, ceilingPowerOfTwo);

        //返回a和b的和，前提是它不溢出。-- 溢出报异常
        long checkedAdd = LongMath.checkedAdd(44, 99);
        assertEquals(checkedAdd, 143);

        //返回a和b的乘积，前提是它不会溢出。
        long checkedMultiply = LongMath.checkedMultiply(35, 645);
        assertTrue(22575 == checkedMultiply);

        //将b返回第k次幂，前提是它不会溢出。
        long checkedPow = LongMath.checkedPow(5, 3);
        assertTrue(125 == checkedPow);

        //返回a和b的差，前提是它不溢出。
        long checkedSubtract = LongMath.checkedSubtract(72, 70);
        assertTrue(2 == checkedSubtract);

        //返回p除以q的结果，使用指定的RoundingMod取整
        long divide = LongMath.divide(72, 5, RoundingMode.UP);
        assertTrue(15 == divide);

        //返回n !，即前n个正整数的乘积；如果n == 0，则返回1；如果结果不适合int，则返回Integer.MAX_VALUE。
        long factorial = LongMath.factorial(5);
        assertTrue(120 == factorial);

        //返回小于或等于x的2的最大幂。
        long floorPowerOfTwo = LongMath.floorPowerOfTwo(94);
        assertTrue(floorPowerOfTwo == 64);

        //返回a，b的最大公约数。
        long gcd = LongMath.gcd(43, 344);
        assertTrue(43 == gcd);

        //如果x表示2的幂，则返回true。
        assertTrue(LongMath.isPowerOfTwo(1024));

        //如果n是素数，则返回true：大于1的整数，不能将其分解为较小的正整数的乘积。
        assertTrue(LongMath.isPrime(79));

        //返回x的以10为底的对数，根据指定的舍入模式舍入。
        long log10 = LongMath.log10(99, RoundingMode.UP);
        assertTrue(2 == log10);
        log10 = LongMath.log10(99, RoundingMode.DOWN);
        assertTrue(log10 == 1);

        long log2 = LongMath.log2(88, RoundingMode.CEILING);
        assertTrue(7 == log2);
        log2 = LongMath.log2(88, RoundingMode.FLOOR);
        assertTrue(6 == log2);

        //返回x和y的算术平均值，四舍五入为负无穷大。
        long mean = LongMath.mean(-5, 99);
        assertTrue(47 == mean);

        //返回x 求余 m，这是小于m的非负值。
        long mod = LongMath.mod(444444, 5);
        assertTrue(mod == 4);

        long pow = LongMath.pow(555, 8888);
        assertEquals("-4721724385030486367", String.valueOf(pow));

        //返回a和b的总和，除非它将溢出或下溢，否则分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        long saturatedAdd = LongMath.saturatedAdd(523, 908);
        assertTrue(1431 == saturatedAdd);

        //返回a和b的乘积，除非它会上溢或下溢，否则分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        long saturatedMultiply = LongMath.saturatedMultiply(77, 11);
        assertTrue(847 == saturatedMultiply);

        //除非b溢出或下溢，否则分别将b返回k的幂，在这种情况下，将分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        long saturatedPow = LongMath.saturatedPow(7, 10);
        assertTrue(282475249 == saturatedPow);

        //返回a和b的差，除非它会溢出或下溢，否则分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        long saturatedSubtract = LongMath.saturatedSubtract(7, 9);
        assertTrue(saturatedSubtract == -2);

        //返回x的平方根，以指定的舍入模式舍入。
        long sqrt = LongMath.sqrt(99, RoundingMode.UP);
        assertTrue(10 == sqrt);
    }

    private static void testIntMath() {
        //二项式系数（形如(1 + x)ⁿ展开后x的系数（其中n为自然数，k为整数）） -- 相当于从n件物件中，不分先后地选取k件的方法总数，因此也叫做组合数
        int binomial = IntMath.binomial(4, 2);
        assertEquals(6, binomial);

        //返回大于或等于x的2的最小幂。
        int ceilingPowerOfTwo = IntMath.ceilingPowerOfTwo(77);
        assertEquals(128, ceilingPowerOfTwo);

        //返回a和b的和，前提是它不溢出。-- 溢出报异常
        int checkedAdd = IntMath.checkedAdd(33, 88);
        assertTrue(121 == checkedAdd);
//        int checkedAdd1 = IntMath.checkedAdd(1937834543, 1989723848);
        //返回a和b的乘积，前提是它不会溢出。
        int checkedMultiply = IntMath.checkedMultiply(35, 645);
        assertTrue(22575 == checkedMultiply);

        //将b返回第k次幂，前提是它不会溢出。
        int checkedPow = IntMath.checkedPow(5, 3);
        assertTrue(125 == checkedPow);

        //返回a和b的差，前提是它不溢出。
        int checkedSubtract = IntMath.checkedSubtract(72, 70);
        assertTrue(2 == checkedSubtract);

        //返回p除以q的结果，使用指定的RoundingMod取整
        int divide = IntMath.divide(72, 5, RoundingMode.UP);
        assertTrue(15 == divide);

        //返回n !，即前n个正整数的乘积；如果n == 0，则返回1；如果结果不适合int，则返回Integer.MAX_VALUE。
        int factorial = IntMath.factorial(5);
        assertTrue(120 == factorial);

        //返回小于或等于x的2的最大幂。
        int floorPowerOfTwo = IntMath.floorPowerOfTwo(94);
        assertTrue(floorPowerOfTwo == 64);

        //返回a，b的最大公约数。
        int gcd = IntMath.gcd(43, 344);
        assertTrue(43 == gcd);

        //如果x表示2的幂，则返回true。
        assertTrue(IntMath.isPowerOfTwo(1024));

        //如果n是素数，则返回true：大于1的整数，不能将其分解为较小的正整数的乘积。
        assertTrue(IntMath.isPrime(79));

        //返回x的以10为底的对数，根据指定的舍入模式舍入。
        int log10 = IntMath.log10(99, RoundingMode.UP);
        assertTrue(2 == log10);
        log10 = IntMath.log10(99, RoundingMode.DOWN);
        assertTrue(log10 == 1);

        int log2 = IntMath.log2(88, RoundingMode.CEILING);
        assertTrue(7 == log2);
        log2 = IntMath.log2(88, RoundingMode.FLOOR);
        assertTrue(6 == log2);

        //返回x和y的算术平均值，四舍五入为负无穷大。
        int mean = IntMath.mean(-5, 99);
        assertTrue(47 == mean);

        //返回x 求余 m，这是小于m的非负值。
        int mod = IntMath.mod(444444, 5);
        assertTrue(mod == 4);

        int pow = IntMath.pow(555, 8888);
        assertTrue(1190479521 == pow);

        //返回a和b的总和，除非它将溢出或下溢，否则分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        int saturatedAdd = IntMath.saturatedAdd(523, 908);
        assertTrue(1431 == saturatedAdd);

        //返回a和b的乘积，除非它会上溢或下溢，否则分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        int saturatedMultiply = IntMath.saturatedMultiply(77, 11);
        assertTrue(847 == saturatedMultiply);

        //除非b溢出或下溢，否则分别将b返回k的幂，在这种情况下，将分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        int saturatedPow = IntMath.saturatedPow(7, 10);
        assertTrue(282475249 == saturatedPow);

        //返回a和b的差，除非它会溢出或下溢，否则分别返回Integer.MAX_VALUE或Integer.MIN_VALUE。
        int saturatedSubtract = IntMath.saturatedSubtract(7, 9);
        assertTrue(saturatedSubtract == -2);

        //返回x的平方根，以指定的舍入模式舍入。
        int sqrt = IntMath.sqrt(99, RoundingMode.UP);
        assertTrue(10 == sqrt);

    }
}
