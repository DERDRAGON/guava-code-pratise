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
        testSplitter();
        testCharsets();
        testCaseFormat();
        testCharMatcher();
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
        Assert.assertEquals("32", charMatcherDigi.retainFrom("sdfsdfsddsf32"));
//    String removeFrom(CharSequence sequence): 删除sequence中匹配到到的字符并返回
        Assert.assertEquals("24", charMatcherDigi.retainFrom("fdsdfsdf24ddffsd"));

//    CharMatcher inRange(char startInclusive, char endIncludesive): 返回匹配范围内任意字符的Matcher
//    CharMatcher or(CharMatcher other): 返回与other匹配条件组合做或来判断的Matcher
        Assert.assertEquals("sfdsdfdsfDASDHAS", charMatcherAny.inRange('a', 'z').or(charMatcherAny.inRange('A', 'Z')).retainFrom("sfdsdfdsf243DASDHAS"));
        Assert.assertEquals("243", charMatcherAny.inRange('a', 'z').or(charMatcherAny.inRange('A', 'Z')).removeFrom("sfdsdfdsf243DASDHAS"));

//    CharMatcher is(char match): 返回匹配指定字符的Matcher
//    int indexIn(CharSequence sequence): 返回sequence中匹配到的第一个字符的坐标
//    int indexIn(CharSequence sequence, int start): 返回从start开始,在sequence中匹配到的第一个字符的坐标
        CharMatcher charMatcher = CharMatcher.is('3');
        Assert.assertTrue(Integer.valueOf(0).equals(charMatcher.retainFrom("dfssd").length()));
        Assert.assertTrue(Integer.valueOf(4).equals(charMatcher.indexIn("fdjd3lccs3dsddf")));
        Assert.assertTrue(Integer.valueOf(9).equals(charMatcher.indexIn("fdjd3lccs3dsddf", 7)));
        Assert.assertTrue(Integer.valueOf(9).equals(charMatcher.lastIndexIn("fdjd3lccs3dsddf")));

//    CharMatcher isNot(char match): 返回不匹配指定字符的Matcher
        CharMatcher matcher = CharMatcher.isNot('5');
        Assert.assertEquals("fsdfds243", matcher.retainFrom("fsdfds2435"));
        Assert.assertEquals("5", matcher.removeFrom("fsdfds2435"));
//    String collapseFrom(CharSequence sequence, char replacement): 将匹配到的组(连续匹配的字符)替换成replacement
        Assert.assertEquals("y5", matcher.collapseFrom("fsdfds2435", 'y'));

//    CharMatcher anyOf(CharSequence sequence): 返回匹配sequence中任意字符的Matcher
        CharMatcher any = CharMatcher.anyOf("abcdefghijklmnopqrstuvwxyz");
        Assert.assertEquals("sldkj", any.retainFrom("sldkj3434"));

//    CharMatcher noneOf(CharSequence sequence): 返回不匹配sequence中任何一个字符的Matcher
        CharMatcher none = CharMatcher.noneOf("c");
//    boolean matchesAllOf(CharSequence sequence): sequence中所有字符都能匹配Matcher,返回true
//    boolean matchesAnyOf(CharSequence sequence): 只要sequence中有任意字符能匹配Matcher,返回true
        Assert.assertFalse(none.matchesAllOf("fdjdlccsdsddf"));
        Assert.assertTrue(none.matchesAnyOf("fdjdlccsdsddf"));

//    CharMatcher negate(): 返回以当前Matcher判断规则相反的Matcher
        CharMatcher negate = none.negate();
//    int countIn(CharSequence sequence): 返回sequence中匹配到的字符计数
        Assert.assertTrue(Integer.valueOf(16).equals(none.countIn("sjdfklsfdcjfjksdf")));
        Assert.assertTrue(Integer.valueOf(1).equals(negate.countIn("sjdfklsfdcjfjksdf")));

//    boolean matchesNoneOf(CharSequence sequence): sequence中所有字符都不能匹配Matcher,返回true
        Assert.assertTrue(negate.matchesNoneOf("sdfsdff"));//完全没有c -- true
        Assert.assertFalse(none.matchesNoneOf("fsdddfscsfdsdfs")); //全部都是c -- false

//    CharMatcher forPredicate(Predicate<? super Charater> predicate): 返回使用predicate的apply()判断匹配的Matcher
        CharMatcher predicate = CharMatcher.forPredicate(new Predicate<Character>() {
            @Override
            public boolean apply(Character input) {
                return input.toString().length() > 5;
            }
        });
        Assert.assertFalse(predicate.matchesAllOf("4sdfs")); // -- 用于char类型操作 -- 只能判断sequence参数的最后一位

