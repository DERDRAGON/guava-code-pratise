package com.der.codepratise.BasicUtilities;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Converter;
import com.google.common.primitives.*;

import java.util.Comparator;
import java.util.List;

/**
 * @program: pratise
 * @description: ${description}
 * @author: long
 * @create: 2019-12-10 15:38
 */
public class BaseDataType {

    public static void main(String[] args) {
        pratiseInts();
        pratiseBytes();
        pratiseLongs();
        pratiseFloats();
        pratiseDoubles();
        pratiseBooleans();
    }

    private static void pratiseInts() {
        List<Integer> list = Ints.asList(2, 3, 4, 56);
        System.out.println(JSON.toJSONString(list));

        System.out.println(Ints.join("^",5,23,5,456,56,6, list.get(0)));
        System.out.println(Ints.join("^", Ints.toArray(list)));

        int[] ints = {345, 465, 23, 2};
        int[] concat = Ints.concat(new int[]{1, 23, 45}, ints, new int[]{2, 46, 878, 89, 3434});
        System.out.println(JSON.toJSONString(concat));

        System.out.println(Ints.max(concat));
        System.out.println(Ints.min(concat));

        System.out.println(Ints.contains(concat, 23));

        //checkedCast方法  进行类型转换：将long类型的输入参数转换为int类型的返回，前提是long类型的值没有超过int的可表示范围
        System.out.println(Ints.checkedCast(9999999l));

        // toByteArray方法  将一个整形value转换为大端表示，以byte array返回，array中每个值有4位。
        byte[] bytes = Ints.toByteArray(333);
        System.out.println(bytes.length);
        for (byte aByte : bytes) {
            System.out.println(aByte);
        }

        Comparator<int[]> comparator = Ints.lexicographicalComparator();
        System.out.println(comparator.compare(ints, concat));

        // 如果小于规定的最小尺寸，则改为minLength+padding的长度的int[]
        int[] capacity = Ints.ensureCapacity(concat, 4, 9);
        System.out.println(JSON.toJSONString(capacity));

        int cast = Ints.saturatedCast(2434343223432l);
        System.out.println(cast);
    }

    private static void pratiseBytes() {
        byte abc = 2;
        List<Byte> bytes = Bytes.asList(abc);
        System.out.println(JSON.toJSONString(bytes));
    }

    private static void pratiseLongs() {
        List<Long> list = Longs.asList(3l, 34l, 32466l, 4334);
        System.out.println(JSON.toJSONString(list));

        // 类型转换器 string-long 格式不对报错
        Converter<String, Long> converter = Longs.stringConverter();

        System.out.println(converter.convert("2343"));

        // 类型转换器 string-long 格式不对返回空
        System.out.println(Longs.tryParse("2434d2"));
    }

    private static void pratiseFloats() {
        List<Float> floats = Floats.asList(2.3f, 3.4f);
        System.out.println(JSON.toJSONString(floats));
    }

    private static void pratiseDoubles() {
        System.out.println(Doubles.isFinite(244343));
        System.out.println(Double.POSITIVE_INFINITY);
    }

    private static void pratiseChars() {
        List<Character> list = Chars.asList('2', '4');
        System.out.println(list);
    }

    private static void pratiseBooleans() {
        List<Boolean> list = Booleans.asList(true, false);
        System.out.println(list);
        System.out.println(Booleans.countTrue(true, false, true, true));

        System.out.println(Booleans.join("4", false, true, false, true, true));

    }
}
