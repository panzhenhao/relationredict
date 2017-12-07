/** 
 * Project Name:relationpredict 
 * File Name:RatioStatistic.java 
 * Package Name:com.uestcnslab.relationpredict.clusterfirst 
 * Date:2017年12月7日下午6:43:04 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/  
  
package com.uestcnslab.relationpredict.clusterfirst;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.AttributeMode;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.Distance;
import com.uestcnslab.relationpredict.util.LoadModel;

/** @author pzh 
 *  @version $Id: RatioStatistic.java, v 0.1 2017年12月7日 下午6:43:04 pzh Exp $ */

public class RatioStatistic {
    /**
     * @Fields logger : 日志模型
     */
    private static Logger logger = Logger.getLogger("RatioStatistic");
    /** 
     * main:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param args 
     * @throws IOException 
     *
     * @since JDK 1.8 
     */ 
    public static void main(String[] args) throws IOException {
        //1.加载模型
        String path = LoadModel.class.getClass().getResource("/").getPath();
        WordVectorModel cbowModel = LoadModel.loadModel(path + "trunk/cbowVectors.bin");
        // WordVectorModel skipModel = LoadModel.loadModel(path + "trunk/skipVectors.bin");
        //WordVectorModel gloveModel = LoadModel.loadModel(path+"GloVe-1.2/vectors.bin");
        logger.info("第一阶段：模型加载完成！");

        //2.加载训练集聚类模型
        String filename = path + "cluster-first-data/train_all_cbow200_50.csv";
        List<AttributeMode> attributeModes = CsvFileUtil.loadClusterRelationModel(filename);
        logger.info("第二阶段：训练集聚类模型加载完成！");
        
        //3 加载聚类中心
        filename = path + "cluster-first-data/train_core_cbow200_50.csv";
        List<AttributeMode> coreAttributeModes = CsvFileUtil.loadClusterRelationModel(filename);
        logger.info("第三阶段：聚类中心模型加载完成！");
        
        //4 整合数据模型
        integrateAttributeMode(attributeModes, coreAttributeModes, cbowModel);
        logger.info("第四阶段：整合数据模型完成！");
    }
    /**
     * integrateAttributeMode:整合模型数据. <br/>
     * 
     * @author pzh
     * @param attributeModes
     * @param coreAttributeModes
     * @param wordVectorModel
     *
     * @since JDK 1.8
     */
    private static void integrateAttributeMode(List<AttributeMode> attributeModes,
                                               List<AttributeMode> coreAttributeModes,
                                               WordVectorModel wordVectorModel) {
        //词向量模型
        Map<String, float[]> wordMap = wordVectorModel.getWordMap();
     
        Map<Integer, AttributeMode> coreMap = new HashMap<Integer, AttributeMode>();
        for (AttributeMode coreAttributeMode : coreAttributeModes) {
            coreMap.put(coreAttributeMode.getId(), coreAttributeMode);
        }
        for (AttributeMode attributeMode : attributeModes) {
            String word1 = attributeMode.getWord1();
            String word2 = attributeMode.getWord2();
            int flag = attributeMode.getFlag();
            if (flag==0) {
                continue;
            }
            int core = flag - 1;
            attributeMode.setWord1Vector(wordMap.get(word1));
            attributeMode.setWord2Vector(wordMap.get(word2));
            attributeMode.setCoreVector(coreMap.get(core).getCoreVector());
            double distance = Distance.pointDistance(attributeMode);
            attributeMode.setDistance(distance);
        }
    }
    
   
}
  