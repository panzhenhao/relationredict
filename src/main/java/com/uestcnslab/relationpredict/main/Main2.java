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

public class Main2 {
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
        Map<String, Map<String, Integer>> Result = countTpFpResult(mapResult);
        
        System.out.println("关系　准确率 召回率 f1值" );
        for (String key : Result.keySet()) {
            Map<String, Integer> map = Result.get(key);
            int tp = map.get(TP) == null ? 0 : map.get(TP);
            int fp = map.get(FP) == null ? 0 : map.get(FP);
            int fn = map.get(FN) == null ? 0 : map.get(FN);
            int tn = tp + fp;
            float acc = (float) tp / tn;
            float recall = (float) tp / (tp + fn);
            float f1_Measure = (2 * acc * recall) / (acc + recall);
            System.out.println(key + "," + acc +","+ recall+","+ f1_Measure);
        }
    }

    /**
     * countTpFpResult:统计结果. <br/>
     * 
     * @author pzh
     * @param mapResult
     * @return 统计的结果
     *
     * @since JDK 1.8
     */
    private static Map<String, Map<String, Integer>> countTpFpResult(Map<Integer, List<SimilarModel>> mapResult) {
        Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
        for (Integer key : mapResult.keySet()) {
            SimilarModel similarModel = mapResult.get(key).get(0);
            String realRelation = similarModel.getRealRelation();
            String testRelation = similarModel.getTestRelation();
            if (result.get(realRelation) != null) {
                Map<String, Integer> map = result.get(realRelation);
                if (realRelation.equals(testRelation)) {
                    //真实关系==测试关系　检索出来
                    if (map.get(TP) == null) {
                        map.put(TP, 1);
                    } else {
                        map.put(TP, map.get(TP) + 1);
                    }
                } else {
                    //真实关系\=测试关系
                    if (map.get(FN) == null) {
                        map.put(FN, 1);
                    } else {
                        map.put(FN, map.get(FN) + 1);
                    }
                    addTestRealtionFp(result, testRelation);
                }
                result.put(realRelation, map);
            } else {
                Map<String, Integer> map = new HashMap<String, Integer>();
                if (realRelation.equals(testRelation)) {
                    //真实关系==测试关系
                    map.put(TP, 1);
                } else {
                    //真实关系\=测试关系，检索出来的是测试关系，所以是FN
                    //相对于测试关系，那么测试关系的FP增加
                    map.put(FN, 1);
                    //累计测试关系的FP
                    addTestRealtionFp(result, testRelation);
                }
                result.put(realRelation, map);
            }
        }
        return result;
    }

    /**
     * addTestRealtionFp: 累计测试关系的FP值. <br/>
     * 
     * @author pzh
     * @param result
     *            结果集
     * @param testRelation
     *            测试关系
     *
     * @since JDK 1.8
     */
    private static void addTestRealtionFp(Map<String, Map<String, Integer>> result,
                                          String testRelation) {

        if (result.get(testRelation) != null) {
            Map<String, Integer> map = result.get(testRelation);
            if (map.get(FP) == null) {
                map.put(FP, 1);
            } else {
                map.put(FP, map.get(FP) + 1);
            }
            result.put(testRelation, map);
        } else {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put(FP, 1);
            result.put(testRelation, map);
        }
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
     * @param mapResult
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
