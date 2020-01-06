package com.der.codepratise.io;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author K0790016
 **/
@Slf4j
public class IoStreamStudy {

    private static String FILE_PATH;

    private static String FILE_SUFFIX = ".txt";

    static {
        StringBuilder filePath = new StringBuilder();
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
            filePath.append("E:/ideaworkspace");
        } else {
            filePath.append("/Library/idea_space");
        }
        filePath.append("/guava-code-pratise/file/");
        FILE_PATH = filePath.toString();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static String getFileFullName(String fileName) {
        return FILE_PATH + fileName + FILE_SUFFIX;
    }

    private static File getFile(String fileName){
        if (StringUtils.isNotBlank(fileName)) {
            File file = new File(getFileFullName(fileName));
            if (file.exists())
                return file;
            return createNewFile(fileName);
        }
        return null;
    }

    private static File createNewFile(String fileName) {
        FileWriter fileWriter = null;
        try {
            if (StringUtils.isBlank(fileName)){
                return null;
            }
            File file = new File(getFileFullName(fileName));
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter =  new FileWriter(file);
            fileWriter.write("这是一个新建的文件，建于" + DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss S").format(LocalDateTime.now()) + "，文件名称为：" + fileName);
            return file;
        } catch (IOException e) {
            log.error("创建文件file:{},异常:{}", fileName, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    log.error("File Writer 关闭异常:{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return null;
    }

    /**
     * 创建一个文件输入流
     * @param fileName 文件名称
     * @return InputStream
     */
    public static InputStream getNewInputStreamFile(String fileName) {
        File file = createNewFile(fileName);
        if (null != file){
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                log.error("获取文件输入流失败:{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return null;
    }

    public static OutputStream getNewOutputStream(String fileName) {
        File file = createNewFile(fileName);
        if (null != file) {
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                log.error("创建文件输出流失败:{}", ExceptionUtils.getStackTrace(e));
            }
        }
        return null;
    }

    public static void main(String[] args) {
        testByteStreams();
        testCharStreams();
        testFiles();
    }

    private static void testFiles() {
        try {
            Files.copy(createNewFile("in8"), getNewOutputStream("out7"));
            Files.copy(createNewFile("in8"), createNewFile("out8"));
        } catch (IOException e) {
        }

//        ByteSource byteSource = Files.asByteSource(createNewFile("in1"));

        try {
            CharSource charSource = Files.asCharSource(createNewFile("out5"), Charsets.UTF_8);
            String read = charSource.read();
            assertTrue(read.lastIndexOf("out5") > 0);
        } catch (IOException e) {
        }

        try {
            Files.asCharSink(getFile("in7"), Charsets.UTF_8, FileWriteMode.APPEND).write("来添加一段信息");
            CharSource charSource = Files.asCharSource(getFile("in7"), Charsets.UTF_8);
            String read = charSource.read();
            assertTrue(read.lastIndexOf("来添加一段信息") > 0);
        } catch (IOException e) {
        }

        //创建指定文件的任何必要但不存在的父目录。
//        Files.createParentDirs(createNewFile("29"));

        try {
            assertTrue(Files.equal(getFile("in2"), getFile("out2")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.move(getFile("in4"), getFile("out3"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<String> readLines = Files.readLines(getFile("in5"), Charsets.UTF_8);
            assertTrue(readLines.size() == 1);
            assertTrue(readLines.get(0).length() == 43);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //创建空文件
//        Files.touch(new File("fkjsjfk"));

        /**
         * 递归文件
         *
         * breadthFirst 为【广度优先遍历】
         * depthFirstPreOrder 和 depthFirstPostOrder 为【深度优先遍历】
         *
         * */
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(FILE_PATH));
//        files.forEach(System.out::println);
        assertTrue(18 == Lists.newArrayList(files).size());

        Iterable<File> files1 = Files.fileTraverser().depthFirstPostOrder(new File(FILE_PATH));
//        files1.forEach(System.out::println);

        Iterable<File> files2 = Files.fileTraverser().depthFirstPreOrder(new File(FILE_PATH));
//        files2.forEach(System.out::println);

        String fileExtension = Files.getFileExtension(getFileFullName("in7"));
        assertEquals("txt", fileExtension);

        String nameWithoutExtension = Files.getNameWithoutExtension(getFileFullName("in8"));
        assertEquals(nameWithoutExtension, "in8");

        assertTrue(Files.isFile().apply(getFile("in9")));

        assertTrue(Files.isDirectory().apply(new File(FILE_PATH)));

        try {
            MappedByteBuffer mappedByteBuffer = Files.map(getFile("in7"));
            assertTrue(mappedByteBuffer.isLoaded());
        } catch (IOException e) {
        }
        try {
            BufferedReader bufferedReader = Files.newReader(getFile("in8"), Charsets.UTF_8);
            List<String> stringList = bufferedReader.lines().collect(Collectors.toList());
            assertTrue(stringList.size() == 1);
            bufferedReader.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        try {
            BufferedWriter bufferedWriter = Files.newWriter(getFile("in8"), Charsets.UTF_8);
            bufferedWriter.write("来试试新的字符串");
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        //全路径
//        String simplifyPath = Files.simplifyPath(getFileFullName("in7"));

        String str = "这是一段写入文件的字符串";
        try {
            Files.write(str.getBytes(Charsets.UTF_8), getFile("in9"));
            Files.write(str, getFile("in9"), Charsets.UTF_8);
        } catch (IOException e) {
        }
    }

    private static void testCharStreams() {
        Writer writer = null;
        try {
            writer = CharStreams.asWriter(new FileWriter(createNewFile("out5")));
            writer.append("dsdfjlskdj丽枫酒店说了电视剧");
            writer.flush();
        } catch (IOException e) {
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
        try {
            FileReader in6 = new FileReader(createNewFile("in6"));
            FileWriter out6 = new FileWriter(createNewFile("out6"));
            long copy = CharStreams.copy(in6, out6);
            assertTrue(43 == copy);
        } catch (IOException e) {
        }
        try {
            FileReader in7 = new FileReader(createNewFile("in7"));
            long exhaust = CharStreams.exhaust(in7);
            assertTrue(43 == exhaust);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

//        一个不写入字符的writer
//        Writer nullWriter = CharStreams.nullWriter();
//        nullWriter.append("fsfsfsfdf");

        try {
            FileReader in8 = new FileReader(createNewFile("in8"));
            List<String> strings = CharStreams.readLines(in8);
            assertTrue(1 == strings.size());
            assertTrue(43 == strings.get(0).length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

        try {
            FileReader in9 = new FileReader(createNewFile("in9"));

            CharStreams.skipFully(in9, 36);

            assertEquals("名称为：in9", CharStreams.toString(in9));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private static void testByteStreams() {
        try {
            InputStream in1 = getNewInputStreamFile("in1");
            OutputStream out1 = getNewOutputStream("out1");
            ByteStreams.copy(in1, out1);
        } catch (IOException e) {
            log.error("文件流拷贝异常：{}", ExceptionUtils.getStackTrace(e));
        }

        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(getNewInputStreamFile("in2"));
            WritableByteChannel writableByteChannel = Channels.newChannel(getNewOutputStream("out2"));
            ByteStreams.copy(readableByteChannel, writableByteChannel);
        } catch (IOException e) {
            log.error("NIO 文件通道拷贝:{}", ExceptionUtils.getStackTrace(e));
        }

        try {
            long exhaust = ByteStreams.exhaust(getNewInputStreamFile("in3"));
            assertTrue(81 == exhaust);
        } catch (IOException e) {
            log.error("文件丢弃异常:{}", ExceptionUtils.getStackTrace(e));
        }

        InputStream in4 = ByteStreams.limit(getNewInputStreamFile("in4"), 64);
        try {
            //只能复制到 "这是一个新建的文件，建于2020-01-05 17:53:02 2，文�"
            ByteStreams.copy(in4, getNewOutputStream("out4"));
        } catch (IOException e) {
        }

        byte[] datain = new byte[64];
        try {
            InputStream in5 = getNewInputStreamFile("in5");
            in5.read(datain);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回一个新的ByteArrayDataInput实例，从头开始读取bytes数组.
//        ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(datain);
        //返回具有默认大小的新ByteArrayDataOutput实例.
//        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();

        try {
            ByteStreams.read(getNewInputStreamFile("in6"), datain, 5, 32);
        } catch (IOException e) {
        }
        assertTrue(64 == datain.length);

        try {
            byte[] byteArray = ByteStreams.toByteArray(getNewInputStreamFile("17"));
            assertTrue(80 == byteArray.length);
        } catch (IOException e) {
        }
    }

}
