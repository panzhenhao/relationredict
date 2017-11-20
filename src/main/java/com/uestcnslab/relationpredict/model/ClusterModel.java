/** 
 * Project Name:relationredict 
 * File Name:ClusterModel.java 
 * Package Name:com.uestcnslab.relationpredict.model 
 * Date:2017年11月19日下午6:34:00 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.model;

import java.io.Serializable;

/**
 * @author pzh
 * @version $Id: ClusterModel.java, v 0.1 2017年11月19日 下午6:34:00 pzh Exp $
 */

public class ClusterModel implements Serializable {

    /**
     * @Fields serialVersionUID ）
     */
    private static final long serialVersionUID = 1L;
    /**
     * @Fields id
     */
    private int               id;
    /**
     * @Fields relation
     */
    private String            relation;
    /**
     * @Fields flag
     */
    private int               flag;
    /**
     * @Fields vector
     */
    private float[]           vector;

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

}
