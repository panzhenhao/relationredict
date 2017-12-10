/** 
 * Project Name:relationpredict 
 * File Name:RatioStatistic.java 
 * Package Name:com.uestcnslab.relationpredict 
 * Date:2017年12月8日下午2:03:10 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.clusterfirst;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.AttributeModel;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.DataUtil;
import com.uestcnslab.relationpredict.util.Distance;
import com.uestcnslab.relationpredict.util.LoadModel;

/**
 * @author pzh
 * @version $Id: RatioStatistic.java, v 0.1 2017年12月8日 下午2:03:10 pzh Exp $
 */

public class RatioStatistic {
    /**
     * @Fields logger : 日志模型
     */
    private static Logger logger = Logger.getLogger("ModelComplement");

    /**
     * main:(这里用一句话描述这个方法的作用). <br/>
     * 
     * @author pzh
     * @param args
     *
     * @since JDK 1.8
     */
    public static void main(String[] args) {

        String path = LoadModel.class.getClass().getResource("/").getPath();
        //1.加载训练集聚类模型
        String filename = path + "cluster-first-data/train_cbow200_10_all.csv";
        List<AttributeModel> attributeModes = CsvFileUtil.loadClusterRelationModel(filename);
        logger.info("第一阶段：聚类模型加载完成！");

        //2.各个聚类中心数据条数key：聚类中心value：条数
        Map<Integer, Integer> coreData = countCoreData(attributeModes);
        logger.info("第二阶段：聚类中心条数统计完成！");

        //3. 获取各个关系的聚类分布
        int[][] relationCoreData = getRelationDistribution(attributeModes, coreData);
        logger.info("第三阶段：获取各个关系的聚类分布完成！");

        //4.分布比例
        float[][] distribution = getDistributionRatio(coreData, relationCoreData);
        logger.info("第四阶段：各个聚类中心比例统计完成！");

        //5.聚类中心map
        Map<Integer, float[]> coreMap = getCoreMap(attributeModes);
        logger.info("第五阶段：获取聚类中心map完成！");

        //6.生成训练集
        generateTrainData(attributeModes, distribution, coreMap);
        logger.info("第六阶段：训练集生成完成！");

    }

    /**
     * generateTrainData:生成训练集. <br/>
     * 
     * @author pzh
     * @param attributeModes
     * @param distribution
     * @param coreMap
     *
     * @since JDK 1.8
     */
    private static void generateTrainData(List<AttributeModel> attributeModes,
                                          float[][] distribution, Map<Integer, float[]> coreMap) {
        String filename;
        System.out.println(attributeModes.size());
        for (int i = 0; i < distribution.length; i++) {
            List<String> data = new ArrayList<String>();
            String trainRelation = DataUtil.idRelationMap.get(i);
            //真实关系
            int count = 0;
            for (AttributeModel attributeMode : attributeModes) {
                float[] dis = new float[distribution[0].length + 1];
                String relation = attributeMode.getRelation();
                float[] relationVector = attributeMode.getRelationVector();
                for (int key : coreMap.keySet()) {
                    float[] coreVector = coreMap.get(key);
                    double distance = Distance.pointDistanceCos(relationVector, coreVector);
                    //占比*距离倒数              
                    dis[key] = (float) (distribution[i][key] / distance);
                }
                if (trainRelation.equals(relation)) {
                    dis[dis.length - 1] = 1;
                    count++;
                } else {
                    dis[dis.length - 1] = 0;
                }
                String str = Arrays.toString(dis);
                str = str.replaceAll(",", "");
                str = str.substring(1, str.length() - 1);
                data.add(str);

            }
            System.out.println(count);
            System.out.println(data.size());
            filename = System.getProperty("user.dir")
                       + "/src/main/resources/cluster-first-data/train_" + trainRelation + "_"
                       + coreMap.keySet().size();
            writeFile(filename, data);
        }
    }

    /**
     * writeFile:(这里用一句话描述这个方法的作用). <br/>
     * 
     * @author pzh
     * @param filename
     * @param data
     *
     * @since JDK 1.8
     */
    private static void writeFile(String filename, List<String> data) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(writer);
            for (String str : data) {
                bw.write(str + "\n");
            }
            bw.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * getCoreMap:聚类中心map. <br/>
     * 
     * @author pzh
     * @param attributeModes
     * @return
     *
     * @since JDK 1.8
     */
    private static Map<Integer, float[]> getCoreMap(List<AttributeModel> attributeModes) {
        Map<Integer, float[]> coreMap = new HashMap<Integer, float[]>();
        for (AttributeModel attributeMode : attributeModes) {
            int flag = attributeMode.getFlag() - 1;
            float[] vector = attributeMode.getCoreVector();
            if (coreMap.containsKey(flag)) {
                continue;
            } else {
                coreMap.put(flag, vector);
            }
        }
        return coreMap;
    }

    /**
     * getDistributionRatio:获取分布比例. <br/>
     * 
     * @author pzh
     * @param coreData
     * @param relationCoreData
     * @return
     *
     * @since JDK 1.8
     */
    private static float[][] getDistributionRatio(Map<Integer, Integer> coreData,
                                                  int[][] relationCoreData) {
        float[][] distribution = new float[relationCoreData.length][relationCoreData[0].length];
        for (int i = 0; i < relationCoreData.length; i++) {
            for (int j = 0; j < relationCoreData[i].length; j++) {
                distribution[i][j] = (float) relationCoreData[i][j] / coreData.get(j + 1);
            }
        }
        return distribution;
    }

    /**
     * getRelationDistribution:获取各个关系的聚类分布. <br/>
     * 
     * @author pzh
     * @param attributeModes
     * @param coreData
     * @return 分布信息
     *
     * @since JDK 1.8
     */
    private static int[][] getRelationDistribution(List<AttributeModel> attributeModes,
                                                   Map<Integer, Integer> coreData) {
        //聚类中心个数
        int core = coreData.keySet().size();
        int relationType = DataUtil.trainSetRelations.keySet().size();
        int[][] relationCoreData = new int[relationType][core];
        for (AttributeModel attributeMode : attributeModes) {
            //哪一行
            String relation = attributeMode.getRelation();
            int line = DataUtil.relationIdMap.get(relation);
            //哪一列:需要减一
            int flag = attributeMode.getFlag();
            relationCoreData[line][flag - 1]++;
        }
        return relationCoreData;
    }

    /**
     * countCoreData:各个聚类中心数据条数key：聚类中心value：条数. <br/>
     * 
     * @author pzh
     * @param attributeModes
     * @return
     *
     * @since JDK 1.8
     */
    private static Map<Integer, Integer> countCoreData(List<AttributeModel> attributeModes) {
        Map<Integer, Integer> coreData = new HashMap<Integer, Integer>();
        for (AttributeModel attributeMode : attributeModes) {
            Integer key = attributeMode.getFlag();
            if (coreData.containsKey(key)) {
                coreData.put(key, coreData.get(key) + 1);
            } else {
                coreData.put(key, 1);
            }
        }
        return coreData;
    }

}
