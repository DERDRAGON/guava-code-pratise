package com.der.codepratise.CollectionsUtil;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * @author K0790016
 **/
public class ImmutableCollection {

    private static final Integer[] ints = {1,2,1,3,5,2};
    private static final List<Integer> integers = Ints.asList(1,2,5,2,1,3);

    private static final Ordering<Comparable> natural = Ordering.natural();

    public static final ImmutableSet<Color> GOOGLE_COLORS =
            ImmutableSet.<Color>builder()
                    .add(Color.CYAN)
                    .add(new Color(0, 191, 255))
                    .build();


    public static void main(String[] args) {
        testImmutableSet();
        testImmutableList();
    }

    private static void testImmutableSet() {
        ImmutableSet<Integer> immutableSet = ImmutableSet.copyOf(integers);
        Assert.assertEquals(String.valueOf(4), String.valueOf(immutableSet.size()));

        ImmutableSet<Integer> set = ImmutableSet.of(1);
        Assert.assertFalse(set.equals(immutableSet));

        ImmutableSet<Boolean> booleans = ImmutableSet.<Boolean>builder().add(Boolean.FALSE).add(Boolean.TRUE).build();
        ImmutableSet<Integer> immutableSet1 = ImmutableSet.<Integer>builderWithExpectedSize(5).addAll(integers).add(6).build();
        Assert.assertTrue(new Integer(5).equals(immutableSet1.size()));

//        ImmutableSet.toImmutableSet();  ??

        ImmutableSortedSet<Integer> immutableSortedSet = ImmutableSortedSet.<Integer>copyOf(ImmutableCollection.integers);
        Assert.assertTrue(natural.isOrdered(immutableSortedSet));

        ImmutableSortedSet<Integer> immutableSortedSet1 = ImmutableSortedSet.<Integer>copyOf(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        }, immutableSortedSet);

        Assert.assertTrue(natural.reverse().isOrdered(immutableSortedSet1));
        ImmutableSortedSet<Integer> immutableSortedSet2 = ImmutableSortedSet.<Integer>copyOfSorted(immutableSortedSet1);
        Assert.assertTrue(natural.isOrdered(immutableSortedSet2.descendingSet()));

        Assert.assertTrue(new Integer(5).equals(immutableSortedSet2.floor(4)));

    }

    private static void testImmutableList() {
        ImmutableList<Integer> immutableList = ImmutableList.<Integer>builder().add(2).addAll(integers).build();
        ImmutableList<Integer> immutableList1 = ImmutableList.<Integer>builderWithExpectedSize(5).addAll(integers).build();
        ImmutableList<Integer> immutableList2 = ImmutableList.<Integer>of(2, 3, 4, 5);
        ImmutableList<Integer> immutableList3 = ImmutableList.<Integer>copyOf(integers);
        ImmutableList<Integer> immutableList4 = ImmutableList.<Integer>copyOf(ints);
        Assert.assertFalse(Arrays.equals(immutableList3.toArray(), immutableList4.toArray()));
        ImmutableList<Integer> immutableList5 = ImmutableList.<Integer>sortedCopyOf(ImmutableCollection.integers);
        Assert.assertTrue(natural.isOrdered(immutableList5));
    }
}
