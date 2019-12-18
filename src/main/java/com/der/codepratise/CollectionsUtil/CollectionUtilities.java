package com.der.codepratise.CollectionsUtil;

import com.der.codepratise.entity.MapInstanceEntity;
import com.der.codepratise.entity.MapTestEntity;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import org.assertj.core.util.Lists;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;

import java.util.Collection;
import java.util.List;

/**
 * @author K0790016
 **/
public class CollectionUtilities {

    private static final List<MapTestEntity> list = Lists.newArrayList(new MapTestEntity(6, "six"),
            new MapTestEntity(7, "seven"), new MapTestEntity(8, "eight"), new MapTestEntity(9, "nine"),
            new MapTestEntity(10, "ten"), new MapTestEntity(11, "eleven"), new MapTestEntity(12, "twelve"),
            new MapTestEntity(13, "thirteen"), new MapTestEntity(14, "fourteen"), new MapTestEntity(15, "fitteen"));

    private static final Ordering<MapTestEntity> order = Ordering.from(MapTestEntity::compareTo);
    private static final Ordering<MapTestEntity> reverse = order.reverse();

    public static void main(String[] args) {
        testCollections2();
    }

    private static void testCollections2() {
        Collection<MapTestEntity> filter = Collections2.filter(list, map -> map.getId() < 13);
        Assert.assertEquals("[MapTestEntity(id=6, name=six, sex=null, description=null), MapTestEntity(id=7, name=seven, sex=null, description=null), MapTestEntity(id=8, name=eight, sex=null, description=null), MapTestEntity(id=9, name=nine, sex=null, description=null), MapTestEntity(id=10, name=ten, sex=null, description=null), MapTestEntity(id=11, name=eleven, sex=null, description=null), MapTestEntity(id=12, name=twelve, sex=null, description=null)]",
                filter.toString());
        //返回指定Iterable的所有排列的Collection。
        //使用指定的Comparator返回指定Iterable的所有排列的Collection，以建立字典顺序。
        Collection<List<MapTestEntity>> orderedPermutations = Collections2.orderedPermutations(list, MapTestEntity::compareTo);
        Collection<MapInstanceEntity> transform = Collections2.transform(list, new Function<MapTestEntity, MapInstanceEntity>() {
            @Override
            public MapInstanceEntity apply(@Nullable MapTestEntity mapTestEntity) {
                return new MapInstanceEntity(mapTestEntity.getId(), mapTestEntity.getName(), "movie" + mapTestEntity.getId());
            }
        });
        Assert.assertEquals("[MapInstanceEntity(favouriteMoive=movie6), MapInstanceEntity(favouriteMoive=movie7), MapInstanceEntity(favouriteMoive=movie8), MapInstanceEntity(favouriteMoive=movie9), MapInstanceEntity(favouriteMoive=movie10), MapInstanceEntity(favouriteMoive=movie11), MapInstanceEntity(favouriteMoive=movie12), MapInstanceEntity(favouriteMoive=movie13), MapInstanceEntity(favouriteMoive=movie14), MapInstanceEntity(favouriteMoive=movie15)]",
                transform.toString());
    }
}