//    String replaceFrom(CharSequence sequence, char replacement): 替换sequence中匹配到的字符并返回
        Assert.assertEquals("000000c0c00000", none.replaceFrom("434sfjcscsdfew", '0'));
        Assert.assertEquals("434sfj0s0sdfew", negate.replaceFrom("434sfjcscsdfew", '0'));

//    String trimFrom(CharSequence sequence): 删除首尾匹配到的字符并返回
        Assert.assertEquals("sffsddsfsd", negate.trimFrom("csffsddsfsdc"));
        Assert.assertEquals("ccccccc", none.trimFrom("sffsdsfsdfdffcccccccdsfsd"));
//    String trimLeadingFrom(CharSequence sequence): 删除首部匹配到的字符
        Assert.assertEquals("sffsddsfsdc", negate.trimLeadingFrom("csffsddsfsdc"));
        Assert.assertEquals("cccccccdsfsd", none.trimLeadingFrom("sffsdsfsdfdffcccccccdsfsd"));
//    String trimTrailingFrom(CharSequence sequence): 删除尾部匹配到的字符
        Assert.assertEquals("csffsddsfsd", negate.trimTrailingFrom("csffsddsfsdc"));
        Assert.assertEquals("sffsdsfsdfdffccccccc", none.trimTrailingFrom("sffsdsfsdfdffcccccccdsfsd"));
//    String collapseFrom(CharSequence sequence, char replacement): 将匹配到的组(连续匹配的字符)替换成replacement
        Assert.assertEquals("1sffsddsfsd1", negate.collapseFrom("csffsddsfsdc", '1'));
        Assert.assertEquals("1cc1ccccccc1", none.collapseFrom("sffccsdsfsdfdffcccccccdsfsd", '1'));
//    String trimAndCollapseFrom(CharSequence sequence, char replacement): 先trim在replace
        Assert.assertEquals("sffsd1dsfsd", negate.trimAndCollapseFrom("csffsdcdsfsdc", '1'));
        Assert.assertEquals("cc1ccccccc", none.trimAndCollapseFrom("sffccsdsfsdfdffcccccccdsfsd", '1'));

        Assert.assertEquals("sdff   ff", CharMatcher.whitespace().trimFrom("  sdff   ff  "));
    }

    private static void testCaseFormat() {
        String caondfds_fds = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "CAONDFDS_FDS");
        Assert.assertEquals("caondfdsFds", caondfds_fds);

        String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, caondfds_fds);
        Assert.assertEquals("CaondfdsFds", to);

        String to1 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "CAONDFDS_FDS");
        Assert.assertEquals("c_a_o_n_d_f_d_s__f_d_s", to1);

        String to2 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, caondfds_fds);
        Assert.assertEquals("caondfds-fds", to2);

        String to3 = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, to2);
        Assert.assertEquals("CAONDFDS_FDS", to3);

        String to4 = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, to3);
        Assert.assertEquals("CaondfdsFds", to4);

        String to5 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, to4);
        Assert.assertEquals("caondfds_fds", to5);

        Converter<String, String> converter = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.UPPER_CAMEL);
        Assert.assertEquals("CaondfdsFds", converter.convert(to5));

        Iterable<String> convertAll = converter.convertAll(Lists.newArrayList(to, to2, to3, to4));
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
        Assert.assertTrue(Integer.valueOf(4).equals(split.size()));
        Assert.assertEquals("sg", split.get(2));

        Splitter splitter2 = Splitter.on(",").omitEmptyStrings().limit(3).trimResults();
        List<String> toList = splitter2.splitToList(emampleStr);
        Assert.assertTrue(Integer.valueOf(3).equals(toList.size()));
        Assert.assertEquals("sg,,ssf", toList.get(2));

        Map<String, String> map = Splitter.on("&").withKeyValueSeparator("=").split("id=33&name=ksfdjdl");
        Assert.assertEquals("33", map.get("id"));
        Assert.assertEquals("ksfdjdl", map.get("name"));
        Splitter splitter1 = Splitter.onPattern("\\|").omitEmptyStrings();
        List<String> toList1 = splitter1.splitToList("hello|world|java");
        Assert.assertEquals("java", toList1.get(2));

        //将字符串拆分为指定固定长度的子字符串。最后一块可以小于长度，但永远不会为空。
        Splitter splitter3 = Splitter.fixedLength(5);
        List<String> toList2 = splitter3.splitToList("jfkljfskljfsklfjsfjlsdkfjsdklfjfslkd");
        Assert.assertTrue(Integer.valueOf(8).equals(toList2.size()));
        Assert.assertTrue(Integer.valueOf(1).equals(toList2.get(7).length()));

        List<String> strings = splitter3.splitToList("");
        System.out.println(JSON.toJSONString(strings));

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

        Joiner skipNulls = Joiner.on("&").skipNulls();
        //?? 最后一个不能是null
        Assert.assertEquals("1&2&3", skipNulls.join(null,"1", "2", null, "3"));
    }
}
