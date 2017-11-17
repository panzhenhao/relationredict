/** 
 * Project Name:relationredict 
 * File Name:Main.java 
 * Package Name:com.uestcnslab.relationpredict.main 
 * Date:2017年11月14日下午5:58:09 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/  
  
package com.uestcnslab.relationpredict.main;

import java.io.IOException;

import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.LoadModel;

/** @author pzh 
 *  @version $Id: Main.java, v 0.1 2017年11月14日 下午5:58:09 pzh Exp $ */

public class Main {

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
        //1.加载模型
        String path = LoadModel.class.getClass().getResource("/").getPath();
        WordVectorModel cbowModel = LoadModel.loadModel(path+"trunk/cbowVectors.bin");
        WordVectorModel skipModel = LoadModel.loadModel(path+"trunk/skipVectors.bin");
        WordVectorModel gloveModel = LoadModel.loadModel(path+"GloVe-1.2/vectors.bin");
        //2.加载训练集聚类模型
        //3.加载测试集
        //4.计算结果
    }

}
  