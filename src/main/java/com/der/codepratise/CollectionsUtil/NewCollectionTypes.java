package com.der.codepratise.CollectionsUtil;

import com.alibaba.fastjson.JSON;
import com.der.codepratise.entity.MapInstanceEntity;
import com.der.codepratise.entity.MapTestEntity;
import com.google.common.collect.*;
import org.assertj.core.util.Lists;
import org.junit.Assert;

import java.util.*;

/**
 * @author K0790016
 **/
public class NewCollectionTypes {

    private static final Ordering natural = Ordering.natural();

    private static final Ordering reverse = natural.reverse();

    private static final List<MapTestEntity> list = Lists.newArrayList(new MapTestEntity(6, "six"),
            new MapTestEntity(7, "seven"), new MapTestEntity(8, "eight"), new MapTestEntity(9, "nine"),
            new MapTestEntity(10, "ten"), new MapTestEntity(11, "eleven"), new MapTestEntity(12, "twelve"),
            new MapTestEntity(13, "thirteen"), new MapTestEntity(14, "fourteen"), new MapTestEntity(15, "fitteen"));

    public static void main(String[] args) {
        testMultiset();
        testMultimap();
        testBiMap();
        testTable();
        testClassToInstanceMap();
        testRangeSet();
        testRangeMap();
    }

    private static void testRangeMap() {
        TreeRangeMap<Integer, MapTestEntity> treeRangeMap = TreeRangeMap.<Integer, MapTestEntity>create();
        treeRangeMap.put(Range.closedOpen(10, 15), new MapTestEntity(25, "twenty five"));
        Assert.assertEquals("[[10..15)=MapTestEntity(id=25, name=twenty five, sex=null, description=null)]", treeRangeMap.toString());
        //后添加的区间会覆盖之前的区间
        treeRangeMap.put(Range.closedOpen(14, 17), new MapTestEntity(26, "twenty six"));
        Assert.assertEquals("MapTestEntity(id=25, name=twenty five, sex=null, description=null)", treeRangeMap.get(12).toString());
        Map<Range<Integer>, MapTestEntity> asDescendingMapOfRanges = treeRangeMap.asDescendingMapOfRanges();
        Assert.assertEquals("[[10..14)=MapTestEntity(id=25, name=twenty five, sex=null, description=null), [14..17)=MapTestEntity(id=26, name=twenty six, sex=null, description=null)]",
                treeRangeMap.toString());
        treeRangeMap.put(Range.closedOpen(13, 17), new MapTestEntity(27, "twenty seven"));
        Assert.assertEquals("[[10..13)=MapTestEntity(id=25, name=twenty five, sex=null, description=null), [13..17)=MapTestEntity(id=27, name=twenty seven, sex=null, description=null)]",
                treeRangeMap.toString());
        Map<Range<Integer>, MapTestEntity> asMapOfRanges = treeRangeMap.asMapOfRanges();
        Assert.assertTrue(new Integer(2).equals(asMapOfRanges.size()));

    }

