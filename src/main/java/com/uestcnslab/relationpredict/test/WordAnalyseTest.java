/** 
 * Project Name:relationpredict 
 * File Name:WordAnalyseTest.java 
 * Package Name:com.uestcnslab.relationpredict.test 
 * Date:2017年10月31日下午6:30:45 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.test;

import java.io.IOException;

import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.LoadModel;
import com.uestcnslab.relationpredict.util.WordAnalyse;

/**
 * @author zhenhao.pzh
 * @version $Id: WordAnalyseTest.java, v 0.1 2017年10月31日 下午6:30:45 zhenhao.pzh
 *          Exp $
 */

public class WordAnalyseTest {

    /**
     * main:(这里用一句话描述这个方法的作用). <br/>
     * 
     * @author zhenhao.pzh
     * @param args
     * @throws IOException
     *
     * @since JDK 1.8
     */
    public static void main(String[] args) throws IOException {
        WordAnalyse wordAnalyse = new WordAnalyse();
        LoadModel loadModel = new LoadModel();
        String path = loadModel.getClass().getResource("/").getPath();
        WordVectorModel wordVectorModel = loadModel.loadModel(path + "trunk/cbowVectors.bin");
        System.out.println(wordAnalyse.distance("dog", wordVectorModel.getWordMap()));
        System.out.println(wordAnalyse.analogy("dog","leg","leg", wordVectorModel));
    }

}
