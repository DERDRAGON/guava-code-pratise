package com.der.codepratise.CollectionsUtil;

import com.der.codepratise.entity.MapInstanceEntity;
import com.der.codepratise.entity.MapTestEntity;
import com.der.codepratise.enums.DerTestEnum;
import com.google.common.base.Function;
import com.google.common.collect.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author K0790016
 **/
public class CollectionUtilities {

    private static final Ordering natural = Ordering.natural();

    private static final Ordering reverse = natural.reverse();

    private static final List<MapTestEntity> list = Lists.<MapTestEntity>newArrayList(new MapTestEntity(6, "six"),
            new MapTestEntity(7, "seven"), new MapTestEntity(8, "eight"), new MapTestEntity(9, "nine"),
            new MapTestEntity(10, "ten"), new MapTestEntity(11, "eleven"), new MapTestEntity(12, "twelve"),
            new MapTestEntity(13, "thirteen"), new MapTestEntity(14, "fourteen"), new MapTestEntity(15, "fitteen"));

    private static final List<MapTestEntity> list2 = Lists.<MapTestEntity>newArrayList(new MapTestEntity(22, "twenty twe"),
            new MapTestEntity(23, "twenty three"), new MapTestEntity(27, "twenty five"), new MapTestEntity(1, "first"),
            new MapTestEntity(2, "second"), new MapTestEntity(3, "third"), new MapTestEntity(4, "four"),
            new MapTestEntity(5,"five"));

    private static final Ordering<MapTestEntity> order = Ordering.from(MapTestEntity::compareTo);

    public static void main(String[] args) {
        testCollections2();
        testLists();
        testSets();
        testMaps();
        testQueues();
        testMultisets();
    }

    private static void testMultisets() {
        HashMultiset<MapTestEntity> hashMultiset = HashMultiset.<MapTestEntity>create(list);
        HashMultiset<MapTestEntity> hashMultiset2 = HashMultiset.<MapTestEntity>create(list2);
        Multiset<MapTestEntity> union = Multisets.union(hashMultiset, hashMultiset2);
        Assert.assertTrue(Multisets.containsOccurrences(union, hashMultiset));
    }