    private static void testRangeSet() {
        //RangeSet描述了一组不相连的、非空的区间。当把一个区间添加到可变的RangeSet时，所有相连的区间会被合并，空区间会被忽略。
        RangeSet<Integer> treeRangeSet = TreeRangeSet.<Integer>create();
        treeRangeSet.add(Range.closed(1, 10)); // {[1, 10]}
        Assert.assertEquals("[[1..10]]", treeRangeSet.toString());
        treeRangeSet.add(Range.closedOpen(11, 15)); // disconnected range: {[1, 10], [11, 15)}
        Assert.assertEquals("[[1..10], [11..15)]", treeRangeSet.toString());
        treeRangeSet.add(Range.closedOpen(15, 20)); // connected range; {[1, 10], [11, 20)}
        Assert.assertEquals("[[1..10], [11..20)]", treeRangeSet.toString());
        treeRangeSet.add(Range.openClosed(0, 0)); // empty range; {[1, 10], [11, 20)}
        Assert.assertEquals("[[1..10], [11..20)]", treeRangeSet.toString());
        treeRangeSet.remove(Range.open(5, 10)); // splits [1, 10]; {[1, 5], [10, 10], [11, 20)}
        Assert.assertEquals("[[1..5], [10..10], [11..20)]", treeRangeSet.toString());
        treeRangeSet.add(Range.closed(10, 11));
        Assert.assertEquals("[[1..5], [10..20)]", treeRangeSet.toString());
        treeRangeSet.add(Range.greaterThan(21));
        Assert.assertEquals("[[1..5], [10..20), (21..+∞)]", treeRangeSet.toString());
        Set<Range<Integer>> ranges = treeRangeSet.asRanges();
        Assert.assertEquals("[[1..5], [10..20), (21..+∞)]", ranges.toString());
        // treeRangeSet的补集
        RangeSet<Integer> complement = treeRangeSet.complement();
        Assert.assertEquals("[(-∞..1), (5..10), [20..21]]", complement.toString());
        //rangeContaining - 返回包含指定元素的Range；如果没有，则返回null。
        Assert.assertEquals("[1..5]", treeRangeSet.rangeContaining(5).toString());
        //足够直接地，测试RangeSet中的任何Range是否包含指定范围。
        Assert.assertTrue(treeRangeSet.encloses(Range.closed(12, 15)));
        //返回包含此RangeSet中包含每个范围的最小Range。
        Range<Integer> span = treeRangeSet.span();
        Assert.assertEquals("[1..+∞)", span.toString());

    }

    /**
     * 泛型为键，对象为值
     */
    private static void testClassToInstanceMap() {
        MutableClassToInstanceMap<MapTestEntity> mapTestEntityMutableClassToInstanceMap = MutableClassToInstanceMap.<MapTestEntity>create();
        MapTestEntity testEntity = new MapTestEntity(22, "twenty twe");
        mapTestEntityMutableClassToInstanceMap.putInstance(MapTestEntity.class, testEntity);
        mapTestEntityMutableClassToInstanceMap.putInstance(MapTestEntity.class, new MapTestEntity(23, "twenty three"));
        mapTestEntityMutableClassToInstanceMap.putInstance(MapInstanceEntity.class, new MapInstanceEntity(23, "twenty three","33"));
        MapTestEntity instance = mapTestEntityMutableClassToInstanceMap.getInstance(MapTestEntity.class);
        MapTestEntity instance2 = mapTestEntityMutableClassToInstanceMap.getInstance(MapInstanceEntity.class);
        Assert.assertEquals("MapTestEntity(id=23, name=twenty three, sex=null, description=null)", instance.toString());
        Assert.assertEquals("MapInstanceEntity(favouriteMoive=33)", instance2.toString());

        mapTestEntityMutableClassToInstanceMap.put(MapInstanceEntity.class, new MapInstanceEntity(24, "twenty four", "肖申克的救赎"));
        Assert.assertTrue(new Integer(2).equals(mapTestEntityMutableClassToInstanceMap.size()));

        ImmutableClassToInstanceMap<MapTestEntity> immutableClassToInstanceMap = ImmutableClassToInstanceMap.copyOf(mapTestEntityMutableClassToInstanceMap);
        Assert.assertTrue(immutableClassToInstanceMap.getInstance(MapTestEntity.class).equals(mapTestEntityMutableClassToInstanceMap.getInstance(MapTestEntity.class)));
    }

