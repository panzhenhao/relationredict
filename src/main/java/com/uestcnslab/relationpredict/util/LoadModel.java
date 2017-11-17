/** 
 * Project Name:relationpredict 
 * File Name:LoadModel.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年10月30日下午7:00:38 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.WordVectorModel;

/**
 * @author zhenhao.pzh
 * @version $Id: LoadModel.java, v 0.1 2017年10月30日 下午7:00:38 zhenhao.pzh Exp $
 */

public class LoadModel {
    /**
     * @Fields logger : 日志模型
     */
    static Logger                   logger   = Logger.getLogger("LoadModel");
    /**
     * @Fields MAX_SIZE     每次读取单词长度数组大小
     */
    private static final int MAX_SIZE = 50;

    /**
     * loadModel:加载模型. <br/>
     * 
     * @author zhenhao.pzh
     * @param path
     *            模型位置
     * @return 返回模型结果
     * @throws IOException
     *
     * @since JDK 1.8
     */
    public static WordVectorModel loadModel(String path) throws IOException {
        logger.info("加载模型：" + path);
        WordVectorModel wordVectorModel = new WordVectorModel();
        HashMap<String, float[]> wordMap = new HashMap<String, float[]>();

        DataInputStream dis = null;
        BufferedInputStream bis = null;
        double len = 0;
        float vector = 0;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
            dis = new DataInputStream(bis);
            // 读取词数 模型中向量的个数
            int words = Integer.parseInt(readString(dis));
            // 读取维度 词向量训练的维度
            int size = Integer.parseInt(readString(dis));
            wordVectorModel.setCount(words);
            wordVectorModel.setSize(size);
            String word;
            float[] vectors = null;
            for (int i = 0; i < words; i++) {
                // 词向量的key
                word = readString(dis);
                // 词向量
                vectors = new float[size];
                len = 0;
                for (int j = 0; j < size; j++) {
                    vector = readFloat(dis);
                    len += vector * vector;
                    vectors[j] = (float) vector;
                }
                len = Math.sqrt(len);

                for (int j = 0; j < vectors.length; j++) {
                    vectors[j] = (float) (vectors[j] / len);
                }
                wordMap.put(word, vectors);
                dis.read();
            }

        } finally {
            // bis.close();
            // dis.close();
        }

        wordVectorModel.setWordMap(wordMap);
        return wordVectorModel;
    }

    /**
     * readString: 读取单词. <br/>
     * 
     * @author zhenhao.pzh
     * @param dis
     *            数据输入流
     * @return 字符串
     * @throws IOException
     *
     * @since JDK 1.8
     */
    private static String readString(DataInputStream dis) throws IOException {
        byte[] bytes = new byte[MAX_SIZE];
        byte b = dis.readByte();
        int i = -1;
        StringBuilder sb = new StringBuilder();
        while (b != 32 && b != 10) {
            i++;
            bytes[i] = b;
            b = dis.readByte();
            if (i == 49) {
                sb.append(new String(bytes));
                i = -1;
                bytes = new byte[MAX_SIZE];
            }
        }
        sb.append(new String(bytes, 0, i + 1));
        return sb.toString();
    }

    /**
     * readFloat:读取一个float. <br/>
     * 
     * @author zhenhao.pzh
     * @param is
     *            输入流
     * @return float
     * @throws IOException
     *
     * @since JDK 1.8
     */
    private static float readFloat(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
        return getFloat(bytes);
    }

    /**
     * 将 byte[] 转为float
     * 
     * @param b
     *            byte[]
     * @return float
     */
    private static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    /**
     * loadGloveModel: 加载glove模型. <br/>
     * 
     * @author pzh
     * @param path
     *            文件路径
     * @return 词向量模型
     *
     * @since JDK 1.8
     */
    public static WordVectorModel loadGloveModel(String path) {
        logger.info("加载模型：" + path);
        WordVectorModel wordVectorModel = new WordVectorModel();
        try {
            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            String str = br.readLine();
            int count = Integer.parseInt(str.split(" ")[0]);
            int size = Integer.parseInt(str.split(" ")[1]);
            wordVectorModel.setCount(count);
            wordVectorModel.setSize(size);
            HashMap<String,float[]> wordMap = new HashMap<String, float[]>();
            while ((str = br.readLine()) != null) {
                
                String[] keyAndvalue = str.split(" ");
                String key = keyAndvalue[0];
                float[] value = new float[size];
                for (int i = 1; i < keyAndvalue.length; i++) {
                    value[i-1] = Float.parseFloat(keyAndvalue[i]);
                }
                wordMap.put(key, value);
            }
            wordVectorModel.setWordMap(wordMap);
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordVectorModel;
    }
}
