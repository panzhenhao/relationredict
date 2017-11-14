/** 
 * Project Name:relationredict 
 * File Name:WordVecPoint.java 
 * Package Name:com.uestcnslab.relationpredict.model 
 * Date:2017年11月14日下午5:50:22 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/  
  
package com.uestcnslab.relationpredict.model;

import java.io.Serializable;

/** @author pzh 
 *  @version $Id: WordVecPoint.java, v 0.1 2017年11月14日 下午5:50:22 pzh Exp $ */

public class WordVecPoint implements Serializable{

    /**
      * @Fields serialVersionUID 
      */
    private static final long serialVersionUID = 1L;
    
    /**
      * 向量名
      */
    private String word;
    /**
      * 向量
      */
    private float[] vector;
    
    /**
      * 标示（聚类用）
      */
    private Integer flag = -1;
    /** 
     * 获取 word
     * @return word 
     */
    public String getWord() {
        return word;
    }
    /** 
     * 设置 word
     * @param word  
     */
    public void setWord(String word) {
        this.word = word;
    }
    /** 
     * 获取 vector
     * @return vector 
     */
    public float[] getVector() {
        return vector;
    }
    /** 
     * 设置 vector
     * @param vector  
     */
    public void setVector(float[] vector) {
        this.vector = vector;
    }
    /** 
     * 获取 flag
     * @return flag 
     */
    public Integer getFlag() {
        return flag;
    }
    /** 
     * 设置 flag
     * @param flag  
     */ 
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
  