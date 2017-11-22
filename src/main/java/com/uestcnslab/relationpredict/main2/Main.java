/** 
 * Project Name:relationredict 
 * File Name:Main.java 
 * Package Name:com.uestcnslab.relationpredict.main 
 * Date:2017年11月14日下午5:58:09 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.main2;

import java.util.ArrayList;
import java.util.Collections;
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
     * main:　给定一个词组判断这个词组是什么关系. <br/>
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
        String filename = path + "data/train_all_cbow200_cluster_classify_50.csv";
        List<ClusterModel> clusterModels = CsvFileUtil.loadClusterModel(filename);
        
        //3 加载聚类中心
        filename = path + "data/train_core_cbow200_cluster_classify_50.csv";
        List<ClusterModel> coreClusterModels = CsvFileUtil.loadClusterModel(filename);
        
        //4统计聚类中心的关系
        Map<Integer, String> coreRelation = countClusterCore(clusterModels,coreClusterModels.size());
        
        //更新聚类中心关系
        updateCoreClusterModels(coreClusterModels,coreRelation);
        
        //3.加载测试集
        filename = path + "data/testset.csv";
        List<DataSetModel> testSet = CsvFileUtil.loadDataSet(filename);

        //4.计算关系向量模型
        List<WordVecPoint> relationVec = WordVecModelTransferUtil.getRelationVec(testSet,
            cbowModel);

        //５.计算结果
        Map<Integer, List<SimilarModel>> mapResult = calculateSimilar(coreClusterModels, relationVec);

        //6.统计结果
        Map<String, Map<String, Integer>> Result = countTpFpResult(mapResult);

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

    private static String getTestRelation(List<SimilarModel> list) {
        Collections.sort(list);
        Map<String, Double> map = new HashMap<String, Double>();
        String testRelation =list.get(0).getTestRelation();
        double maxScore =Double.MIN_VALUE;
        for (int i=0;i<list.size();i++) {
            SimilarModel similarModel = list.get(i);
            String key = similarModel.getTestRelation();
            double score = similarModel.getScore();
            if (map.get(key)!=null) {
                score +=map.get(key);
            }
            if (score>maxScore) {
                testRelation = key;
                maxScore = score;
            }
            map.put(key, score);
        }
        return testRelation;
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
            String testRelation = getTestRelation(mapResult.get(key));
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
     * updateCoreClusterModels:根据统计结果，更新聚类中心关系. <br/> 
     * 
     * @author pzh 
     * @param coreClusterModels 聚类中心模型
     * @param coreRelation  聚类中心关系
     *
     * @since JDK 1.8 
     */ 
    private static void updateCoreClusterModels(List<ClusterModel> coreClusterModels,
                                                Map<Integer, String> coreRelation) {
        for (ClusterModel clusterModel: coreClusterModels) {
            Integer key = clusterModel.getId();
            clusterModel.setRelation(coreRelation.get(key));
        }
    }

    /** 
     * countClusterCore:先聚类在分类情况下，统计聚类中心属于哪种关系. <br/> 
     * 
     * @author pzh 
     * @param clusterModels
     * @param n
     * @return 聚类中心－关系 map
     *
     * @since JDK 1.8 
     */ 
    private static Map<Integer, String> countClusterCore(List<ClusterModel> clusterModels, int n) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        //map key为聚类核　value 为map　其中key为关系value为出现次数
        Map<Integer,Map<String, Integer>> coreRelations = new HashMap<Integer, Map<String,Integer>>();
        for (ClusterModel clusterModel: clusterModels) {
            //聚类中心编号为flag-1
            int flag = clusterModel.getFlag();
            int core = flag-1;
            String relation = clusterModel.getRelation();
            countCoreRelation(core,relation,coreRelations);
        }
        for (Integer key : coreRelations.keySet()) {
            Map<String, Integer> map = coreRelations.get(key);
            String relation = null;
            Integer count = Integer.MIN_VALUE;
            for (String keyTemp : map.keySet()) {
                int num = map.get(keyTemp);
                if (num>count) {
                    count =num;
                    relation = keyTemp;
                }
            }
            result.put(key, relation);
        }
        return result;
    }

    /** 
     * countCoreRelation:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param core 所属聚类中心
     * @param relation 关系
     * @param coreRelations 
     *
     * @since JDK 1.8 
     */ 
    private static void countCoreRelation(int core, String relation, Map<Integer, Map<String, Integer>> coreRelations) {
        if (coreRelations.containsKey(core)) {
            Map<String, Integer> map = coreRelations.get(core);
            if (map.containsKey(relation)) {
                map.put(relation, map.get(relation)+1);
            }else {
                map.put(relation, 1);
            }
        }else {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put(relation, 1);
            coreRelations.put(core, map);
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
