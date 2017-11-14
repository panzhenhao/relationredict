/** 
 * Project Name:relationpredict 
 * File Name:LoadModelTest.java 
 * Package Name:com.uestcnslab.relationpredict 
 * Date:2017年10月31日下午5:04:07 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/
package com.uestcnslab.relationpredict.test;

import java.io.IOException;
import java.nio.file.Path;

import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.LoadModel;

/**
 * @author zhenhao.pzh
 * @version $Id: LoadModelTest.java, v 0.1 2017年10月31日 下午5:04:07 zhenhao.pzh Exp
 *          $
 */

public class LoadModelTest {

    /**
     * main:(这里用一句话描述这个方法的作用). <br/>
     * 
     * @author zhenhao.pzh
     * @param args
     *
     * @since JDK 1.8
     */
    public static void main(String[] args) {
        LoadModel loadModel = new LoadModel();
        try {
           String path = loadModel.getClass().getResource("/").getPath();
           WordVectorModel wordVectorModel = loadModel.loadModel(path+"trunk/cbowVectors.bin");
           System.out.println(wordVectorModel.getCount());
           System.out.println(wordVectorModel.getSize());
           WordVectorModel wordVectorModel2 = loadModel.loadModel(path+"trunk/skipVectors.bin");
           System.out.println(wordVectorModel2.getCount());
           System.out.println(wordVectorModel2.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
