/** 
 * Project Name:relationredict 
 * File Name:WordVecRelationModel.java 
 * Package Name:com.uestcnslab.relationpredict.model 
 * Date:2017年11月23日下午9:46:12 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author pzh
 * @version $Id: WordVecRelationModel.java, v 0.1 2017年11月23日 下午9:46:12 pzh Exp
 *          $
 */

public class WordVecRelationModel implements Serializable {

    /**
     * @Fields serialVersionUID
     */
    private static final long serialVersionUID = 5611759698701655810L;

    /**
     * @Fields wordFirst 第一个词
     */
    private String            wordFirst;
    /**
     * @Fields wordEnd 第二个词
     */
    private String            wordEnd;
    /**
     * 关系
     */
    private String            relation;
    /**
     * 向量
     */
    private float[]           vector;

    /**
     * 标示（聚类用）
     */
    private Integer           flag             = -1;

    /**
     * 获取 wordFirst
     * 
     * @return wordFirst
     */
    public String getWordFirst() {
        return wordFirst;
    }

    /**
     * 设置 wordFirst
     * 
     * @param wordFirst
     */
    public void setWordFirst(String wordFirst) {
        this.wordFirst = wordFirst;
    }

    /**
     * 获取 wordEnd
     * 
     * @return wordEnd
     */
    public String getWordEnd() {
        return wordEnd;
    }

    /**
     * 设置 wordEnd
     * 
     * @param wordEnd
     */
    public void setWordEnd(String wordEnd) {
        this.wordEnd = wordEnd;
    }

    /**
     * 获取 relation
     * 
     * @return relation
     */
    public String getRelation() {
        return relation;
    }

    /**
     * 设置 relation
     * 
     * @param relation
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * 获取 vector
     * 
     * @return vector
     */
    public float[] getVector() {
        return vector;
    }

    /**
     * 设置 vector
     * 
     * @param vector
     */
    public void setVector(float[] vector) {
        this.vector = vector;
    }

    /**
     * 获取 flag
     * 
     * @return flag
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * 设置 flag
     * 
     * @param flag
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /** * @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return "WordVecRelationModel [wordFirst=" + wordFirst + ", wordEnd=" + wordEnd
               + ", relation=" + relation + ", vector=" + Arrays.toString(vector) + ", flag=" + flag
               + "]";
    }

}
