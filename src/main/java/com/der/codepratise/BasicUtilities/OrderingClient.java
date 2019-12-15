package com.der.codepratise.BasicUtilities;

import com.alibaba.fastjson.JSON;
import com.der.codepratise.entity.Book;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 排序: Guava强大的”流畅风格比较器”
 * @author K0790016
 **/
@Slf4j
public class OrderingClient {

    public static void main(String[] args) {
        Ordering<Comparable> natural = Ordering.natural();
        Assert.assertEquals(-1, natural.compare(1, 8));
        Ordering<Object> usingToString = Ordering.usingToString();
        Assert.assertTrue(usingToString.compare("4434", "sdfkdsj") < 0); //-- -63

        Comparator<Integer> integerComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };

        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        Ordering<Integer> nullsFirst = Ordering.from(integerComparator).nullsFirst();
        Assert.assertEquals(String.valueOf(5), String.valueOf(nullsFirst.max(integers)));
        Assert.assertTrue(nullsFirst.isOrdered(integers));

        Ordering<Integer> integerOrdering = new Ordering<Integer>() {
            @Override
            public int compare(@Nullable Integer v1, @Nullable Integer v2) {
                return Ints.compare(v1, v2);
            }
        };

        Ordering<Book> bookOrdering = Ordering.natural().nullsLast().onResultOf(new Function<Book, Integer>() {
            @Override
            public Integer apply(@Nullable Book book) {
                return book.getId();
            }
        });

        List<Book> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            book.setId(i);
            list.add(book);
        }
        list.add(null);
        list.add(null);
        list.add(null);
        // 在一个集合中找出最大的几个 -- 未避免null，除非自己操作
        List<Book> greatest = null;
        try {
            greatest = bookOrdering.greatestOf(list, 5);
            greatest.forEach(book -> System.out.println(JSON.toJSONString(book)));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        List<String> sortedCopy = usingToString.sortedCopy(Arrays.asList("43", "sf", "sfd", "909", "dsfdf"));
        Assert.assertTrue(usingToString.isOrdered(sortedCopy));
    }
}
