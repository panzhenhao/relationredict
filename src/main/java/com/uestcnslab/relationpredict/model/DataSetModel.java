/** 
 * Project Name:relationpredict 
 * File Name:DataSetModel.java 
 * Package Name:com.uestcnslab.relationpredict.model 
 * Date:2017年11月1日下午12:53:13 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.model;

import java.io.Serializable;

/**
 * @author zhenhao.pzh
 * @version $Id: DataSetModel.java, v 0.1 2017年11月1日 下午12:53:13 zhenhao.pzh Exp
 *          $
 */

public class DataSetModel  implements Serializable{

    /**
      * @Fields serialVersionUID 
      */
    private static final long serialVersionUID = 1L;
    /**
     * relation 关系
     */
    private String relation;
    /**
     * wordFirst 第一个词
     */
    private String wordFirst;
    /**
     * wordEnd 第二个词
     */
    private String wordEnd;

    /**
     * 获取 关系名称
     * 
     * @return 名称
     */
    public String getRelation() {
        return relation;
    }

    /**
     * 设置 关系名称
     * 
     * @param relation
     *            关系名称
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * 获取 第一个词
     * 
     * @return 第一个词
     */
    public String getWordFirst() {
        return wordFirst;
    }

    /**
     * 设置 第一个词
     * 
     * @param wordFirst
     *            第一个词
     */
    public void setWordFirst(String wordFirst) {
        this.wordFirst = wordFirst;
    }

    /**
     * 获取 第二个词
     * 
     * @return 第二个词
     */
    public String getWordEnd() {
        return wordEnd;
    }

    /**
     * 设置 第二个词
     * 
     * @param wordEnd
     *            第二个词
     */
    public void setWordEnd(String wordEnd) {
        this.wordEnd = wordEnd;
    }

    /** * @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return "DataSetModel [relation=" + relation + ", wordFirst=" + wordFirst + ", wordEnd="
               + wordEnd + "]";
    }
    
}
