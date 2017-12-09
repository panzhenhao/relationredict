/** 
 * Project Name:relationredict 
 * File Name:AttributeMode.java 
 * Package Name:com.uestcnslab.relationpredict.model 
 * Date:2017年11月24日下午9:03:19 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.model;

import java.io.Serializable;

/**
 * @author pzh
 * @version $Id: AttributeMode.java, v 0.1 2017年11月24日 下午9:03:19 pzh Exp $
 */

public class AttributeModel implements Serializable {

    /**
     * @Fields serialVersionUID : TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 2507238131289488365L;

    /**
     * @Fields id
     */
    private int               id;
    /**
     * @Fields relation word1-word2的关系
     */
    private String            relation;
    /**
     * @Fields word1 第一个单词
     */
    private String            word1;
    /**
     * @Fields word2 第二个单词
     */
    private String            word2;
    /**
     * @Fields flag 所属聚类中心
     */
    private int               flag;
    /**
     * @Fields relationVector 关系向量
     */
    private float[]           relationVector;
    /**
     * @Fields word1Vector word1的词向量
     */
    private float[]           word1Vector;
    /**
     * @Fields word2Vector word２的词向量）
     */
    private float[]           word2Vector;
    /**
     * @Fields coreVector 聚类中心向量
     */
    private float[]           coreVector;

    /**
     * @Fields distance 关系向量距聚类中心的距离
     */
    private double            distance;

    /**
     * 获取 id
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置 id
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
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
     * 获取 word1
     * 
     * @return word1
     */
    public String getWord1() {
        return word1;
    }

    /**
     * 设置 word1
     * 
     * @param word1
     */
    public void setWord1(String word1) {
        this.word1 = word1;
    }

    /**
     * 获取 word2
     * 
     * @return word2
     */
    public String getWord2() {
        return word2;
    }

    /**
     * 设置 word2
     * 
     * @param word2
     */
    public void setWord2(String word2) {
        this.word2 = word2;
    }

    /**
     * 获取 flag
     * 
     * @return flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * 设置 flag
     * 
     * @param flag
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * 获取 relationVector
     * 
     * @return relationVector
     */
    public float[] getRelationVector() {
        return relationVector;
    }

    /**
     * 设置 relationVector
     * 
     * @param relationVector
     */
    public void setRelationVector(float[] relationVector) {
        this.relationVector = relationVector;
    }

    /**
     * 获取 word1Vector
     * 
     * @return word1Vector
     */
    public float[] getWord1Vector() {
        return word1Vector;
    }

    /**
     * 设置 word1Vector
     * 
     * @param word1Vector
     */
    public void setWord1Vector(float[] word1Vector) {
        this.word1Vector = word1Vector;
    }

    /**
     * 获取 word2Vector
     * 
     * @return word2Vector
     */
    public float[] getWord2Vector() {
        return word2Vector;
    }

    /**
     * 设置 word2Vector
     * 
     * @param word2Vector
     */
    public void setWord2Vector(float[] word2Vector) {
        this.word2Vector = word2Vector;
    }

    /**
     * 获取 coreVector
     * 
     * @return coreVector
     */
    public float[] getCoreVector() {
        return coreVector;
    }

    /**
     * 设置 coreVector
     * 
     * @param coreVector
     */
    public void setCoreVector(float[] coreVector) {
        this.coreVector = coreVector;
    }

    /**
     * 获取 distance
     * 
     * @return distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * 设置 distance
     * 
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

}
