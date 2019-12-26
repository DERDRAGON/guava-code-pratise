package com.der.codepratise.stringPratise;

import com.alibaba.fastjson.JSON;
import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableBiMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author K0790016
 **/
public class Part1Sessionjoiner {

    public static void main(String[] args) {
        testJoiner();
        testSplitter();
        testCharsets();
        testCaseFormat();
    }

    private static void testCaseFormat() {
        String caondfds_fds = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "CAONDFDS_FDS");
        System.out.println(caondfds_fds);
    }

    private static void testCharsets() {
        String src = "abc";
        src.getBytes(Charsets.UTF_8);
        src.getBytes(Charsets.ISO_8859_1);
        src.getBytes(Charsets.UTF_16);
        src.getBytes(Charsets.US_ASCII);
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