    private static void testQueues() {
        ArrayDeque<MapTestEntity> arrayDeque = Queues.<MapTestEntity>newArrayDeque();
        ArrayDeque<MapTestEntity> arrayDeque1 = Queues.<MapTestEntity>newArrayDeque(list);
        arrayDeque.addAll(list);
        Assert.assertTrue(Integer.valueOf(arrayDeque.size()).equals(arrayDeque1.size()));
        MapTestEntity poll = arrayDeque.poll();
        Assert.assertEquals("MapTestEntity(id=6, name=six, sex=null, description=null)", poll.toString());
        try {
            ArrayBlockingQueue<MapTestEntity> arrayBlockingQueue = Queues.<MapTestEntity>newArrayBlockingQueue(10);
            arrayBlockingQueue.addAll(list);
            List<MapTestEntity> arrayList = Lists.<MapTestEntity>newArrayList();
            int drain = Queues.<MapTestEntity>drain(arrayBlockingQueue, arrayList, 4, Duration.ofMillis(1000));
            Assert.assertTrue(Integer.valueOf(4).equals(drain));
            Assert.assertTrue(Integer.valueOf(6).equals(arrayBlockingQueue.size()));
            Assert.assertTrue(Integer.valueOf(4).equals(arrayList.size()));

            int drain1 = Queues.drain(arrayBlockingQueue, arrayList, 4, 1000, TimeUnit.MILLISECONDS);
            Assert.assertTrue(Integer.valueOf(4).equals(drain1));
            Assert.assertTrue(Integer.valueOf(2).equals(arrayBlockingQueue.size()));
            Assert.assertTrue(Integer.valueOf(8).equals(arrayList.size()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayBlockingQueue<MapTestEntity> arrayBlockingQueue = Queues.<MapTestEntity>newArrayBlockingQueue(10);
        arrayBlockingQueue.addAll(list);
        ArrayList<MapTestEntity> arrayList = Lists.<MapTestEntity>newArrayList();
        int drainUninterruptibly = Queues.drainUninterruptibly(arrayBlockingQueue, arrayList, 5, Duration.ofSeconds(1l));
        Assert.assertTrue(Integer.valueOf(5).equals(drainUninterruptibly));
        Assert.assertTrue(Integer.valueOf(5).equals(arrayBlockingQueue.size()));
        Assert.assertTrue(Integer.valueOf(5).equals(arrayList.size()));

        ConcurrentLinkedQueue<MapTestEntity> concurrentLinkedQueue = Queues.<MapTestEntity>newConcurrentLinkedQueue();
        list2.parallelStream().forEach(mapTestEntity -> concurrentLinkedQueue.add(mapTestEntity));
        MapTestEntity peek = concurrentLinkedQueue.peek();
        Assert.assertFalse(list2.get(list2.size() - 1).equals(peek));

        LinkedBlockingDeque<MapTestEntity> linkedBlockingDeque = Queues.<MapTestEntity>newLinkedBlockingDeque(10);
        linkedBlockingDeque.addAll(list2);
        linkedBlockingDeque.addLast(list.get(0));
        List<MapTestEntity> sortedCopy = order.sortedCopy(linkedBlockingDeque);
        linkedBlockingDeque.clear();
        linkedBlockingDeque.addAll(sortedCopy);
        Iterator<MapTestEntity> descendingIterator = linkedBlockingDeque.descendingIterator();

        LinkedBlockingDeque<MapTestEntity> linkedBlockingDeque1 = Queues.<MapTestEntity>newLinkedBlockingDeque();
        descendingIterator.forEachRemaining(entity -> linkedBlockingDeque1.addLast(entity));

        Assert.assertTrue(Integer.valueOf(9).equals(linkedBlockingDeque.size()));
        Assert.assertTrue(Integer.valueOf(9).equals(linkedBlockingDeque1.size()));
        Assert.assertTrue(reverse.isOrdered(linkedBlockingDeque1));

        PriorityBlockingQueue<MapTestEntity> priorityBlockingQueue = Queues.<MapTestEntity>newPriorityBlockingQueue();
        priorityBlockingQueue.addAll(list);
        priorityBlockingQueue.addAll(list2);
        ArrayList<MapTestEntity> mapTestEntities = Lists.<MapTestEntity>newArrayList();
        priorityBlockingQueue.drainTo(mapTestEntities);
        Assert.assertTrue(order.isOrdered(mapTestEntities));

//        Queues.<MapTestEntity>newSynchronousQueue(); -- 创建一个具有不公平访问策略的空SynchronousQueue。

//        Deque<MapTestEntity> synchronizedDeque = Queues.synchronizedDeque(arrayDeque); -- 返回由指定双端队列支持的同步（线程安全）双端队列。

//        Queues.synchronizedQueue() -- 返回由指定队列支持的同步（线程安全）队列。


    }

    private static void testMaps() {
        Map<String, MapTestEntity> hashMap = Maps.<String, MapTestEntity>newHashMap();
        for (MapTestEntity entity : list) {
            hashMap.put(entity.getName(), entity);
        }
        Assert.assertTrue(Integer.valueOf(10).equals(hashMap.size()));

        //返回一个实时Map视图，其键是set的内容，其值是使用函数按需计算的。
        Map<String, MapTestEntity> asMap = Maps.<String, MapTestEntity>asMap(Sets.<String>newHashSet(list.stream().map(entity -> entity.getName()).collect(Collectors.toList())), str -> new MapTestEntity(1, str));
        Assert.assertEquals("{nine=MapTestEntity(id=1, name=nine, sex=null, description=null), six=MapTestEntity(id=1, name=six, sex=null, description=null), " +
                        "twelve=MapTestEntity(id=1, name=twelve, sex=null, description=null), seven=MapTestEntity(id=1, name=seven, sex=null, description=null), " +
                        "eleven=MapTestEntity(id=1, name=eleven, sex=null, description=null), fitteen=MapTestEntity(id=1, name=fitteen, sex=null, description=null), " +
                        "ten=MapTestEntity(id=1, name=ten, sex=null, description=null), thirteen=MapTestEntity(id=1, name=thirteen, sex=null, description=null), " +
                        "eight=MapTestEntity(id=1, name=eight, sex=null, description=null), fourteen=MapTestEntity(id=1, name=fourteen, sex=null, description=null)}",
                asMap.toString());

        MapDifference<String, MapTestEntity> mapDifference = Maps.<String, MapTestEntity>difference(hashMap, asMap);
        Assert.assertFalse(mapDifference.areEqual());
        Assert.assertEquals("not equal: value differences={nine=(MapTestEntity(id=9, name=nine, sex=null, description=null), MapTestEntity(id=1, name=nine, sex=null, description=null)), " +
                        "six=(MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=1, name=six, sex=null, description=null)), " +
                        "twelve=(MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=1, name=twelve, sex=null, description=null)), " +
                        "seven=(MapTestEntity(id=7, name=seven, sex=null, description=null), MapTestEntity(id=1, name=seven, sex=null, description=null)), " +
                        "eleven=(MapTestEntity(id=11, name=eleven, sex=null, description=null), MapTestEntity(id=1, name=eleven, sex=null, description=null)), " +
                        "fitteen=(MapTestEntity(id=15, name=fitteen, sex=null, description=null), MapTestEntity(id=1, name=fitteen, sex=null, description=null)), " +
                        "ten=(MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=1, name=ten, sex=null, description=null)), " +
                        "thirteen=(MapTestEntity(id=13, name=thirteen, sex=null, description=null), MapTestEntity(id=1, name=thirteen, sex=null, description=null)), " +
                        "eight=(MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=1, name=eight, sex=null, description=null)), " +
                        "fourteen=(MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=1, name=fourteen, sex=null, description=null))}",
                mapDifference.toString());

        Map<String, MapTestEntity> filterEntries = Maps.<String, MapTestEntity>filterEntries(hashMap, entity -> entity.getValue().getId() < 10);
        Assert.assertEquals("{nine=MapTestEntity(id=9, name=nine, sex=null, description=null), six=MapTestEntity(id=6, name=six, sex=null, description=null), " +
                        "seven=MapTestEntity(id=7, name=seven, sex=null, description=null), eight=MapTestEntity(id=8, name=eight, sex=null, description=null)}",
                filterEntries.toString());

        Map<String, MapTestEntity> filterKeys = Maps.<String, MapTestEntity>filterKeys(hashMap, key -> key.length() > 5);
//        Assert.assertEquals("{twelve=MapTestEntity(id=12, name=twelve, sex=null, description=null), eleven=MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
//                        "fitteen=MapTestEntity(id=15, name=fitteen, sex=null, description=null), thirteen=MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
//                        "fourteen=MapTestEntity(id=14, name=fourteen, sex=null, description=null)}",
//                filterKeys.toString());
        Assert.assertTrue(Integer.valueOf(5).equals(filterKeys.size()));

        Map<String, MapTestEntity> filterValues = Maps.filterValues(hashMap, val -> val.getId() > 7);
//        Assert.assertEquals("{nine=MapTestEntity(id=9, name=nine, sex=null, description=null), twelve=MapTestEntity(id=12, name=twelve, sex=null, description=null), " +
//                        "eleven=MapTestEntity(id=11, name=eleven, sex=null, description=null), fitteen=MapTestEntity(id=15, name=fitteen, sex=null, description=null), " +
//                        "ten=MapTestEntity(id=10, name=ten, sex=null, description=null), thirteen=MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
//                        "eight=MapTestEntity(id=8, name=eight, sex=null, description=null), fourteen=MapTestEntity(id=14, name=fourteen, sex=null, description=null)}",
//                filterValues.toString());
        Assert.assertTrue(Integer.valueOf(8).equals(filterValues.size()));

        try {
            Properties properties = new Properties();
            InputStream asStream = CollectionUtilities.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(asStream);
            ImmutableMap<String, String> fromProperties = Maps.fromProperties(properties);
//            Assert.assertEquals("{master.datasource.password=csl129, " +
//                            "second.datasource.driverClassName=com.mysql.jdbc.Driver, " +
//                            "second.datasource.url=jdbc:mysql://localhost:3306/slave?useUnicode=true&characterEncoding=utf8, " +
//                            "second.datasource.username=root, master.datasource.driverClassName=com.mysql.cj.jdbc.Driver, " +
//                            "second.datasource.password=csl129, mybatis.typeAliasesPackage=com.der.miutisourceb.entity, " +
//                            "mybatis.mapperLocations=classpath:mybatis/mapper/*.xml, " +
//                            "master.datasource.url=jdbc:mysql://localhost:3306/master?useUnicode=true&characterEncoding=utf8, " +
//                            "master.datasource.username=root}",
//                    fromProperties.toString());
            Assert.assertTrue(Integer.valueOf(10).equals(fromProperties.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map.Entry<String, MapTestEntity> immutableEntry = Maps.immutableEntry("test", new MapTestEntity(30, "thirty"));
        MapTestEntity entity = immutableEntry.getValue();
        Assert.assertEquals("MapTestEntity(id=30, name=thirty, sex=null, description=null)", entity.toString());

        EnumMap<DerTestEnum, String> enumMap = Maps.newEnumMap(DerTestEnum.class);
        Lists.newArrayList(DerTestEnum.values()).forEach(derTestEnum -> enumMap.put(derTestEnum, derTestEnum.getName()));
        ImmutableMap<DerTestEnum, Object> immutableEnumMap = Maps.immutableEnumMap(enumMap);
        Assert.assertEquals("{LI_RUI=李睿, LIU_WEI=刘威, SHILONG=世龙}", immutableEnumMap.toString());

        LinkedHashMap<String, MapTestEntity> linkedHashMap = Maps.newLinkedHashMap(hashMap);
        Assert.assertTrue(Integer.valueOf(10).equals(linkedHashMap.size()));

        TreeMap<String, MapTestEntity> treeMap = Maps.<String, MapTestEntity>newTreeMap();
        treeMap.putAll(hashMap);
        SortedMap<String, MapTestEntity> subMap = treeMap.subMap("seven", "six");

        NavigableMap<String, MapTestEntity> subMap1 = Maps.subMap(treeMap, Range.closedOpen("seven", "six"));
        Assert.assertTrue(subMap.equals(subMap1));

        //不会用
//        Maps.<DerTestEnum, String>toImmutableEnumMap()

        ImmutableMap<MapTestEntity, String> toMap = Maps.<MapTestEntity, String>toMap(list, mapTestEntity -> mapTestEntity.getName());
        Assert.assertTrue(Integer.valueOf(10).equals(toMap.size()));

//        Maps.transformEntries()

        Map<String, Object> transformValues = Maps.transformValues(hashMap, new Function<MapTestEntity, Object>() {
            @Override
            public Object apply(@Nullable MapTestEntity mapTestEntity) {
                return mapTestEntity;
            }
        });
        Assert.assertTrue(transformValues.equals(hashMap));

        ImmutableMap<Integer, MapTestEntity> uniqueIndex = Maps.uniqueIndex(list, mapTestEntity -> mapTestEntity.getId());
        Assert.assertEquals("{6=MapTestEntity(id=6, name=six, sex=null, description=null), 7=MapTestEntity(id=7, name=seven, sex=null, description=null), " +
                        "8=MapTestEntity(id=8, name=eight, sex=null, description=null), 9=MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "10=MapTestEntity(id=10, name=ten, sex=null, description=null), 11=MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "12=MapTestEntity(id=12, name=twelve, sex=null, description=null), 13=MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "14=MapTestEntity(id=14, name=fourteen, sex=null, description=null), 15=MapTestEntity(id=15, name=fitteen, sex=null, description=null)}",
                uniqueIndex.toString());

//        Maps.unmodifiableBiMap()
//        Maps.unmodifiableNavigableMap()
    }

    private static void testSets() {
        Set<MapTestEntity> hashSet = Sets.<MapTestEntity>newHashSet();
        hashSet.addAll(list);
        Assert.assertTrue(Integer.valueOf(10).equals(hashSet.size()));

        Set<List<MapTestEntity>> cartesianProduct = Sets.<MapTestEntity>cartesianProduct(hashSet);
        for (List<MapTestEntity> entityList : cartesianProduct) {
            Assert.assertTrue(Integer.valueOf(1).equals(entityList.size()));
        }

        Set<Set<MapTestEntity>> combinations = Sets.combinations(hashSet, 3);
        Assert.assertTrue(Integer.valueOf(120).equals(combinations.size()));

        ImmutableSet<DerTestEnum> immutableEnumSet = Sets.immutableEnumSet(DerTestEnum.LI_RUI);
        EnumSet<DerTestEnum> complementOf = Sets.complementOf(immutableEnumSet);
        Assert.assertEquals("[LIU_WEI, SHILONG]", complementOf.toString());
        EnumSet<DerTestEnum> complementOf1 = Sets.complementOf(immutableEnumSet, DerTestEnum.class);
        Assert.assertEquals("[LIU_WEI, SHILONG]", complementOf1.toString());

        Set<MapTestEntity> newHashSet = Sets.<MapTestEntity>newHashSet();
        // difference返回两组差异的不可更改视图。-- hashSet里跟参数2Set的不同的值
        Sets.SetView<MapTestEntity> difference = Sets.difference(hashSet, Sets.newHashSet(list2));
        Set<MapTestEntity> copyInto = difference.copyInto(newHashSet);
        //toString 还能偶尔不一样?
        // 不同的jdk set的toString实现不同
//        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=8, name=eight, sex=null, description=null), " +
//                        "MapTestEntity(id=11, name=eleven, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
//                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), " +
//                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null), " +
//                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null)]",
//                newHashSet.toString());
        Assert.assertTrue(Integer.valueOf(10).equals(newHashSet.size()));
        Assert.assertTrue(copyInto.equals(newHashSet));
        HashSet<MapTestEntity> copyInto1 = Sets.difference(hashSet, Sets.newHashSet(list.get(0), list.get(2))).copyInto(Sets.newHashSet());
//        Assert.assertEquals("[MapTestEntity(id=11, name=eleven, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
//                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), " +
//                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null), " +
//                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null)]",
//                copyInto1.toString());
        Assert.assertTrue(Integer.valueOf(8).equals(copyInto1.size()));
        // 返回符合过滤条件的实例
        Set<MapTestEntity> filter = Sets.filter(newHashSet, map -> map.getId() < 10);
//        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=8, name=eight, sex=null, description=null), " +
//                        "MapTestEntity(id=9, name=nine, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null)]",
//                filter.toString());
        Assert.assertTrue(Integer.valueOf(4).equals(filter.size()));

        // 取两个set的交集
        ImmutableSet<MapTestEntity> immutableCopy = Sets.intersection(hashSet, complementOf).immutableCopy();
        Assert.assertTrue(Integer.valueOf(0).equals(immutableCopy.size()));

        //返回两个集合的交集的不可修改的视图。
        ImmutableSet<MapTestEntity> intersection = Sets.intersection(hashSet, filter).immutableCopy();
//        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=8, name=eight, sex=null, description=null), " +
//                        "MapTestEntity(id=9, name=nine, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null)]",
//                intersection.toString());
        Assert.assertTrue(Integer.valueOf(4).equals(intersection.size()));

        Set<MapTestEntity> concurrentHashSet = Sets.<MapTestEntity>newConcurrentHashSet();
        Assert.assertTrue(concurrentHashSet.addAll(newHashSet));

        CopyOnWriteArraySet<MapTestEntity> copyOnWriteArraySet = Sets.newCopyOnWriteArraySet(hashSet);
        copyOnWriteArraySet.retainAll(filter);
//        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=8, name=eight, sex=null, description=null), " +
//                        "MapTestEntity(id=9, name=nine, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null)]",
//                copyOnWriteArraySet.toString());
        Assert.assertTrue(Integer.valueOf(4).equals(copyOnWriteArraySet.size()));

        EnumSet<DerTestEnum> enumEnumSet = Sets.newEnumSet(immutableEnumSet, DerTestEnum.class);
        Assert.assertEquals("[LI_RUI]", enumEnumSet.toString());
        enumEnumSet.add(DerTestEnum.LIU_WEI);
        Assert.assertTrue(Integer.valueOf(2).equals(enumEnumSet.size()));

        HashSet<MapTestEntity> hashSetWithExpectedSize = Sets.<MapTestEntity>newHashSetWithExpectedSize(6);
        hashSetWithExpectedSize.addAll(list);
        Assert.assertTrue(Integer.valueOf(10).equals(hashSetWithExpectedSize.size()));

        //创建一个空集合，该集合使用身份确定相等性。
        //它比较对象引用，而不是调用equals，以确定提供的对象是否与集合中的元素匹配。
        //例如，当传递的对象等于set成员但不是同一实例时，contains返回false。
        //此行为类似于IdentityHashMap处理键查找的方式。
        Set<MapTestEntity> identityHashSet = Sets.<MapTestEntity>newIdentityHashSet();
        Set<MapTestEntity> identityHashSet2 = Sets.<MapTestEntity>newIdentityHashSet();
        Set<MapTestEntity> identityHashSet3 = Sets.<MapTestEntity>newIdentityHashSet();
        MapTestEntity entity = new MapTestEntity(333, "333");
        MapTestEntity entity1 = new MapTestEntity(333, "333 too");
        identityHashSet.add(entity);
        identityHashSet2.add(entity1);
        identityHashSet3.add(entity);
        Assert.assertFalse(identityHashSet.equals(identityHashSet2));
        Assert.assertTrue(identityHashSet.equals(identityHashSet3));

        //大同小异
//        LinkedHashSet<MapTestEntity> linkedHashSet = Sets.<MapTestEntity>newLinkedHashSet();

        //泛型需要继承java.lang.Comparable
        TreeSet<MapTestEntity> comparableTreeSet = Sets.<MapTestEntity>newTreeSet();
        comparableTreeSet.addAll(list);
        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), " +
                        "MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null)]",
                comparableTreeSet.toString());
        TreeSet<MapTestEntity> treeSet = Sets.newTreeSet(MapTestEntity::compareTo);
        List<MapInstanceEntity> transform = Lists.transform(list, mapTestEntity -> new MapInstanceEntity(mapTestEntity.getId(), mapTestEntity.getName(), "movie" + mapTestEntity.getId()));
        treeSet.addAll(transform);
        Assert.assertEquals("[MapInstanceEntity(favouriteMoive=movie6), MapInstanceEntity(favouriteMoive=movie7), " +
                        "MapInstanceEntity(favouriteMoive=movie8), MapInstanceEntity(favouriteMoive=movie9), MapInstanceEntity(favouriteMoive=movie10), " +
                        "MapInstanceEntity(favouriteMoive=movie11), MapInstanceEntity(favouriteMoive=movie12), MapInstanceEntity(favouriteMoive=movie13), " +
                        "MapInstanceEntity(favouriteMoive=movie14), MapInstanceEntity(favouriteMoive=movie15)]",
                transform.toString());

        Set<Set<MapTestEntity>> powerSet = Sets.<MapTestEntity>powerSet(treeSet);
        Assert.assertEquals("powerSet({MapInstanceEntity(favouriteMoive=movie6)=0, MapInstanceEntity(favouriteMoive=movie7)=1, " +
                        "MapInstanceEntity(favouriteMoive=movie8)=2, MapInstanceEntity(favouriteMoive=movie9)=3, MapInstanceEntity(favouriteMoive=movie10)=4, " +
                        "MapInstanceEntity(favouriteMoive=movie11)=5, MapInstanceEntity(favouriteMoive=movie12)=6, MapInstanceEntity(favouriteMoive=movie13)=7, " +
                        "MapInstanceEntity(favouriteMoive=movie14)=8, MapInstanceEntity(favouriteMoive=movie15)=9})",
                powerSet.toString());

        //返回集合的一部分的视图，其元素按范围包含。 太麻烦了，quit这部分
//        static <K extends Comparable<? super K>> NavigableSet<K>	subSet(NavigableSet<K> set, Range<K> range)

        // 返回由指定的导航集支持的同步（线程安全）导航集。太麻烦了，quit这部分
//        static <E> NavigableSet<E>	synchronizedNavigableSet(NavigableSet<E> navigableSet)

        // union - 返回两个集合的不可修改的视图。
        HashSet<MapTestEntity> copyInto2 = Sets.<MapTestEntity>union(hashSet, Sets.newHashSet(list2)).copyInto(Sets.newHashSet());
        Assert.assertTrue(Integer.valueOf(18).equals(list.size() + list2.size()));

        // 返回指定导航集的不可修改视图。
//        static <E> NavigableSet<E>	unmodifiableNavigableSet(NavigableSet<E> set)

    }

    private static void testLists() {
        List<MapTestEntity> asList = Lists.<MapTestEntity>asList(new MapTestEntity(28, "twenty eight"), list.toArray(new MapTestEntity[10]));
        Assert.assertTrue(Integer.valueOf(11).equals(asList.size()));
        List<List<MapTestEntity>> lists = Lists.cartesianProduct(list);
        Assert.assertEquals("[[MapTestEntity(id=6, name=six, sex=null, description=null)], [MapTestEntity(id=7, name=seven, sex=null, description=null)], " +
                        "[MapTestEntity(id=8, name=eight, sex=null, description=null)], [MapTestEntity(id=9, name=nine, sex=null, description=null)], " +
                        "[MapTestEntity(id=10, name=ten, sex=null, description=null)], [MapTestEntity(id=11, name=eleven, sex=null, description=null)], " +
                        "[MapTestEntity(id=12, name=twelve, sex=null, description=null)], [MapTestEntity(id=13, name=thirteen, sex=null, description=null)], " +
                        "[MapTestEntity(id=14, name=fourteen, sex=null, description=null)], [MapTestEntity(id=15, name=fitteen, sex=null, description=null)]]",
                lists.toString());
        List<List<MapTestEntity>> cartesianProduct = Lists.cartesianProduct(list, list2);
        Assert.assertTrue(Integer.valueOf(80).equals(cartesianProduct.size()));

        List<MapInstanceEntity> transform = Lists.transform(list, mapTestEntity -> new MapInstanceEntity(mapTestEntity.getId(), mapTestEntity.getName(), "movie" + mapTestEntity.getId()));
        Assert.assertTrue(Integer.valueOf(10).equals(transform.size()));

        //charactersOf(String string) -- 返回指定字符串的视图，作为字符值的不可变列表。
        ImmutableList<Character> characters = Lists.charactersOf("232323232");
        Assert.assertEquals("[2, 3, 2, 3, 2, 3, 2, 3, 2]", characters.toString());
        ImmutableList<Character> characters2 = Lists.charactersOf("jsfkl sfjdksldffj fsdf ");
        Assert.assertEquals("[j, s, f, k, l,  , s, f, j, d, k, s, l, d, f, f, j,  , f, s, d, f,  ]", characters2.toString());
        HashSet<Character> hashSet = characters2.stream().filter(ch -> !(' ' == ch)).collect(Collectors.toCollection(HashSet::new));
        Assert.assertEquals("[s, d, f, j, k, l]", hashSet.toString());

        //charactersOf(CharSequence sequence) - 以List <Character>返回指定CharSequence的视图，将序列作为Unicode代码单元的序列查看。
        //可变List 当sb发生变化的时候list随之发生变化
        StringBuilder sb = new StringBuilder("232323232");
        List<Character> characters1 = Lists.charactersOf(sb);
        Assert.assertTrue(Integer.valueOf(9).equals(characters1.size()));
        sb.append(666);
        Assert.assertTrue(Integer.valueOf(12).equals(characters1.size()));

        List<MapTestEntity> newArrayList = Lists.newArrayList(list);
        Assert.assertTrue(Integer.valueOf(10).equals(newArrayList.size()));

        List<MapTestEntity> listWithCapacity = Lists.<MapTestEntity>newArrayListWithCapacity(8);
        listWithCapacity.addAll(list);
        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), " +
                        "MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null)]",
                listWithCapacity.toString());

        List<MapTestEntity> listWithExpectedSize = Lists.<MapTestEntity>newArrayListWithExpectedSize(8);
        listWithExpectedSize.addAll(list2);
        Assert.assertTrue(Integer.valueOf(8).equals(listWithExpectedSize.size()));
        List<MapTestEntity> listWithExpectedSize1 = Lists.<MapTestEntity>newArrayListWithExpectedSize(8);
        listWithExpectedSize1.addAll(list);
        Assert.assertTrue(Integer.valueOf(10).equals(listWithExpectedSize1.size()));

        List<MapTestEntity> copyOnWriteArrayList = Lists.newCopyOnWriteArrayList(list);
        Assert.assertTrue(Integer.valueOf(10).equals(copyOnWriteArrayList.size()));

        LinkedList<MapTestEntity> newLinkedList = Lists.newLinkedList(listWithCapacity);
        newLinkedList.addLast(list2.get(0));
        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), " +
                        "MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null), MapTestEntity(id=13, name=thirteen, sex=null, description=null), " +
                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null), MapTestEntity(id=15, name=fitteen, sex=null, description=null), " +
                        "MapTestEntity(id=22, name=twenty twe, sex=null, description=null)]",
                newLinkedList.toString());
        MapTestEntity peek = newLinkedList.peek();
        MapTestEntity first = newLinkedList.peekFirst();
        Assert.assertTrue(peek.equals(first));
        MapTestEntity last = newLinkedList.peekLast();
        Assert.assertEquals("MapTestEntity(id=22, name=twenty twe, sex=null, description=null)", last.toString());

        //按size分割list，最后一个可能更小
        List<List<MapTestEntity>> partition = Lists.partition(list, 3);
        Assert.assertTrue(Integer.valueOf(4).equals(partition.size()));
        Assert.assertTrue(Integer.valueOf(1).equals(partition.get(3).size()));

        List<List<MapTestEntity>> reverse = Lists.reverse(partition);
        Assert.assertTrue(Integer.valueOf(1).equals(reverse.get(0).size()));


    }

    private static void testCollections2() {
        Collection<MapTestEntity> filter = Collections2.filter(list, map -> map.getId() < 13);
        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), " +
                        "MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null)]",
                filter.toString());
        //返回指定Iterable的所有排列的Collection。
        //使用指定的Comparator返回指定Iterable的所有排列的Collection，以建立字典顺序。
        Collection<List<MapTestEntity>> orderedPermutations = Collections2.orderedPermutations(list, MapTestEntity::compareTo);
        Collection<MapInstanceEntity> transform = Collections2.transform(list, mapTestEntity -> new MapInstanceEntity(mapTestEntity.getId(), mapTestEntity.getName(), "movie" + mapTestEntity.getId()));
        Assert.assertEquals("[MapInstanceEntity(favouriteMoive=movie6), MapInstanceEntity(favouriteMoive=movie7), MapInstanceEntity(favouriteMoive=movie8), " +
                        "MapInstanceEntity(favouriteMoive=movie9), MapInstanceEntity(favouriteMoive=movie10), MapInstanceEntity(favouriteMoive=movie11), " +
                        "MapInstanceEntity(favouriteMoive=movie12), MapInstanceEntity(favouriteMoive=movie13), MapInstanceEntity(favouriteMoive=movie14), " +
                        "MapInstanceEntity(favouriteMoive=movie15)]",
                transform.toString());
    }
}
