/** 
 * Project Name:relationpredict 
 * File Name:SpectralClustering.java 
 * Package Name:com.uestcnslab.relationpredict.clusterfirst 
 * Date:2017年12月19日下午7:12:55 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/  
  
package com.uestcnslab.relationpredict.clusterfirst.spectralcluster;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.AttributeModel;
import com.uestcnslab.relationpredict.model.WordVecRelationModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.LoadModel;

/** @author pzh 
 *  @version $Id: SpectralClustering.java, v 0.1 2017年12月19日 下午7:12:55 pzh Exp $ */

public class SpectralClustering {
    /**
     * @Fields logger : 日志模型
     */
    private static Logger logger = Logger.getLogger("SpectralClustering");
    public static void main(String[] args) {
        
        String path = LoadModel.class.getClass().getResource("/").getPath();
        //1.加载训练集聚类模型
        String filename = path + "cluster-first-data/train_cbow200_40_all.csv";
        List<AttributeModel> attributeModes = CsvFileUtil.loadClusterRelationModel(filename);
        logger.info("第一阶段：聚类模型加载完成！"); 
        List<String> relationVectors = new ArrayList<String>();
        List<String> idRelations = new ArrayList<String>();
        for (AttributeModel attributeModel: attributeModes) {
            String idRelation = attributeModel.getId()+"-"+attributeModel.getRelation();
            String relationVector = Arrays.toString(attributeModel.getRelationVector());
            relationVector = relationVector.replace("[", "");
            relationVector = relationVector.replace("]", "");
            relationVector = relationVector.replaceAll(",", "\t");
            idRelations.add(idRelation);
            relationVectors.add(relationVector);
        }
        //3.写文件
        filename = System.getProperty("user.dir")+"/src/main/resources/cluster-first-data/train_cbow200_40_relationVectors.txt";
        writeData(filename, relationVectors);
        filename = System.getProperty("user.dir")+"/src/main/resources/cluster-first-data/train_cbow200_40_idRelations.txt";
        writeData(filename, idRelations);
        logger.info("第五阶段：数据集全属性文件csv完成！");
    }
    /** 
     * writeData:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param filename
     * @param list 
     *
     * @since JDK 1.8 
     */ 
    private static void writeData(String filename, List<String> list) {

        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(writer);
            for (String str : list) {
                bw.write(str + "\n");
            }
            bw.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
  