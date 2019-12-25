package com.der.codepratise.CachesExplained;

import com.der.codepratise.entity.MapTestEntity;
import com.google.common.cache.*;

import java.time.Duration;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2019-12-25 17:27
 */
public class CachesExplained {

    public static void main(String[] args) {

        //从LoadingCache查询的正规方式是使用get(K)方法。这个方法要么返回已经缓存的值，要么使用CacheLoader向缓存原子地加载新值
        LoadingCache<String, MapTestEntity> loadingCache = CacheBuilder.newBuilder().maximumSize(2000L).expireAfterAccess(Duration.ofDays(1L)).removalListener(new RemovalListener<String, MapTestEntity>() {
            @Override
            public void onRemoval(RemovalNotification<String, MapTestEntity> notification) {

            }
        }).build(new CacheLoader<String, MapTestEntity>() {
            @Override
            public MapTestEntity load(String key) throws Exception {
                return new MapTestEntity(null, key);
            }
        });

        loadingCache.getUnchecked()
    }
}
