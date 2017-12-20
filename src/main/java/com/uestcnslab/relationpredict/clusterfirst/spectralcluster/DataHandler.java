/** 
 * Project Name:relationpredict 
 * File Name:DataHandler.java 
 * Package Name:com.uestcnslab.relationpredict.clusterfirst.spectralcluster 
 * Date:2017年12月20日下午12:18:03 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.clusterfirst.spectralcluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.AttributeModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.LoadModel;

/**
 * @author pzh
 * @version $Id: DataHandler.java, v 0.1 2017年12月20日 下午12:18:03 pzh Exp $
 */

public class DataHandler {
    /**
     * @Fields logger : 日志模型
     */
    private static Logger logger = Logger.getLogger("DataHandler");

    public static void main(String[] args) {
        String path = LoadModel.class.getClass().getResource("/").getPath();
        //1.加载训练集聚类模型
        int n = 40;//聚类中心
        String filename = path + "cluster-first-data/cluster_result.txt";
        File file = new File(filename);
        BufferedReader reader = null;
        String temp = null;
        List<AttributeModel> attributeModes = new ArrayList<AttributeModel>();
        try {
            reader = changeAttributeModel(file, attributeModes);
            
            Map<Integer,float[]> map = new HashMap<Integer, float[]>();
            Map<Integer, Integer> count = new HashMap<Integer, Integer>();
            for (int i = 0; i < n; i++) {
                float[] vector = new float[200];
                count.put(i, 0);
                map.put(i, vector);
                for (AttributeModel attributeModel: attributeModes) {
                    if (attributeModel.getFlag()==i) {
                        float[]  relationVector = attributeModel.getRelationVector();
                        addTomap(map,i,relationVector,count);
                    }
                } 
            }
            for (int key: map.keySet()) {
                float[] coreVector = map.get(key);
                int num = count.get(key);
                for (int i = 0; i < coreVector.length; i++) {
                    coreVector[i] = coreVector[i] /num;
                }
            }
            for (AttributeModel attributeModel :attributeModes) {
                Integer key = attributeModel.getFlag();
                float[] coreVector = map.get(key);
                attributeModel.setCoreVector(coreVector);
            }
            filename = System.getProperty("user.dir")+"/src/main/resources/cluster-first-data/train_all_cbow200_40_sc.csv";
            CsvFileUtil.writeDataClusterToCsv(filename, attributeModes);
            logger.info("第1阶段：数据集全属性文件csv完成！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static BufferedReader changeAttributeModel(File file,
                                                       List<AttributeModel> attributeModes) throws FileNotFoundException,
                                                                                            IOException {
        BufferedReader reader;
        String temp;
        reader = new BufferedReader(new FileReader(file));
        while ((temp = reader.readLine()) != null) {
            String[] data = temp.split(" ");
            AttributeModel attributeMode = new AttributeModel();
            String[] idrelation = data[1].split("-");
            Integer id = Integer.parseInt(idrelation[0]);
            String relation = idrelation[1];
            attributeMode.setId(id);
            attributeMode.setFlag(Integer.parseInt(data[0]));
            attributeMode.setRelation(relation);
            float[] vector = new float[data.length - 2];
            for (int i = 2; i < data.length; i++) {
                vector[i - 2] = Float.valueOf(data[i]);
            }
            attributeMode.setRelationVector(vector);
            attributeModes.add(attributeMode);
        }
        return reader;
    }

    private static void addTomap(Map<Integer, float[]> map, int i, float[] relationVector, Map<Integer, Integer> count) {
        float[] vector = map.get(i);
        for (int j = 0; j < vector.length; j++) {
            vector[j] = vector[j]+relationVector[j];
        }
        map.put(i, vector);
        count.put(i,count.get(i)+1);
    }

}
