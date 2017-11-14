/** 
 * Project Name:relationpredict 
 * File Name:LoadDataSetTest.java 
 * Package Name:com.uestcnslab.relationpredict.test 
 * Date:2017年11月1日下午2:26:08 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.test;

import com.uestcnslab.relationpredict.util.LoadDataSet;

/** @author zhenhao.pzh 
 *  @version $Id: LoadDataSetTest.java, v 0.1 2017年11月1日 下午2:26:08 zhenhao.pzh Exp $ */

public class LoadDataSetTest {

    /** 
     * main:测试函数. <br/> 
     * 
     * @author zhenhao.pzh 
     * @param args 
     *
     * @since JDK 1.8 
     */
    public static void main(String[] args) {
        LoadDataSet loadDataSet = new LoadDataSet();
        String path = loadDataSet.getClass().getResource("/").getPath();
        path = path + "data/word_pairs_final.9classes.csv";
        System.out.println(loadDataSet.loadDataFormCsv(path));
    }
}
