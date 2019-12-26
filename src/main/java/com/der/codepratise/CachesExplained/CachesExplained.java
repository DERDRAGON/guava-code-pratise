package com.der.codepratise.CachesExplained;

import com.der.codepratise.entity.MapTestEntity;
import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2019-12-25 17:27
 */
@Slf4j
public class CachesExplained {

    private static final Ordering natural = Ordering.natural();

    private static final Ordering reverse = natural.reverse();

    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("cache-remove-%s").build();
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 3600, TimeUnit.MINUTES , Queues.newArrayBlockingQueue(30), threadFactory);

    private static final List<MapTestEntity> list = Lists.newArrayList(new MapTestEntity(6, "six"),
            new MapTestEntity(7, "seven"), new MapTestEntity(8, "eight"), new MapTestEntity(9, "nine"),
            new MapTestEntity(10, "ten"), new MapTestEntity(11, "eleven"), new MapTestEntity(12, "twelve"),
            new MapTestEntity(13, "thirteen"), new MapTestEntity(14, "fourteen"), new MapTestEntity(15, "fitteen"));

    private static final List<MapTestEntity> list2 = Lists.<MapTestEntity>newArrayList(new MapTestEntity(22, "twenty twe"),
            new MapTestEntity(23, "twenty three"), new MapTestEntity(27, "twenty five"), new MapTestEntity(1, "first"),
            new MapTestEntity(2, "second"), new MapTestEntity(3, "third"), new MapTestEntity(4, "four"),
            new MapTestEntity(5,"five"));

    public static void main(String[] args) {

        //从LoadingCache查询的正规方式是使用get(K)方法。这个方法要么返回已经缓存的值，要么使用CacheLoader向缓存原子地加载新值
        LoadingCache<String, MapTestEntity> loadingCache = CacheBuilder.newBuilder().maximumSize(2000L).expireAfterAccess(Duration.ofMillis(300)).removalListener(new RemovalListener<String, MapTestEntity>() {
            @Override
            public void onRemoval(RemovalNotification<String, MapTestEntity> notification) {
                log.info("remove key {} with value {}", notification.getKey(), notification.getValue());
            }
        }).build(new CacheLoader<String, MapTestEntity>() {
            @Override
            public MapTestEntity load(String key) throws Exception {
                return new MapTestEntity(null, key);
            }
        });

        ForwardingLoadingCache<String, MapTestEntity> forwardingLoadingCache = new ForwardingLoadingCache<String, MapTestEntity>() {

            @Override
            protected LoadingCache<String, MapTestEntity> delegate() {
                return loadingCache;
            }
        };

        try {
            MapTestEntity mapTestEntity = forwardingLoadingCache.get("33");
            Assert.assertEquals("MapTestEntity(id=null, name=33, sex=null, description=null)", mapTestEntity.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            Callable<MapTestEntity> callable = new Callable<MapTestEntity>() {
                @Override
                public MapTestEntity call() throws Exception {
                    return new MapTestEntity(0, "zero");
                }
            };
            MapTestEntity zero = forwardingLoadingCache.get("44", callable);
            Assert.assertEquals("MapTestEntity(id=0, name=zero, sex=null, description=null)", zero.toString());
            //cacheloader 中 load只执行了一次，说明第一次获取33后被缓存了起来
            MapTestEntity mapTestEntity = forwardingLoadingCache.get("33", callable);
            Assert.assertEquals("MapTestEntity(id=null, name=33, sex=null, description=null)", mapTestEntity.toString());

        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        MapTestEntity unchecked = loadingCache.getUnchecked("5");
        Assert.assertEquals("MapTestEntity(id=null, name=5, sex=null, description=null)", unchecked.toString());

        ConcurrentMap<String, MapTestEntity> asMap = forwardingLoadingCache.asMap();
        Assert.assertEquals("{5=MapTestEntity(id=null, name=5, sex=null, description=null), 44=MapTestEntity(id=0, name=zero, sex=null, description=null), " +
                        "33=MapTestEntity(id=null, name=33, sex=null, description=null)}",
                asMap.toString());
        try {
            MapTestEntity mapTestEntity = forwardingLoadingCache.get("34");
            Assert.assertEquals("MapTestEntity(id=null, name=34, sex=null, description=null)", mapTestEntity.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            ImmutableMap<String, MapTestEntity> all = forwardingLoadingCache.getAll(Lists.<String>newArrayList("5", "33", "44", "55"));
            Assert.assertEquals("{5=MapTestEntity(id=null, name=5, sex=null, description=null), 33=MapTestEntity(id=null, name=33, sex=null, description=null), " +
                            "44=MapTestEntity(id=0, name=zero, sex=null, description=null), 55=MapTestEntity(id=null, name=55, sex=null, description=null)}",
                    all.toString());
            Assert.assertEquals("{55=MapTestEntity(id=null, name=55, sex=null, description=null), 5=MapTestEntity(id=null, name=5, sex=null, description=null), " +
                            "34=MapTestEntity(id=null, name=34, sex=null, description=null), 44=MapTestEntity(id=0, name=zero, sex=null, description=null), " +
                            "33=MapTestEntity(id=null, name=33, sex=null, description=null)}",
                    asMap.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //回收方式 -- base-sice eviction ---- 使用CacheBuilder构建的缓存不会"自动"执行清理和回收工作，也不会在某个缓存项过期后马上清理，也没有诸如此类的清理机制。
        // 相反，它会在写操作时顺带做少量的维护工作，或者偶尔在读操作时做——如果写操作实在太少的话。
        //1、maximumSize
        Cache<String, MapTestEntity> build = CacheBuilder.newBuilder().maximumSize(10).<String, MapTestEntity>build();
        for (MapTestEntity mapTestEntity : list) {
            build.put(mapTestEntity.getName(), mapTestEntity);
        }
        build.put(list2.get(0).getName(), list2.get(0));
        Assert.assertTrue(Long.valueOf(10).equals(build.size()));

        //2、maximumWeight
        Weigher<String, MapTestEntity> wright = new Weigher<String, MapTestEntity>() {
            @Override
            public int weigh(String key, MapTestEntity value) {
                return value.getId();
            }
        };
        Cache<String, MapTestEntity> build1 = CacheBuilder.newBuilder().maximumWeight(15).weigher(wright).<String, MapTestEntity>build();
        for (MapTestEntity mapTestEntity : list2) {
            build1.put(mapTestEntity.getName(), mapTestEntity);
        }
        for (MapTestEntity mapTestEntity : list) {
            build1.put(mapTestEntity.getName(), mapTestEntity);
        }
        Assert.assertEquals("{fitteen=MapTestEntity(id=15, name=fitteen, sex=null, description=null)}", build1.asMap().toString());

        //Timed Eviction
        //1、缓存项在给定时间内没有被读/写访问，则回收
        Cache<String, MapTestEntity> build2 = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMillis(20)).<String, MapTestEntity>build();
        //2、缓存项在给定时间内没有被写访问（创建或覆盖），则回收
        Cache<String, MapTestEntity> build3 = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMillis(300)).<String, MapTestEntity>build();

        //基于引用的回收（Reference-based Eviction）
        Cache<String, MapTestEntity> build4 = CacheBuilder.newBuilder().weakKeys().<String, MapTestEntity>build();
        Cache<String, MapTestEntity> build5 = CacheBuilder.newBuilder().weakValues().<String, MapTestEntity>build();
        Cache<String, MapTestEntity> build6 = CacheBuilder.newBuilder().softValues().<String, MapTestEntity>build();

        // 显式清除
        build.invalidate("nine");
        Assert.assertTrue(Long.valueOf(9).equals(build.size()));
        build.invalidateAll(list2.stream().map(mapTestEntity -> mapTestEntity.getName()).collect(Collectors.toList()));
        Assert.assertTrue(Long.valueOf(8).equals(build.size()));
        build.invalidateAll();
        Assert.assertTrue(Long.valueOf(0).equals(build.size()));
        RemovalListener<String, MapTestEntity> asynchronous = RemovalListeners.asynchronous(new RemovalListener<String, MapTestEntity>() {
            @Override
            public void onRemoval(RemovalNotification<String, MapTestEntity> notification) {
                LocalDateTime now = LocalDateTime.now();
                log.info("remove key {} with value {} at time {}", notification.getKey(), notification.getValue(), now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss S")));
            }
        }, executor);
        Cache<String, MapTestEntity> build7 = CacheBuilder.newBuilder().removalListener(asynchronous).<String, MapTestEntity>build();
        for (MapTestEntity mapTestEntity : list) {
            build7.put(mapTestEntity.getName(), mapTestEntity);
        }
//        build7.invalidateAll(list.stream().map(mapTestEntity -> mapTestEntity.getName()).collect(Collectors.toList()));
        for (MapTestEntity mapTestEntity : list) {
            build7.invalidate(mapTestEntity.getName());
        }

        //刷新
        LoadingCache<String, MapTestEntity> build8 = CacheBuilder.newBuilder().maximumSize(30).<String, MapTestEntity>build(new CacheLoader<String, MapTestEntity>() {
            @Override
            public MapTestEntity load(String key) throws Exception {
                return new MapTestEntity(Integer.valueOf(key), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + key);
            }

            @Override
            public ListenableFuture<MapTestEntity> reload(String key, MapTestEntity oldValue) throws Exception {
                return ListenableFutureTask.create(new Callable<MapTestEntity>() {
                    @Override
                    public MapTestEntity call() throws Exception {
                        return new MapTestEntity(Integer.valueOf(key), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + key);
                    }
                });
            }
        });

        //自动定时刷新功能  refreshAfterWrite 缓存项只有在被检索时才会真正刷新
        Cache<String, MapTestEntity> build9 = CacheBuilder.newBuilder().maximumSize(3000).refreshAfterWrite(Duration.ofMillis(2000)).recordStats().<String, MapTestEntity>build();

        CacheStats cacheStats = build9.stats();
        //缓存命中率
        cacheStats.hitRate();
        //加载新值的平均时间
        cacheStats.averageLoadPenalty();

        //缓存项被回收的总数，不包括显式清除
        cacheStats.evictionCount();


    }
}
