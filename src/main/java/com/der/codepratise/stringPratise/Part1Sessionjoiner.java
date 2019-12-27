package com.der.codepratise.stringPratise;

import com.alibaba.fastjson.JSON;
import com.der.codepratise.entity.MapTestEntity;
import com.google.common.base.*;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import org.assertj.core.util.Lists;
import org.junit.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author K0790016
 **/
public class Part1Sessionjoiner {

    private static final CharMatcher charMatcherAny = CharMatcher.any();
    private static final CharMatcher charMatcherDigi = CharMatcher.digit();

    private static final List<MapTestEntity> list = Lists.newArrayList(new MapTestEntity(6, "six"),
            new MapTestEntity(7, "seven"), new MapTestEntity(8, "eight"), new MapTestEntity(9, "nine"),
            new MapTestEntity(10, "ten"), new MapTestEntity(11, "eleven"), new MapTestEntity(12, "twelve"),
            new MapTestEntity(13, "thirteen"), new MapTestEntity(14, "fourteen"), new MapTestEntity(15, "fitteen"));

    public static void main(String[] args) {
        testJoiner();
//        testSplitter();
//        testCharsets();
//        testCaseFormat();
//        testCharMatcher();
    }

//    ANY: 匹配任何字符
//    ASCII: 匹配是否是ASCII字符
//    BREAKING_WHITESPACE: 匹配所有可换行的空白字符(不包括非换行空白字符,例如"\u00a0")
//    DIGIT: 数字
//    INVISIBLE: 匹配所有看不见的字符
//    JAVA_DIGIT: 匹配UNICODE数字, 使用 Character.isDigit() 实现
//    JAVA_ISO_CONTROL: 匹配ISO匹配ASCII数字控制字符, 使用 Charater.isISOControl() 实现
//    JAVA_LETTER: 匹配字母, 使用 Charater.isLetter() 实现
//    JAVA_LETTER_OR_DIGET: 匹配数字或字母
//    JAVA_LOWER_CASE: 匹配小写
//    JAVA_UPPER_CASE: 匹配大写
//    NONE: 不匹配所有字符
//    SINGLE_WIDTH: 匹配单字宽字符, 如中文字就是双字宽
//    WHITESPACE: 匹配所有空白字符
    private static void testCharMatcher() {
//    CharMatcher is(char match): 返回匹配指定字符的Matcher
//    CharMatcher isNot(char match): 返回不匹配指定字符的Matcher
//    CharMatcher anyOf(CharSequence sequence): 返回匹配sequence中任意字符的Matcher
//    CharMatcher noneOf(CharSequence sequence): 返回不匹配sequence中任何一个字符的Matcher
//    CharMatcher inRange(char startInclusive, char endIncludesive): 返回匹配范围内任意字符的Matcher
//    CharMatcher forPredicate(Predicate<? super Charater> predicate): 返回使用predicate的apply()判断匹配的Matcher
//    CharMatcher negate(): 返回以当前Matcher判断规则相反的Matcher
//    CharMatcher and(CharMatcher other): 返回与other匹配条件组合做与来判断的Matcher
//    CharMatcher or(CharMatcher other): 返回与other匹配条件组合做或来判断的Matcher
//    boolean matchesAnyOf(CharSequence sequence): 只要sequence中有任意字符能匹配Matcher,返回true
//    boolean matchesAllOf(CharSequence sequence): sequence中所有字符都能匹配Matcher,返回true
//    boolean matchesNoneOf(CharSequence sequence): sequence中所有字符都不能匹配Matcher,返回true
//    int indexIn(CharSequence sequence): 返回sequence中匹配到的第一个字符的坐标
//    int indexIn(CharSequence sequence, int start): 返回从start开始,在sequence中匹配到的第一个字符的坐标
//    int lastIndexIn(CharSequence sequence): 返回sequence中最后一次匹配到的字符的坐标
//    int countIn(CharSequence sequence): 返回sequence中匹配到的字符计数
//    String removeFrom(CharSequence sequence): 删除sequence中匹配到到的字符并返回
//    String retainFrom(CharSequence sequence): 保留sequence中匹配到的字符并返回
//    String replaceFrom(CharSequence sequence, char replacement): 替换sequence中匹配到的字符并返回
//    String trimFrom(CharSequence sequence): 删除首尾匹配到的字符并返回
//    String trimLeadingFrom(CharSequence sequence): 删除首部匹配到的字符
//    String trimTrailingFrom(CharSequence sequence): 删除尾部匹配到的字符
//    String collapseFrom(CharSequence sequence, char replacement): 将匹配到的组(连续匹配的字符)替换成replacement
//    String trimAndCollapseFrom(CharSequence sequence, char replacement): 先trim在replace


        //    String retainFrom(CharSequence sequence): 保留sequence中匹配到的字符并返回
        System.out.println(charMatcherDigi.retainFrom("sdfsdfsddsf32"));
//    String removeFrom(CharSequence sequence): 删除sequence中匹配到到的字符并返回
        System.out.println(charMatcherDigi.retainFrom("fdsdfsdf24ddffsd"));

//    CharMatcher inRange(char startInclusive, char endIncludesive): 返回匹配范围内任意字符的Matcher
//    CharMatcher or(CharMatcher other): 返回与other匹配条件组合做或来判断的Matcher
        System.out.println(charMatcherAny.inRange('a', 'z').or(charMatcherAny.inRange('A', 'Z')).retainFrom("sfdsdfdsf243DASDHAS"));
        System.out.println(charMatcherAny.inRange('a', 'z').or(charMatcherAny.inRange('A', 'Z')).removeFrom("sfdsdfdsf243DASDHAS"));

//    CharMatcher is(char match): 返回匹配指定字符的Matcher
//    int indexIn(CharSequence sequence): 返回sequence中匹配到的第一个字符的坐标
//    int indexIn(CharSequence sequence, int start): 返回从start开始,在sequence中匹配到的第一个字符的坐标
        CharMatcher charMatcher = CharMatcher.is('3');
        System.out.println(charMatcher.retainFrom("dfssd").length());
        System.out.println(charMatcher.indexIn("fdjd3lccs3dsddf"));
        System.out.println(charMatcher.indexIn("fdjd3lccs3dsddf", 7));
        System.out.println(charMatcher.lastIndexIn("fdjd3lccs3dsddf"));

        System.out.println("====================");

//    CharMatcher isNot(char match): 返回不匹配指定字符的Matcher
        CharMatcher matcher = CharMatcher.isNot('5');
        System.out.println(matcher.retainFrom("fsdfds2435"));
        System.out.println(matcher.removeFrom("fsdfds2435"));
//    String collapseFrom(CharSequence sequence, char replacement): 将匹配到的组(连续匹配的字符)替换成replacement
        System.out.println(matcher.collapseFrom("fsdfds2435", 'y'));

//    CharMatcher anyOf(CharSequence sequence): 返回匹配sequence中任意字符的Matcher
        CharMatcher any = CharMatcher.anyOf("abcdefghijklmnopqrstuvwxyz");
        System.out.println(any.retainFrom("sldkj3434"));

//    CharMatcher noneOf(CharSequence sequence): 返回不匹配sequence中任何一个字符的Matcher
        CharMatcher none = CharMatcher.noneOf("c");
//    boolean matchesAllOf(CharSequence sequence): sequence中所有字符都能匹配Matcher,返回true
//    boolean matchesAnyOf(CharSequence sequence): 只要sequence中有任意字符能匹配Matcher,返回true
        System.out.println(none.matchesAllOf("fdjdlccsdsddf"));
        System.out.println(none.matchesAnyOf("fdjdlccsdsddf"));

//    CharMatcher negate(): 返回以当前Matcher判断规则相反的Matcher
        CharMatcher negate = none.negate();
//    int countIn(CharSequence sequence): 返回sequence中匹配到的字符计数
        System.out.println(none.countIn("sjdfklsfdcjfjksdf"));
        System.out.println(negate.countIn("sjdfklsfdcjfjksdf"));

//    boolean matchesNoneOf(CharSequence sequence): sequence中所有字符都不能匹配Matcher,返回true
        System.out.println(negate.matchesNoneOf("sdfsdff"));//完全没有c -- true
        System.out.println(none.matchesNoneOf("fsdddfscsfdsdfs")); //全部都是c -- true

//    CharMatcher forPredicate(Predicate<? super Charater> predicate): 返回使用predicate的apply()判断匹配的Matcher
        CharMatcher predicate = CharMatcher.forPredicate(new Predicate<Character>() {
            @Override
            public boolean apply(Character input) {
                System.out.println("========input==========" + input);
                return input.toString().length() > 5;
            }
        });
        System.out.println(predicate.matchesAllOf("4")); // -- 用于char类型操作 -- 只能判断sequence参数的第一位

//    String replaceFrom(CharSequence sequence, char replacement): 替换sequence中匹配到的字符并返回
        System.out.println(none.replaceFrom("434sfjcscsdfew", '0'));
        System.out.println(negate.replaceFrom("434sfjcscsdfew", '0'));

//    String trimFrom(CharSequence sequence): 删除首尾匹配到的字符并返回
        System.out.println(negate.trimFrom("csffsddsfsdc"));
        System.out.println(none.trimFrom("sffsdsfsdfdffcccccccdsfsd"));
//    String trimLeadingFrom(CharSequence sequence): 删除首部匹配到的字符
        System.out.println(negate.trimLeadingFrom("csffsddsfsdc"));
        System.out.println(none.trimLeadingFrom("sffsdsfsdfdffcccccccdsfsd"));
//    String trimTrailingFrom(CharSequence sequence): 删除尾部匹配到的字符
        System.out.println(negate.trimTrailingFrom("csffsddsfsdc"));
        System.out.println(none.trimTrailingFrom("sffsdsfsdfdffcccccccdsfsd"));
//    String collapseFrom(CharSequence sequence, char replacement): 将匹配到的组(连续匹配的字符)替换成replacement
        System.out.println(negate.collapseFrom("csffsddsfsdc", '1'));
        System.out.println(none.collapseFrom("sffccsdsfsdfdffcccccccdsfsd", '1'));
//    String trimAndCollapseFrom(CharSequence sequence, char replacement): 先trim在replace
        System.out.println(negate.trimAndCollapseFrom("csffsdcdsfsdc", '1'));
        System.out.println(none.trimAndCollapseFrom("sffccsdsfsdfdffcccccccdsfsd", '1'));

        System.out.println(CharMatcher.whitespace().trimFrom("  sdff   ff  "));
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
        Assert.assertEquals("fsdk; fs; gfdfdf", result);

        Assert.assertEquals("2-34f-2", Joiner.on("-").skipNulls().join(stringList));

        //MapJoinner
        Assert.assertEquals("id=123&dkj=456", Joiner.on("&").withKeyValueSeparator("=").join(ImmutableBiMap.of("id", 123, "dkj", 456)));

        try {
            Assert.assertEquals("begin: MapTestEntity(id=6, name=six, sex=null, description=null); MapTestEntity(id=7, name=seven, sex=null, description=null); " +
                            "MapTestEntity(id=8, name=eight, sex=null, description=null); MapTestEntity(id=9, name=nine, sex=null, description=null); " +
                            "MapTestEntity(id=10, name=ten, sex=null, description=null); MapTestEntity(id=11, name=eleven, sex=null, description=null); " +
                            "MapTestEntity(id=12, name=twelve, sex=null, description=null); MapTestEntity(id=13, name=thirteen, sex=null, description=null); " +
                            "MapTestEntity(id=14, name=fourteen, sex=null, description=null); MapTestEntity(id=15, name=fitteen, sex=null, description=null)",
                    joiner.appendTo(new StringBuffer("begin: "), list).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("begin: MapTestEntity(id=6, name=six, sex=null, description=null); MapTestEntity(id=7, name=seven, sex=null, description=null); " +
                        "MapTestEntity(id=8, name=eight, sex=null, description=null); MapTestEntity(id=9, name=nine, sex=null, description=null); " +
                        "MapTestEntity(id=10, name=ten, sex=null, description=null); MapTestEntity(id=11, name=eleven, sex=null, description=null); " +
                        "MapTestEntity(id=12, name=twelve, sex=null, description=null); MapTestEntity(id=13, name=thirteen, sex=null, description=null); " +
                        "MapTestEntity(id=14, name=fourteen, sex=null, description=null); MapTestEntity(id=15, name=fitteen, sex=null, description=null)",
                joiner.appendTo(new StringBuilder("begin: "), list).toString());

        Assert.assertEquals("ko: MapTestEntity(id=6, name=six, sex=null, description=null); MapTestEntity(id=7, name=seven, sex=null, description=null); MapTestEntity(id=11, name=eleven, sex=null, description=null)",
                joiner.appendTo(new StringBuilder("ko: "), list.get(0), list.get(1), list.get(5)).toString());

        Assert.assertEquals("MapTestEntity(id=31, name=thirty one, sex=null, description=null); MapTestEntity(id=32, name=thirty two, sex=null, description=null)", joiner.join(new MapTestEntity(31, "thirty one"), new MapTestEntity(32, "thirty two")));

        Joiner.MapJoiner mapJoiner = Joiner.on(" 这是一段分割线 ").withKeyValueSeparator(":");

        Map<String, MapTestEntity> newHashMap = Maps.<String, MapTestEntity>newHashMap();
        newHashMap.put("thirty three", new MapTestEntity(33, "thirty three"));
        newHashMap.put("thirty four", new MapTestEntity(34, "thirty four"));
        Assert.assertEquals("thirty three:MapTestEntity(id=33, name=thirty three, sex=null, description=null) 这是一段分割线 " +
                        "thirty four:MapTestEntity(id=34, name=thirty four, sex=null, description=null)",
                mapJoiner.join(newHashMap));
    }
}
