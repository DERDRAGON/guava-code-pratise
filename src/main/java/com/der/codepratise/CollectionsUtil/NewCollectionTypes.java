package com.der.codepratise.CollectionsUtil;

import com.der.codepratise.entity.MapTestEntity;
import com.google.common.collect.*;
import org.assertj.core.util.Lists;
import org.junit.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    }

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
        for (Table.Cell<Integer, Integer, MapTestEntity> cell : cells) {
            System.out.println(String.format("row is %s, column is %s, result is %s.", cell.getRowKey(), cell.getColumnKey(), cell.getValue()));
        }
        // 二维数组 中的点个数 放入11个值，row为：[0,6,7,8,9,10,11,12,13,14,15];column为：[0,6,7,8,9,10,11,12,13,14,15]共121个值（即11²）
        Assert.assertTrue(new Integer(121).equals(cells.size()));
        //未完待续

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
        Assert.assertTrue(new Integer(10).equals(immutableMultiset.size()));
        Assert.assertTrue(natural.isOrdered(immutableMultiset));
        Multiset<MapTestEntity> toImmutableMultiset = list.stream().filter(mapTestEntity -> mapTestEntity.getId() != null).collect(ImmutableMultiset.toImmutableMultiset());
        Assert.assertTrue(new Integer(10).equals(toImmutableMultiset.size()));
    }
}