    /**
     * 二维数组，对应坐标值为对象
     */
    private static void testTable() {
        Table<Integer, Integer, MapTestEntity> hashBasedTable = HashBasedTable.<Integer, Integer, MapTestEntity>create();
        hashBasedTable.put(0, 0, new MapTestEntity(0, "zero"));
        for (MapTestEntity entity : list) {
            hashBasedTable.put(entity.getId(), entity.getId(), entity);
        }
        Map<Integer, MapTestEntity> column = hashBasedTable.column(7);
        //{7=MapTestEntity(id=7, name=seven, sex=null, description=null)}
        Assert.assertTrue(new Integer(1).equals(column.size()));
        Assert.assertEquals("MapTestEntity(id=7, name=seven, sex=null, description=null)", list.get(1).toString());
        Map<Integer, MapTestEntity> row = hashBasedTable.row(7);
        Assert.assertTrue(new Integer(1).equals(row.size()));

        Assert.assertTrue(hashBasedTable.containsRow(6));
        Assert.assertTrue(hashBasedTable.containsColumn(7));

        Table<Integer, Integer, MapTestEntity> arrayTable = ArrayTable.<Integer, Integer, MapTestEntity>create(hashBasedTable);
        Set<Table.Cell<Integer, Integer, MapTestEntity>> cells = arrayTable.cellSet();
//        for (Table.Cell<Integer, Integer, MapTestEntity> cell : cells) {
//            System.out.println(String.format("row is %s, column is %s, result is %s.", cell.getRowKey(), cell.getColumnKey(), cell.getValue()));
//        }
        // 二维数组 中的点个数 放入11个值，row为：[0,6,7,8,9,10,11,12,13,14,15];column为：[0,6,7,8,9,10,11,12,13,14,15]共121个值（即11²）
        Assert.assertTrue(new Integer(121).equals(cells.size()));
        //
        Map<Integer, Map<Integer, MapTestEntity>> columnMap = hashBasedTable.columnMap();
        Assert.assertEquals("{0={0=MapTestEntity(id=0, name=zero, sex=null, description=null)}, 6={6=MapTestEntity(id=6, name=six, sex=null, description=null)}, " +
                        "7={7=MapTestEntity(id=7, name=seven, sex=null, description=null)}, 8={8=MapTestEntity(id=8, name=eight, sex=null, description=null)}, " +
                        "9={9=MapTestEntity(id=9, name=nine, sex=null, description=null)}, 10={10=MapTestEntity(id=10, name=ten, sex=null, description=null)}, " +
                        "11={11=MapTestEntity(id=11, name=eleven, sex=null, description=null)}, 12={12=MapTestEntity(id=12, name=twelve, sex=null, description=null)}, " +
                        "13={13=MapTestEntity(id=13, name=thirteen, sex=null, description=null)}, 14={14=MapTestEntity(id=14, name=fourteen, sex=null, description=null)}, " +
                        "15={15=MapTestEntity(id=15, name=fitteen, sex=null, description=null)}}",
                columnMap.toString());
        Map<Integer, Map<Integer, MapTestEntity>> rowMap = arrayTable.rowMap();
        Assert.assertTrue(new Integer(11).equals(rowMap.size()));
        Assert.assertTrue(new Integer(11).equals(rowMap.get(0).size()));

        TreeBasedTable<Integer, Integer, MapTestEntity> treeBasedTable = TreeBasedTable.<Integer, Integer, MapTestEntity>create();
        treeBasedTable.putAll(hashBasedTable);
        SortedSet<Integer> rowKeySet = treeBasedTable.rowKeySet();
        Assert.assertTrue(new Integer(11).equals(rowKeySet.size()));
        /**
         * ImmutableTable
         * 其他实现
         */
    }

