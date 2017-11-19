/** 
 * Project Name:relationredict 
 * File Name:Main.java 
 * Package Name:com.uestcnslab.relationpredict.main 
 * Date:2017年11月14日下午5:58:09 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.main;

import java.util.List;

import com.uestcnslab.relationpredict.model.ClusterModel;
import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.model.WordVecPoint;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.LoadModel;
import com.uestcnslab.relationpredict.util.WordVecModelTransferUtil;

/**
 * @author pzh
 * @version $Id: Main.java, v 0.1 2017年11月14日 下午5:58:09 pzh Exp $
 */

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
        WordVectorModel cbowModel = LoadModel.loadModel(path + "trunk/cbowVectors.bin");
        // WordVectorModel skipModel = LoadModel.loadModel(path + "trunk/skipVectors.bin");
        //WordVectorModel gloveModel = LoadModel.loadModel(path+"GloVe-1.2/vectors.bin");
        
        //2.加载训练集聚类模型
        String filename = path + "data/train_core_cbow200_classify_cluster_5.csv";
        List<ClusterModel> ClusterModels = CsvFileUtil.loadClusterModel(filename);
        
        //3.加载测试集
        filename = path + "data/testset.csv";
        List<DataSetModel> testSet = CsvFileUtil.loadDataSet(filename);
        
        //4.计算关系向量模型
        List<WordVecPoint> relationVec = WordVecModelTransferUtil.getRelationVec(testSet, cbowModel);
        //4.计算结果
    }
}
