package com.der.codepratise.Rranges;

import com.google.common.collect.BoundType;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import static org.junit.Assert.*;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2020-01-03 18:02
 */
public class Rranges {

    public static void main(String[] args) {
        Range<Integer> openClosed = Range.<Integer>openClosed(3, 10);
        assertEquals("(3..10]", openClosed.toString());
        assertTrue(openClosed.contains(5));
        assertFalse(openClosed.containsAll(Lists.newArrayList(3,5,6)));

        assertTrue(3 == openClosed.lowerEndpoint());
        assertTrue(openClosed.lowerBoundType() == BoundType.OPEN);

        assertTrue(10 == openClosed.upperEndpoint());
        assertTrue(openClosed.upperBoundType() == BoundType.CLOSED);

        Range<Integer> greaterThan = Range.greaterThan(2);
        //包含
        assertTrue(greaterThan.encloses(openClosed));

        Range<Integer> integerRange = Range.<Integer>all();
        //交集
        Range<Integer> intersection = integerRange.intersection(greaterThan);
        assertEquals("(2..+∞)", intersection.toString());

        Range<Integer> atLeast = Range.atLeast(16);
        assertTrue(atLeast.isConnected(intersection));

        Range<Integer> atMost = Range.atMost(64);
        assertFalse(atMost.isEmpty());

        //两个不相交的之间的最大间隔范围
        Range<Integer> gap = atMost.gap(Range.atLeast(128));
        assertEquals("(64..128)", gap.toString());

        Range<Integer> singleton = Range.singleton(256);
        assertFalse(singleton.encloses(greaterThan));
    }
}