    private static void testBiMap() {
        Map<String, MapTestEntity> hashMap = Maps.<String, MapTestEntity>newHashMap();
        for (MapTestEntity entity : list) {
            hashMap.put(entity.getName(), entity);
        }
        Map<String, MapTestEntity> hashBiMap = HashBiMap.<String, MapTestEntity>create(hashMap);
        // 替换值，如果旧值是null，则直接赋新值为最终值；如果不是旧值，则通过function操作返回最终值；如果最终值是null则移除该键
        hashBiMap.merge("seven", new MapTestEntity(27, "twenty five"), (oldValue, newValue) -> oldValue.getId() > newValue.getId()? oldValue:newValue);
        Assert.assertEquals("{nine=MapTestEntity(id=9, name=nine, sex=null, description=null), six=MapTestEntity(id=6, name=six, sex=null, description=null), " +
                        "twelve=MapTestEntity(id=12, name=twelve, sex=null, description=null), seven=MapTestEntity(id=27, name=twenty five, sex=null, description=null), " +
                        "eleven=MapTestEntity(id=11, name=eleven, sex=null, description=null), fitteen=MapTestEntity(id=15, name=fitteen, sex=null, description=null), " +
                        "ten=MapTestEntity(id=10, name=ten, sex=null, description=null), thirteen=MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "eight=MapTestEntity(id=8, name=eight, sex=null, description=null), fourteen=MapTestEntity(id=14, name=fourteen, sex=null, description=null)}",
                hashBiMap.toString());
//        Map<MapTestEntity, String> biMap = ((HashBiMap<String, MapTestEntity>) hashBiMap).inverse();
        /**
         * 其他实现
         * ImmutableBiMap
         * EnumBiMap
         * EnumHashBiMap
         */
    }

    private static void testMultimap() {
        Multimap<String, MapTestEntity> listMultimap = MultimapBuilder.hashKeys().arrayListValues().<String, MapTestEntity>build();
        Multimap<String, MapTestEntity> setMultimap = MultimapBuilder.treeKeys().hashSetValues().<String, MapTestEntity>build();
        for (MapTestEntity entity : list) {
            listMultimap.put(entity.getName(), entity);
            setMultimap.put(entity.getName(), entity);
        }
        Assert.assertEquals("{nine=[MapTestEntity(id=9, name=nine, sex=null, description=null)], six=[MapTestEntity(id=6, name=six, sex=null, description=null)], " +
                        "twelve=[MapTestEntity(id=12, name=twelve, sex=null, description=null)], seven=[MapTestEntity(id=7, name=seven, sex=null, description=null)], " +
                        "eleven=[MapTestEntity(id=11, name=eleven, sex=null, description=null)], fitteen=[MapTestEntity(id=15, name=fitteen, sex=null, description=null)], " +
                        "ten=[MapTestEntity(id=10, name=ten, sex=null, description=null)], thirteen=[MapTestEntity(id=13, name=thirteen, sex=null, description=null)], " +
                        "eight=[MapTestEntity(id=8, name=eight, sex=null, description=null)], fourteen=[MapTestEntity(id=14, name=fourteen, sex=null, description=null)]}",
                listMultimap.toString());
        Assert.assertEquals("{eight=[MapTestEntity(id=8, name=eight, sex=null, description=null)], eleven=[MapTestEntity(id=11, name=eleven, sex=null, description=null)], " +
                        "fitteen=[MapTestEntity(id=15, name=fitteen, sex=null, description=null)], fourteen=[MapTestEntity(id=14, name=fourteen, sex=null, description=null)], " +
                        "nine=[MapTestEntity(id=9, name=nine, sex=null, description=null)], seven=[MapTestEntity(id=7, name=seven, sex=null, description=null)], " +
                        "six=[MapTestEntity(id=6, name=six, sex=null, description=null)], ten=[MapTestEntity(id=10, name=ten, sex=null, description=null)], " +
                        "thirteen=[MapTestEntity(id=13, name=thirteen, sex=null, description=null)], twelve=[MapTestEntity(id=12, name=twelve, sex=null, description=null)]}",
                setMultimap.toString());
        listMultimap.put("eleven", new MapTestEntity(21, "eleven plus ten"));
        Assert.assertTrue(new Integer(11).equals(listMultimap.size()));
        Assert.assertTrue(new Integer(10).equals(listMultimap.keySet().size()));
        listMultimap.replaceValues("twelve", Lists.newArrayList(new MapTestEntity(22, "twelve plue ten")));
        Assert.assertTrue(new Integer(11).equals(listMultimap.size()));
        Assert.assertTrue(new Integer(10).equals(listMultimap.keySet().size()));
        Assert.assertEquals("{nine=[MapTestEntity(id=9, name=nine, sex=null, description=null)], six=[MapTestEntity(id=6, name=six, sex=null, description=null)], " +
                        "twelve=[MapTestEntity(id=22, name=twelve plue ten, sex=null, description=null)], seven=[MapTestEntity(id=7, name=seven, sex=null, description=null)], " +
                        "eleven=[MapTestEntity(id=11, name=eleven, sex=null, description=null), MapTestEntity(id=21, name=eleven plus ten, sex=null, description=null)], " +
                        "fitteen=[MapTestEntity(id=15, name=fitteen, sex=null, description=null)], ten=[MapTestEntity(id=10, name=ten, sex=null, description=null)], " +
                        "thirteen=[MapTestEntity(id=13, name=thirteen, sex=null, description=null)], eight=[MapTestEntity(id=8, name=eight, sex=null, description=null)], " +
                        "fourteen=[MapTestEntity(id=14, name=fourteen, sex=null, description=null)]}",
                listMultimap.toString());
        Map<String, Collection<MapTestEntity>> asMap = listMultimap.asMap();
        Assert.assertEquals("{nine=[MapTestEntity(id=9, name=nine, sex=null, description=null)], six=[MapTestEntity(id=6, name=six, sex=null, description=null)], " +
                        "twelve=[MapTestEntity(id=22, name=twelve plue ten, sex=null, description=null)], seven=[MapTestEntity(id=7, name=seven, sex=null, description=null)], " +
                        "eleven=[MapTestEntity(id=11, name=eleven, sex=null, description=null), MapTestEntity(id=21, name=eleven plus ten, sex=null, description=null)], " +
                        "fitteen=[MapTestEntity(id=15, name=fitteen, sex=null, description=null)], ten=[MapTestEntity(id=10, name=ten, sex=null, description=null)], " +
                        "thirteen=[MapTestEntity(id=13, name=thirteen, sex=null, description=null)], eight=[MapTestEntity(id=8, name=eight, sex=null, description=null)], " +
                        "fourteen=[MapTestEntity(id=14, name=fourteen, sex=null, description=null)]}",
                asMap.toString());

        Multimap<String, MapTestEntity> arrayListMultimap = ArrayListMultimap.<String, MapTestEntity>create(setMultimap);
        Assert.assertTrue(arrayListMultimap.containsKey("eleven"));

        Multimap<String, MapTestEntity> hashMultimap = HashMultimap.<String, MapTestEntity>create(listMultimap);
        hashMultimap.remove("eleven", list.get(5));
        Assert.assertTrue(new Integer(10).equals(hashMultimap.size()));
        hashMultimap.removeAll("eleven");
        Assert.assertTrue(new Integer(9).equals(hashMultimap.size()));

        /*
        *  其他实现
        *  LinkedListMultimap
        *  LinkedHashMultimap
        * TreeMultimap
        *  ImmutableListMultimap
        *  ImmutableSetMultimap
        * */
    }

    private static void testMultiset() {
        HashMultiset<MapTestEntity> hashMultiset = HashMultiset.<MapTestEntity>create();
        hashMultiset.add(new MapTestEntity(1, "first"));
        hashMultiset.add(new MapTestEntity(2, "second"));
        hashMultiset.add(new MapTestEntity(3, "third"));
        hashMultiset.add(new MapTestEntity(4, "four"));
        hashMultiset.add(new MapTestEntity(5,"five"), 3);
        hashMultiset.addAll(list);
        Assert.assertTrue(new Integer(17).equals(hashMultiset.size()));
        Assert.assertTrue(new Integer(15).equals(hashMultiset.elementSet().size()));

        HashMultiset<MapTestEntity> hashMultiset1 = HashMultiset.<MapTestEntity>create(list);
        MapTestEntity seveteen = new MapTestEntity(17, "seveteen");
        hashMultiset1.setCount(seveteen, 5);
        Assert.assertTrue(new Integer(15).equals(hashMultiset1.size()));
        hashMultiset1.setCount(seveteen, 3);
        Assert.assertTrue(new Integer(13).equals(hashMultiset1.size()));

        TreeMultiset<MapTestEntity> treeMultiset = TreeMultiset.<MapTestEntity>create();
        MapTestEntity eighteen = new MapTestEntity(18, "eighteen");
        treeMultiset.add(eighteen);
        treeMultiset.addAll(list);
        Assert.assertTrue(new Integer(11).equals(treeMultiset.size()));
        Assert.assertTrue(natural.isOrdered(treeMultiset));
        Assert.assertTrue(reverse.isOrdered(treeMultiset.descendingMultiset()));
        Assert.assertTrue(list.get(0).equals(treeMultiset.firstEntry().getElement()));

        // 如果不使用create -- 操作headMultiset即为操作treeMultiset
        SortedMultiset<MapTestEntity> headMultiset = TreeMultiset.<MapTestEntity>create(treeMultiset.headMultiset(eighteen, BoundType.CLOSED));
        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), " +
                        "MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null), " +
                        "MapTestEntity(id=18, name=eighteen, sex=null, description=null)]",
                headMultiset.toString());
        SortedMultiset<MapTestEntity> headMultiset1 = treeMultiset.headMultiset(list.get(2), BoundType.CLOSED);
        Assert.assertTrue(new Integer(3).equals(headMultiset1.size()));
        SortedMultiset<MapTestEntity> tailMultiset = TreeMultiset.<MapTestEntity>create(treeMultiset.tailMultiset(list.get(2), BoundType.CLOSED));
        Assert.assertEquals("[MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null), " +
                        "MapTestEntity(id=18, name=eighteen, sex=null, description=null)]",
                tailMultiset.toString());
        SortedMultiset<MapTestEntity> subMultiset = treeMultiset.subMultiset(list.get(2), BoundType.CLOSED, list.get(5), BoundType.CLOSED);
        Assert.assertEquals("[MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null)]",
                subMultiset.toString());

        LinkedHashMultiset<MapTestEntity> linkedHashMultiset = LinkedHashMultiset.<MapTestEntity>create();
        linkedHashMultiset.addAll(list);
        linkedHashMultiset.add(new MapTestEntity(19, "nineteen"));
        MapTestEntity twenty = new MapTestEntity(20, "twenty");
        linkedHashMultiset.setCount(twenty, 3);
        Assert.assertTrue(new Integer(14).equals(linkedHashMultiset.size()));
        Assert.assertTrue(new Integer(12).equals(linkedHashMultiset.elementSet().size()));
        //保留列表中的数据
        tailMultiset.retainAll(list);
        linkedHashMultiset.retainAll(list);
        Assert.assertEquals("[MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null)]",
                tailMultiset.toString());
        Assert.assertTrue(new Integer(0).equals(linkedHashMultiset.count(twenty)));
        headMultiset.removeAll(list);
        Assert.assertEquals("[MapTestEntity(id=18, name=eighteen, sex=null, description=null)]", headMultiset.toString());

        Multiset<MapTestEntity> concurrentHashMultiset = ConcurrentHashMultiset.<MapTestEntity>create();
        concurrentHashMultiset.addAll(list);
        concurrentHashMultiset.removeAll(list);
        Assert.assertTrue(new Integer(0).equals(concurrentHashMultiset.size()));

        Multiset<MapTestEntity> immutableMultiset = ImmutableMultiset.<MapTestEntity>copyOf(linkedHashMultiset);
        //        copy.add(23);  -- java.lang.UnsupportedOperationException
//        list.add(33); -- java.lang.UnsupportedOperationException
        Assert.assertTrue(new Integer(10).equals(immutableMultiset.size()));
        Assert.assertTrue(natural.isOrdered(immutableMultiset));
        Multiset<MapTestEntity> toImmutableMultiset = list.stream().filter(mapTestEntity -> mapTestEntity.getId() != null).collect(ImmutableMultiset.toImmutableMultiset());
        Assert.assertTrue(new Integer(10).equals(toImmutableMultiset.size()));

    }
}
