/** 
 * Project Name:relationredict 
 * File Name:SimilarModel.java 
 * Package Name:com.uestcnslab.relationpredict.model 
 * Date:2017年11月19日下午9:34:47 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/  
  
package com.uestcnslab.relationpredict.model;

import java.io.Serializable;

/** @author pzh 
 *  @version $Id: SimilarModel.java, v 0.1 2017年11月19日 下午9:34:47 pzh Exp $ */

public class SimilarModel implements Serializable,Comparable<SimilarModel>{
    /**
      * @Fields serialVersionUID 
      */
    private static final long serialVersionUID = 8390510721648562313L;
    /**
      * @Fields testRelation 测试关系
      */
    private String testRelation;
    /**
      * @Fields realRelation 真实关系
      */
    private String realRelation;
    /**
      * @Fields score 得分/相似度/距离
      */
    private double score;
    /** 
     * 获取 testRelation
     * @return testRelation 
     */
    public String getTestRelation() {
        return testRelation;
    }
    /** 
     * 设置 testRelation
     * @param testRelation  
     */
    public void setTestRelation(String testRelation) {
        this.testRelation = testRelation;
    }
    /** 
     * 获取 realRelation
     * @return realRelation 
     */
    public String getRealRelation() {
        return realRelation;
    }
    /** 
     * 设置 realRelation
     * @param realRelation  
     */
    public void setRealRelation(String realRelation) {
        this.realRelation = realRelation;
    }
    /** 
     * 获取 score
     * @return score 
     */
    public double getScore() {
        return score;
    }
    /** 
     * 设置 score
     * @param score  
     */
    public void setScore(double score) {
        this.score = score;
    }
    /** * @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((realRelation == null) ? 0 : realRelation.hashCode());
        long temp;
        temp = Double.doubleToLongBits(score);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((testRelation == null) ? 0 : testRelation.hashCode());
        return result;
    }
    /** * @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimilarModel other = (SimilarModel) obj;
        if (realRelation == null) {
            if (other.realRelation != null)
                return false;
        } else if (!realRelation.equals(other.realRelation))
            return false;
        if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score))
            return false;
        if (testRelation == null) {
            if (other.testRelation != null)
                return false;
        } else if (!testRelation.equals(other.testRelation))
            return false;
        return true;
    }
    /** * @see java.lang.Comparable#compareTo(java.lang.Object) */
    public int compareTo(SimilarModel o) {
        if (this.score > o.score) {
            return -1;
        } else {
            return 1;
        }
    }
    
}
  