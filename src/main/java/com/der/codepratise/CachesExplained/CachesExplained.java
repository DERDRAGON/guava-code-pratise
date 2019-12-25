package com.der.codepratise.CachesExplained;

import com.der.codepratise.entity.MapTestEntity;
import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2019-12-25 17:27
 */
@Slf4j
public class CachesExplained {

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

    }
}
