package com.der.codepratise.string_pratise;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableBiMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author K0790016
 **/
public class Part1Sessionjoiner {

    public static void main(String[] args) {
        testJoiner();
        testSplitter();
    }

    private static final List<String> stringList = Arrays.asList("2", "34f", "2");

    private static final String emampleStr = "sf,fsd,   sg,,ssf";

    private static void testSplitter() {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        List<String> split = splitter.splitToList(emampleStr);
        System.out.println(split.size());
        split.forEach(System.out::println);

        Splitter splitter2 = Splitter.on(",").omitEmptyStrings().limit(3);
        List<String> toList = splitter2.splitToList(emampleStr);
        System.out.println(toList.size());
        toList.forEach(System.out::println);

        Map<String, String> map = Splitter.on("&").withKeyValueSeparator("=").split("id=33&name=ksfdjdl");
        System.out.println(JSON.toJSONString(map));
        Splitter splitter1 = Splitter.onPattern("\\|").omitEmptyStrings();
        splitter1.splitToList("hello|world|java").forEach(System.out::println);


    }

    private static void testJoiner() {
        Joiner joiner = Joiner.on("; ").skipNulls();
        String result = joiner.join("fsdk", null, "fs", "gfdfdf");
        System.out.println(result);

        System.out.println(Joiner.on("-").skipNulls().join(stringList));

        //MapJoinner
        System.out.println(Joiner.on("&").withKeyValueSeparator("=").join(ImmutableBiMap.of("id", 123, "dkj", 456)));;
    }
}
