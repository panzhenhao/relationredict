/** 
 * Project Name:relationredict 
 * File Name:Main.java 
 * Package Name:com.uestcnslab.relationpredict.main 
 * Date:2017年11月14日下午5:58:09 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uestcnslab.relationpredict.model.ClusterModel;
import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.model.SimilarModel;
import com.uestcnslab.relationpredict.model.WordVecPoint;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.Distance;
import com.uestcnslab.relationpredict.util.LoadModel;
import com.uestcnslab.relationpredict.util.WordVecModelTransferUtil;

/**
 * @author pzh
 * @version $Id: Main.java, v 0.1 2017年11月14日 下午5:58:09 pzh Exp $
 */

public class Main {
    /**
     * @Fields SIZE 相似度保留前几个
     */
    private static Integer SIZE    = 1;

    private static String  CORRECT = "correct";
    private static String  WRONG   = "wrong";

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
        List<WordVecPoint> relationVec = WordVecModelTransferUtil.getRelationVec(testSet,
            cbowModel);

        //５.计算结果
        Map<Integer, List<SimilarModel>> mapResult = calculateSimilar(ClusterModels, relationVec);

        //6.统计结果
        Map<String, Map<String, Integer>> result = countResult(mapResult);

        for (String key : result.keySet()) {
            Map<String, Integer> map = result.get(key);
            int correctNum = map.get(CORRECT) == null ? 0 : map.get(CORRECT);
            int wrongNum = map.get(WRONG) == null ? 0 : map.get(WRONG);
            Integer total = correctNum + wrongNum;
            System.out.println(key + "召回率： " + (float) correctNum / total);
        }
    }

    /**
     * countResult:统计结果. <br/>
     * 
     * @author pzh
     * @param mapResult
     * @return
     *
     * @since JDK 1.8
     */
    private static Map<String, Map<String, Integer>> countResult(Map<Integer, List<SimilarModel>> mapResult) {
        Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
        for (Integer key : mapResult.keySet()) {
            SimilarModel similarModel = mapResult.get(key).get(0);
            String realRelation = similarModel.getRealRelation();
            String testRelation = similarModel.getTestRelation();
            if (result.get(realRelation) != null) {
                Map<String, Integer> map = result.get(realRelation);
                if (realRelation.equals(testRelation)) {
                    if (map.get(CORRECT) == null) {
                        map.put(CORRECT, 1);
                    } else {
                        map.put(CORRECT, map.get(CORRECT) + 1);
                    }

                } else {
                    if (map.get(WRONG) == null) {
                        map.put(WRONG, 1);
                    } else {
                        map.put(WRONG, map.get(WRONG) + 1);
                    }
                }
                result.put(realRelation, map);
            } else {
                Map<String, Integer> map = new HashMap<String, Integer>();
                if (realRelation.equals(testRelation)) {
                    map.put(CORRECT, 1);
                } else {
                    map.put(WRONG, 1);
                }
                result.put(realRelation, map);
            }
        }
        return result;
    }

    /**
     * calculateSimilar:相似计算. <br/>
     * 
     * @author pzh
     * @param ClusterModels
     *            聚类模型
     * @param relationVec
     *            测试关系模型
     * @return 相似度模型
     *
     * @since JDK 1.8
     */
    private static Map<Integer, List<SimilarModel>> calculateSimilar(List<ClusterModel> ClusterModels,
                                                                     List<WordVecPoint> relationVec) {
        Map<Integer, List<SimilarModel>> mapResult = new HashMap<Integer, List<SimilarModel>>();
        for (int i = 0; i < relationVec.size(); i++) {
            WordVecPoint wordVecPoint = relationVec.get(i);
            for (ClusterModel clusterModel : ClusterModels) {
                SimilarModel similarModel = new SimilarModel();
                similarModel.setRealRelation(wordVecPoint.getWord());
                similarModel.setTestRelation(clusterModel.getRelation());
                double cosSimilar = Distance.pointDistance(wordVecPoint, clusterModel);
                similarModel.setScore(cosSimilar);
                insertMapResult(mapResult, i, similarModel);
            }
        }
        return mapResult;
    }

    /**
     * insertMapResult:(这里用一句话描述这个方法的作用). <br/>
     * 
     * @author pzh
     * @param mapResul
     *            结果存储
     * @param key
     *            结果key
     * @param similarModel
     *            测试结果模型
     *
     * @since JDK 1.8
     */
    private static void insertMapResult(Map<Integer, List<SimilarModel>> mapResult, int key,
                                        SimilarModel similarModel) {
        if (mapResult.get(key) == null) {
            List<SimilarModel> similarModels = new ArrayList<SimilarModel>();
            similarModels.add(similarModel);
            mapResult.put(key, similarModels);
        } else {
            List<SimilarModel> similarModels = mapResult.get(key);
            if (similarModels.size() >= SIZE) {
                double min = Double.MAX_VALUE;
                int index = -1;
                for (int i = 0; i < similarModels.size(); i++) {
                    SimilarModel similarModelTemp = similarModels.get(i);
                    if (similarModelTemp.getScore() < min) {
                        index = i;
                        min = similarModelTemp.getScore();
                    }
                }
                if (similarModel.getScore() > min) {
                    similarModels.remove(index);
                    similarModels.add(similarModel);
                }
            } else {
                similarModels.add(similarModel);
                mapResult.put(key, similarModels);
            }
        }
    }
}
