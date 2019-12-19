package com.der.codepratise.CollectionsUtil;

import com.der.codepratise.entity.MapInstanceEntity;
import com.der.codepratise.entity.MapTestEntity;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author K0790016
 **/
public class CollectionUtilities {

    private static final List<MapTestEntity> list = Lists.<MapTestEntity>newArrayList(new MapTestEntity(6, "six"),
            new MapTestEntity(7, "seven"), new MapTestEntity(8, "eight"), new MapTestEntity(9, "nine"),
            new MapTestEntity(10, "ten"), new MapTestEntity(11, "eleven"), new MapTestEntity(12, "twelve"),
            new MapTestEntity(13, "thirteen"), new MapTestEntity(14, "fourteen"), new MapTestEntity(15, "fitteen"));

    private static final List<MapTestEntity> list2 = Lists.<MapTestEntity>newArrayList(new MapTestEntity(22, "twenty twe"),
            new MapTestEntity(23, "twenty three"), new MapTestEntity(27, "twenty five"), new MapTestEntity(1, "first"),
            new MapTestEntity(2, "second"), new MapTestEntity(3, "third"), new MapTestEntity(4, "four"),
            new MapTestEntity(5,"five"));

    private static final Ordering<MapTestEntity> order = Ordering.from(MapTestEntity::compareTo);
    private static final Ordering<MapTestEntity> reverse = order.reverse();

    public static void main(String[] args) {
        testCollections2();
        testLists();
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
