/** 
 * Project Name:relationredict 
 * File Name:Main.java 
 * Package Name:com.uestcnslab.relationpredict.main 
 * Date:2017年11月14日下午5:58:09 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uestcnslab.relationpredict.model.AttributeMode;
import com.uestcnslab.relationpredict.model.ClusterModel;
import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.model.SimilarModel;
import com.uestcnslab.relationpredict.model.WordVecPoint;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.DataUtil;
import com.uestcnslab.relationpredict.util.Distance;
import com.uestcnslab.relationpredict.util.LoadModel;
import com.uestcnslab.relationpredict.util.WordVecModelTransferUtil;

/**
 * @author pzh
 * @version $Id: Main.java, v 0.1 2017年11月14日 下午5:58:09 pzh Exp $
 */

public class DataHandler {
    /**
     * @Fields SIZE 相似度保留前几个
     */
    private static Integer SIZE = 1;

    /**
     * @Fields TP 正确被检索到
     */
    private static String  TP   = "tp";
    /**
     * @Fields FP 错误被检索到的
     */
    private static String  FP   = "fp";
    /**
     * @Fields FN 未被检索到的
     */
    private static String  FN   = "fn";

    /**
     * main: 给定一个词组判断这个词组是什么关系. <br/>
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
        String filename = path + "data/train_all_cbow200_classify_cluster_5_relation.csv";
        List<AttributeMode> attributeModes = CsvFileUtil.loadClusterRelationModel(filename);

        //3 加载聚类中心
        filename = path + "data/train_core_cbow200_classify_cluster_5_relation.csv";
        List<AttributeMode> coreAttributeModes = CsvFileUtil.loadClusterRelationModel(filename);

        //4整合数据模型
        integrateAttributeMode(attributeModes, coreAttributeModes, cbowModel);
        System.out.println();
    }

    /**
     * integrateAttributeMode:整合模型数据. <br/>
     * 
     * @author pzh
     * @param attributeModes
     * @param coreAttributeModes
     * @param cbowModel
     *
     * @since JDK 1.8
     */
    private static void integrateAttributeMode(List<AttributeMode> attributeModes,
                                               List<AttributeMode> coreAttributeModes,
                                               WordVectorModel wordVectorModel) {
        //词向量模型
        Map<String, float[]> wordMap = wordVectorModel.getWordMap();
        //聚类中心个数
        int n = coreAttributeModes.size() / 9;
        Map<String, AttributeMode[]> coreMap = new HashMap<String, AttributeMode[]>();
        for (AttributeMode coreAttributeMode : coreAttributeModes) {
            addToCoreMap(coreMap, coreAttributeMode, n);
        }
        for (AttributeMode attributeMode : attributeModes) {
            String relation = attributeMode.getRelation();
            String word1 = attributeMode.getWord1();
            String word2 = attributeMode.getWord2();
            int flag = attributeMode.getFlag();
            int core = flag - 1;
            attributeMode.setWord1Vector(wordMap.get(word1));
            attributeMode.setWord2Vector(wordMap.get(word2));
            attributeMode.setCoreVector(coreMap.get(relation)[core].getCoreVector());
            double distance = Distance.pointDistance(attributeMode);
            attributeMode.setDistance(distance);
        }
    }

    /**
     * addToCoreMap:将聚类中心填充到固定位置上. <br/>
     * 
     * @author pzh
     * @param coreMap
     * @param attributeMode
     * @param n
     *
     * @since JDK 1.8
     */
    private static void addToCoreMap(Map<String, AttributeMode[]> coreMap,
                                     AttributeMode attributeMode, int n) {
        String coreRelation = attributeMode.getRelation();
        int id = attributeMode.getId();
        int index = id % n;
        if (coreMap.containsKey(coreRelation)) {
            coreMap.get(coreRelation)[index] = attributeMode;
        } else {
            AttributeMode[] attributeModes = new AttributeMode[n];
            attributeModes[index] = attributeMode;
            coreMap.put(coreRelation, attributeModes);
        }
    }
}
