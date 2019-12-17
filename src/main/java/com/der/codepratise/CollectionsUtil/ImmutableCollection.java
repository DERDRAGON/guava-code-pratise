package com.der.codepratise.CollectionsUtil;

import com.der.codepratise.entity.Book;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import org.junit.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * @author K0790016
 **/
public class ImmutableCollection {

    private static final Integer[] ints = {1,2,1,3,5,2};

    private static final List<Integer> integers = Ints.asList(1,2,5,2,1,3);

    private static final Map<String, Object> maps = Maps.newHashMap();

    private static final Ordering<Comparable> natural = Ordering.natural();

    private static final Comparator<Integer> comparator = (a, b) -> a - b;

    public static final ImmutableSet<Color> GOOGLE_COLORS =
            ImmutableSet.<Color>builder()
                    .add(Color.CYAN)
                    .add(new Color(0, 191, 255))
                    .build();
    static {
        maps.put("id", 123);
        maps.put("name", "天行健，君子以自强不息");
        maps.put("description", "地势坤，君子以厚德载物");
    }

    public static void main(String[] args) {
        testImmutableList();
        testImmutableSet();
        testImmutableMap();
    }

    private static void testImmutableMap() {
        ImmutableMap<String, Object> immutableMap = ImmutableMap.copyOf(maps);
        Assert.assertEquals("{name=天行健，君子以自强不息, description=地势坤，君子以厚德载物, id=123}", immutableMap.toString());
        // 转化为一键多值的map
        ImmutableSetMultimap<String, Object> immutableSetMultimap = immutableMap.asMultimap();
        Assert.assertEquals("{name=[天行健，君子以自强不息], description=[地势坤，君子以厚德载物], id=[123]}", immutableSetMultimap.toString());

        ImmutableMap<String, Object> immutableMap1 = ImmutableMap.<String, Object>of("id", 321, "name", "宝剑锋从磨砺出", "description", "梅花香自苦寒来");
        Assert.assertEquals("{id=321, name=宝剑锋从磨砺出, description=梅花香自苦寒来}", immutableMap1.toString());

        ImmutableMap<String, Object> immutableMap2 = ImmutableMap.<String, Object>builderWithExpectedSize(3).putAll(maps).build();
        Assert.assertEquals("{name=天行健，君子以自强不息, description=地势坤，君子以厚德载物, id=123}", immutableMap2.toString());

        ImmutableMultimap<String, Object> immutableMultimap = ImmutableMultimap.<String, Object>copyOf(immutableSetMultimap);
        Assert.assertEquals("{name=[天行健，君子以自强不息], description=[地势坤，君子以厚德载物], id=[123]}", immutableMultimap.toString());

        ImmutableMultimap<String, Object> immutableMultimap1 = ImmutableMultimap.<String, Object>of("id", 123, "id", 234, "name", "噫吁戏危乎高哉", "name", "尔来四万八千瑞", "description", "不与秦塞通人烟");
        ImmutableMultimap<Object, String> inverse = immutableMultimap1.inverse();
        Assert.assertTrue(new Integer(5).equals(inverse.keys().size()));
        System.out.println(inverse.toString());
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

        ImmutableSortedSet<Integer> immutableSortedSet1 = ImmutableSortedSet.<Integer>copyOf(comparator.reversed(), immutableSet);

        Assert.assertTrue(natural.reverse().isOrdered(immutableSortedSet1));
        ImmutableSortedSet<Integer> immutableSortedSet2 = ImmutableSortedSet.<Integer>copyOfSorted(immutableSortedSet1);
        Assert.assertTrue(natural.isOrdered(immutableSortedSet2.descendingSet()));

        Assert.assertTrue(new Integer(5).equals(immutableSortedSet2.floor(4)));

        ImmutableSortedMultiset<Integer> immutableSortedMultiset = ImmutableSortedMultiset.<Integer>copyOf(ints);
        Assert.assertEquals("1", immutableSortedMultiset.firstEntry().getElement().toString());
        Assert.assertEquals("[1 x 2, 2 x 2, 3, 5]", immutableSortedMultiset.toString());
        Assert.assertEquals("5", immutableSortedMultiset.descendingMultiset().firstEntry().getElement().toString());
        Assert.assertEquals("[5, 3, 2 x 2, 1 x 2]", immutableSortedMultiset.descendingMultiset().toString());
        // Enum BoundType 指示范围本身（“关闭”）是否包含某个范围的端点（“打开”）。
        //如果范围在一侧不受限制，则在该侧既不打开也不关闭；
        //界限根本不存在。
        ImmutableSortedMultiset<Integer> multiset = immutableSortedMultiset.headMultiset(3, BoundType.CLOSED);
        Assert.assertEquals("[1 x 2, 2 x 2, 3]", multiset.toString());
        ImmutableSortedMultiset<Integer> multiset1 = immutableSortedMultiset.headMultiset(3, BoundType.OPEN);
        Assert.assertEquals("[1 x 2, 2 x 2]", multiset1.toString());
        ImmutableSortedMultiset<Book> immutableSortedMultiset1 = ImmutableSortedMultiset.<Book>naturalOrder().addCopies(new Book(1), 3).add(new Book(2)).add(new Book(3)).build();
        Assert.assertEquals("[Book(id=1, name=null, author=null) x 3, Book(id=2, name=null, author=null), Book(id=3, name=null, author=null)]", immutableSortedMultiset1.toString());

        ImmutableSortedMultiset<Integer> immutableSortedMultiset2 = ImmutableSortedMultiset.copyOfSorted(immutableSortedMultiset).subMultiset(1, BoundType.OPEN, 5, BoundType.CLOSED);
        Assert.assertEquals("[2 x 2, 3, 5]", immutableSortedMultiset2.toString());
        ImmutableSortedMultiset<Integer> immutableSortedMultiset3 = ImmutableSortedMultiset.orderedBy(comparator.reversed()).addAll(integers).build();
        Assert.assertEquals("[5, 3, 2 x 2, 1 x 2]", immutableSortedMultiset3.toString());
        ImmutableSortedMultiset<Integer> immutableSortedMultiset4 = ImmutableSortedMultiset.<Integer>reverseOrder().addAll(integers).build();
        Assert.assertEquals("[5, 3, 2 x 2, 1 x 2]", immutableSortedMultiset4.toString());
        ImmutableSortedMultiset<Integer> immutableSortedMultiset5 = immutableSortedMultiset4.tailMultiset(2, BoundType.CLOSED);
        Assert.assertEquals("[2 x 2, 1 x 2]", immutableSortedMultiset5.toString());
//        System.out.println(immutableSortedMultiset5.toString());

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
