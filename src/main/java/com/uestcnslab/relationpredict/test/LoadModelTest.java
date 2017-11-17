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
import java.util.Arrays;

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
        try {
           String path = LoadModel.class.getClass().getResource("/").getPath();
           WordVectorModel wordVectorModel = LoadModel.loadModel(path+"trunk/cbowVectors.bin");
           System.out.println(wordVectorModel.getCount());
           System.out.println(wordVectorModel.getSize());
           System.out.println(Arrays.toString(wordVectorModel.getWordMap().get("the")));
           WordVectorModel wordVectorModel2 = LoadModel.loadModel(path+"trunk/skipVectors.bin");
           System.out.println(wordVectorModel2.getCount());
           System.out.println(wordVectorModel2.getSize());
           System.out.println(Arrays.toString(wordVectorModel2.getWordMap().get("the")));
           WordVectorModel wordVectorModel3 = LoadModel.loadGloveModel(path+"GloVe-1.2/vectors.bin");
           System.out.println(wordVectorModel3.getCount());
           System.out.println(wordVectorModel3.getSize());
           System.out.println(Arrays.toString(wordVectorModel3.getWordMap().get("the")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
