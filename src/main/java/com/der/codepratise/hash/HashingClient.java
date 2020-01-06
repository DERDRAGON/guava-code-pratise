package com.der.codepratise.hash;

import com.der.codepratise.entity.MapInstanceEntity;
import com.der.codepratise.entity.MapTestEntity;
import com.der.codepratise.io.IoStreamStudy;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.*;

import java.io.IOException;

/**
 * @program: guava-code-pratise
 * @description: ${description}
 * @author: long
 * @create: 2020-01-06 15:55
 */
public class HashingClient {

    public static void main(String[] args) {
        testBloomFilter();
        testHashing();
    }

    private static void testHashing() {
        HashFunction md5 = Hashing.md5();
        HashCode hashCode = md5.newHasher().putLong(2).putString("name", Charsets.UTF_8).putObject(new MapTestEntity(72, "234"), new Funnel<MapTestEntity>() {
            @Override
            public void funnel(MapTestEntity from, PrimitiveSink into) {
                into.putInt(from.getId());
                into.putString(from.getName(), Charsets.UTF_8);
            }
        }).hash();
        //把 bloom filter 的实现从 md5 切换到 murmur 时，速度提升了 800%。  --> https://github.com/bitly/dablooms/pull/19
        Hasher hasher = Hashing.murmur3_128().newHasher();

        //以有序方式组合哈希码，因此，如果从此方法获得的两个哈希相同，则很可能每个哈希都是以相同的顺序从相同的哈希计算得出的。
        HashCode combineOrdered = Hashing.combineOrdered(Lists.newArrayList(hashCode));

        //以无序方式组合哈希码，因此，如果从此方法获得的两个哈希相同，则很可能每个哈希都是以相同的顺序从相同的哈希计算得出的。
        HashCode combineUnordered = Hashing.combineUnordered(Lists.newArrayList(combineOrdered, hashCode));

        //为哈希码分配一个一致的“存储桶”，该存储桶随着存储桶数量的增加而使重新映射的需求降至最低。有关详细信息，请参见Wikipedia。
        int consistentHash = Hashing.consistentHash(combineUnordered, 64);
    }

    private static void testBloomFilter() {
        BloomFilter<MapInstanceEntity> bloomFilter = BloomFilter.<MapInstanceEntity>create((from, into) -> {
            into.putInt(from.getId()).putBoolean(from.getSex()).putString(from.getName(), Charsets.UTF_8).putString(from.getDescription(), Charsets.UTF_8).putString(from.getFavouriteMoive(), Charsets.UTF_8);
        }, 5000, 0.001);
//        创建一个BloomFilter，其插入次数为预期值，默认的假阳性概率为3％。
        BloomFilter<MapInstanceEntity> bloomFilter2 = BloomFilter.<MapInstanceEntity>create((from, into) -> {
            into.putInt(from.getId()).putBoolean(from.getSex()).putString(from.getName(), Charsets.UTF_8).putString(from.getDescription(), Charsets.UTF_8).putString(from.getFavouriteMoive(), Charsets.UTF_8);
        }, 5000);
//如果该元素可能已放入此Bloom过滤器中，则返回true；如果绝对不是这种情况，则返回false。
//        bloomFilter.mightContain()
//        返回已添加到此Bloom过滤器的不同元素总数的估计值。
//        bloomFilter.approximateElementCount()
//        创建一个新的BloomFilter，它是该实例的副本。
//        bloomFilter.copy()
//        返回对于尚未实际放入BloomFilter的对象，maykContain（Object）将错误地返回true的概率。
//        bloomFilter.expectedFpp()
//        确定给定的Bloom过滤器是否与此Bloom过滤器兼容。
//        bloomFilter.isCompatible(bloomFilter2)
        try {
            BloomFilter.<MapInstanceEntity>readFrom(IoStreamStudy.getNewInputStreamFile("in4"), (from, into) -> {
                into.putInt(from.getId());
            }).writeTo(IoStreamStudy.getNewOutputStream("out4"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        返回一个期望达到指定插入次数的收集器，并产生误报率为3％的BloomFilter。
//        Collector<MapInstanceEntity, ?, BloomFilter<MapInstanceEntity>> bloomFilterCollector = BloomFilter.<MapInstanceEntity>toBloomFilter((from, into) -> {
//            into.putInt(from.getId()).putBoolean(from.getSex()).putString(from.getName(), Charsets.UTF_8).putString(from.getDescription(), Charsets.UTF_8).putString(from.getFavouriteMoive(), Charsets.UTF_8);
//        }, 5000l, 0.001);
    }
}
