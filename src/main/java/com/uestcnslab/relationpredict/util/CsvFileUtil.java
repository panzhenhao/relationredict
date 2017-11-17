/** 
 * Project Name:relationredict 
 * File Name:CsvFileUtil.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年11月16日下午4:27:39 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvWriter;

/**
 * @author pzh
 * @version $Id: CsvFileUtil.java, v 0.1 2017年11月16日 下午4:27:39 pzh Exp $
 */

public class CsvFileUtil {
    /**
     * CSV文件编码
     */
    public static final String ENCODE = "UTF-8";

    /**
     * 文件输入流 FileInputStream
     */
    private FileInputStream    fis    = null;
    /**
     * InputStreamReader
     */
    private InputStreamReader  isw    = null;
    /**
     * BufferedReader
     */
    private BufferedReader     br     = null;

    /** 
     * main:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param args
     * @throws Exception 
     *
     * @since JDK 1.8 
     */ 
    public static void main(String[] args) throws Exception {
        String path = CsvFileUtil.class.getClass().getResource("/").getPath();
        String filename = path + "data/word_pairs_final.9classes.csv";
        CsvFileUtil csvFileUtil = new CsvFileUtil(filename);
        String line = csvFileUtil.readLine();
        System.out.println(line);
        while ((line = csvFileUtil.readLine()) != null) {
            System.out.println(line);
        }
        String[] headers = { "编号", "姓名", "年龄" };
        List<String[]> list = new ArrayList<String[]>();
        String[] content = { "12365", "张山", "34" };
        list.add(content);
        list.add(content);
        csvWrite("/home/pzh/git/relationredict/src/main/resources/data/test.csv", headers, list);
    }

    /**
     * Creates a new instance of CsvFileUtil.
     * 
     * @param filename
     * @throws Exception
     */
    public CsvFileUtil(String filename) throws Exception {
        fis = new FileInputStream(filename);
        isw = new InputStreamReader(fis, ENCODE);
        br = new BufferedReader(isw);
    }

