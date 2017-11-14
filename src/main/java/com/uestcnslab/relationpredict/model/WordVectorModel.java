/** 
 * Project Name:relationpredict 
 * File Name:WordVectorModel.java 
 * Package Name:com.uestcnslab.relationpredict.model 
 * Date:2017年10月30日下午7:35:23 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author zhenhao.pzh
 * @version $Id: WordVectorModel.java, v 0.1 2017年10月30日 下午7:35:23 zhenhao.pzh
 *          Exp $
 */

public class WordVectorModel implements Serializable {
    /**
     * @Fields serialVersionUID
     */
    private static final long        serialVersionUID = 1L;
    /**
     * 模型中词向量的个数
     */
    private int                      count;
    /**
     * 词向量的维度
     */
    private int                      size;
    /**
     * 模型key为单词，value为向量值
     */
    private HashMap<String, float[]> wordMap;

    /**
     * 获取 模型词个数
     * 
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置 模型词个数
     * 
     * @param count
     *            型词个数
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取 向量维度
     * 
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置 向量维度
     * 
     * @param size
     *            向量维度
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 获取 模型
     * 
     * @return wordMap
     */
    public HashMap<String, float[]> getWordMap() {
        return wordMap;
    }

    /**
     * 设置 模型
     * 
     * @param wordMap
     *            模型
     */
    public void setWordMap(HashMap<String, float[]> wordMap) {
        this.wordMap = wordMap;
    }

}