    // ==========以下是公开方法=============================
    /**
     * csvWrite:写csv文件. <br/>
     * 
     * @author pzh
     * @param filePath
     *            文件路径
     * @param headers
     *            文件头数组
     * @param contexts
     *            文件内容list
     *
     * @since JDK 1.8
     */
    public static void csvWrite(String filePath, String[] headers, List<String[]> contexts) {

        try {
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName(ENCODE));
            csvWriter.writeRecord(headers);
            for (String[] context : contexts) {
                csvWriter.writeRecord(context);
            }
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * readLine:从CSV文件流中读取一个CSV行. <br/>
     * 
     * @author pzh
     * @return 行字符串
     * @throws Exception
     *
     * @since JDK 1.8
     */
    public String readLine() throws Exception {

        StringBuffer readLine = new StringBuffer();
        boolean bReadNext = true;

        while (bReadNext) {
            //
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行
            String strReadLine = br.readLine();

            // readLine is Null
            if (strReadLine == null) {
                return null;
            }
            readLine.append(strReadLine);

            // 如果双引号是奇数的时候继续读取。考虑有换行的是情况。
            if (countChar(readLine.toString(), '"', 0) % 2 == 1) {
                bReadNext = true;
            } else {
                bReadNext = false;
            }
        }
        return readLine.toString();
    }

    /**
     * transformLineToArray: 把CSV文件的一行转换成字符串数组。指定数组长度，不够长度的部分设置为null. <br/>
     * 
     * @author pzh
     * @param source
     *            行字符串
     * @param size
     *            指定长度
     * @return 字符串数组
     *
     * @since JDK 1.8
     */
    public static String[] transformLineToArray(String source, int size) {
        ArrayList<String> tmpArray = transformLineToArray(source);
        if (size < tmpArray.size()) {
            size = tmpArray.size();
        }
        String[] rtnArray = new String[size];
        tmpArray.toArray(rtnArray);
        return rtnArray;
    }

    /**
     * transformLineToArray:把CSV文件的一行转换成字符串数组。不指定数组长度. <br/>
     * 
     * @author pzh
     * @param source
     *            行字符串
     * @return 字符串list
     *
     * @since JDK 1.8
     */
    public static ArrayList<String> transformLineToArray(String source) {
        if (source == null || source.length() == 0) {
            return new ArrayList<String>();
        }
        int currentPosition = 0;
        int maxPosition = source.length();
        int nextComma = 0;
        ArrayList<String> rtnArray = new ArrayList<String>();
        while (currentPosition < maxPosition) {
            nextComma = nextComma(source, currentPosition);
            rtnArray.add(nextToken(source, currentPosition, nextComma));
            currentPosition = nextComma + 1;
            if (currentPosition == maxPosition) {
                rtnArray.add("");
            }
        }
        return rtnArray;
    }

    /**
     * toCSVLine: 把字符串类型的数组转换成一个CSV行。（输出CSV文件的时候用）. <br/>
     * 
     * @author pzh
     * @param strArray
     *            字符串数组
     * @return 转换后的字串传
     *
     * @since JDK 1.8
     */
    public static String toCsvLine(String[] strArray) {
        if (strArray == null) {
            return "";
        }
        StringBuffer cvsLine = new StringBuffer();
        for (int idx = 0; idx < strArray.length; idx++) {
            String item = addQuote(strArray[idx]);
            cvsLine.append(item);
            if (strArray.length - 1 != idx) {
                cvsLine.append(',');
            }
        }
        return cvsLine.toString();
    }

    /**
     * toCSVLine:字符串类型的List转换成一个CSV行。（输出CSV文件的时候用）. <br/>
     * 
     * @author pzh
     * @param strArrList
     *            字串传list
     * @return 转换后的字符串
     *
     * @since JDK 1.8
     */
    public static String toCsvLine(ArrayList<String> strArrList) {
        if (strArrList == null) {
            return "";
        }
        String[] strArray = new String[strArrList.size()];
        for (int idx = 0; idx < strArrList.size(); idx++) {
            strArray[idx] = (String) strArrList.get(idx);
        }
        return toCsvLine(strArray);
    }

    // ==========以下是内部使用的方法=============================
    /**
     * 计算指定文字的个数。
     *
     * @param str
     *            文字列
     * @param c
     *            文字
     * @param start
     *            开始位置
     * @return 个数
     */
    private int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }

    /**
     * 查询下一个逗号的位置。
     *
     * @param source
     *            文字列
     * @param st
     *            检索开始位置
     * @return 下一个逗号的位置。
     */
    private static int nextComma(String source, int st) {
        int maxPosition = source.length();
        boolean inquote = false;
        while (st < maxPosition) {
            char ch = source.charAt(st);
            if (!inquote && ch == ',') {
                break;
            } else if ('"' == ch) {
                inquote = !inquote;
            }
            st++;
        }
        return st;
    }

    /**
     * 取得下一个字符串
     */
    private static String nextToken(String source, int st, int nextComma) {
        StringBuffer strb = new StringBuffer();
        int next = st;
        while (next < nextComma) {
            char ch = source.charAt(next++);
            if (ch == '"') {
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {
                    strb.append(ch);
                    next++;
                }
            } else {
                strb.append(ch);
            }
        }
        return strb.toString();
    }

    /**
     * 在字符串的外侧加双引号。如果该字符串的内部有双引号的话，把"转换成""。
     *
     * @param item
     *            字符串
     * @return 处理过的字符串
     */
    private static String addQuote(String item) {
        if (item == null || item.length() == 0) {
            return "\"\"";
        }
        StringBuffer sb = new StringBuffer();
        sb.append('"');
        for (int idx = 0; idx < item.length(); idx++) {
            char ch = item.charAt(idx);
            if ('"' == ch) {
                sb.append("\"\"");
            } else {
                sb.append(ch);
            }
        }
        sb.append('"');
        return sb.toString();
    }
}
