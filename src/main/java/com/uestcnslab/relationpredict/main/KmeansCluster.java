/** 
 * Project Name:relationredict 
 * File Name:Kmeans.java 
 * Package Name:com.uestcnslab.relationpredict.main 
 * Date:2017年11月17日下午8:47:49 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.model.WordVecPoint;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.Kmeans;
import com.uestcnslab.relationpredict.util.LoadModel;
import com.uestcnslab.relationpredict.util.WordVecModelTransferUtil;

/**
 * @author pzh
 * @version $Id: Kmeans.java, v 0.1 2017年11月17日 下午8:47:49 pzh Exp $
 */

public class KmeansCluster {
    /**
     * @Fields logger : 日志模型
     */
    private static Logger logger = Logger.getLogger("KmeansCluster");

    public static void main(String[] args) throws Exception {
        //1.加载数据集
        String path = CsvFileUtil.class.getClass().getResource("/").getPath();
        String filename = path + "data/trainset.csv";
        List<DataSetModel> DataSet = CsvFileUtil.loadDataSet(filename);

        //2.加载模型
        //WordVectorModel model = LoadModel.loadModel(path + "trunk/cbowVectors.bin");
        WordVectorModel model = LoadModel.loadModel(path+"trunk/skipVectors.bin");
        //WordVectorModel model = LoadModel.loadModel(path+"GloVe-1.2/vectors.bin");

        //3.计算关系向量模型
        List<WordVecPoint> relationVec = WordVecModelTransferUtil.getRelationVec(DataSet, model);

        //４.聚类-先聚类在分类
        List<WordVecPoint[]> clusterAndClassifyResult = useKmeansClusterAndClassify(relationVec,
            50);

        //3.１关系向量模型list转map
        Map<String, List<WordVecPoint>> relationVecMap = getRelationVecMap(relationVec);
        //４.1聚类-先分类在聚类
        List<WordVecPoint[]> classifyAndClusterResult = useKmeansClassifyAndCluster(relationVecMap,
            5);

        //５.回写文件
        filename = "/home/pzh/git/relationredict/src/main/resources/data/train_core_skip200_classify_cluster_5.csv";
        writeDataClusterToCsv(filename, classifyAndClusterResult.get(0));
        filename = "/home/pzh/git/relationredict/src/main/resources/data/train_all_skip200_classify_cluster_5.csv";
        writeDataClusterToCsv(filename, classifyAndClusterResult.get(1));
    }

    /**
     * writeDataClusterToCsv: 将聚类中心回写csv文件. <br/>
     * 
     * @author pzh
     * @param filename
     *            文件路径名称
     * @param core
     *            聚类中心数
     *
     * @since JDK 1.8
     */
    private static void writeDataClusterToCsv(String filename, WordVecPoint[] core) {
        String[] headers = { "id", "relation", "flag", "vector" };
        List<String[]> list = new ArrayList<String[]>();
        for (int i = 0; i < core.length; i++) {
            String id = String.valueOf(i);
            String relation = core[i].getWord();
            String vector = Arrays.toString(core[i].getVector());
            String flag = String.valueOf(core[i].getFlag());
            String[] content = { id, relation, flag, vector };
            list.add(content);
        }
        CsvFileUtil.csvWrite(filename, headers, list);
    }

    /**
     * useKmeansClusterAndClassify: 使用k-means聚类算法. <br/>
     * 
     * @author pzh
     * @param relationVec
     *            聚类数据集
     * @param n
     *            聚类中心个数
     * @return 聚类中心＋数据集标记所属的聚类中心 list 0 聚类中心，１标记的数据集
     *
     * @since JDK 1.8
     */
    private static List<WordVecPoint[]> useKmeansClusterAndClassify(List<WordVecPoint> relationVec,
                                                                    int n) {
        Kmeans kmeans = new Kmeans();
        List<WordVecPoint[]> result = new ArrayList<WordVecPoint[]>();
        WordVecPoint[] wvp = new WordVecPoint[relationVec.size()];
        for (int i = 0; i < wvp.length; i++) {
            wvp[i] = relationVec.get(i);
        }
        kmeans.setAllPoint(wvp);
        kmeans.initOldCore(n);
        kmeans.kmeansStart();
        WordVecPoint[] core = kmeans.getNewCore();
        result.add(core);
        result.add(wvp);
        return result;
    }

    /**
     * useKmeansClassifyAndCluster:使用k-means聚类算法.. <br/>
     * 
     * @author pzh
     * @param relationVecMap
     *            聚类数据集
     * @param n
     *            聚类中心个数
     * @return 聚类中心＋数据集标记所属的聚类中心 list 0 聚类中心，１标记的数据集
     *
     * @since JDK 1.8
     */
    private static List<WordVecPoint[]> useKmeansClassifyAndCluster(Map<String, List<WordVecPoint>> relationVecMap,
                                                                    int n) {
        Kmeans kmeans = new Kmeans();
        List<WordVecPoint[]> coreList = new ArrayList<WordVecPoint[]>();
        List<WordVecPoint[]> allList = new ArrayList<WordVecPoint[]>();
        List<WordVecPoint[]> result = new ArrayList<WordVecPoint[]>();
        //总向量数
        int count = 0;
        for (String key : relationVecMap.keySet()) {
            List<WordVecPoint> relationVec = relationVecMap.get(key);
            count += relationVec.size();
            WordVecPoint[] wvp = new WordVecPoint[relationVec.size()];
            for (int i = 0; i < wvp.length; i++) {
                wvp[i] = relationVec.get(i);
            }
            kmeans.setAllPoint(wvp);
            kmeans.initOldCore(n);
            kmeans.kmeansStart();
            WordVecPoint[] core = kmeans.getNewCore();
            coreList.add(core);
            allList.add(wvp);
        }
        WordVecPoint[] coreResult = new WordVecPoint[coreList.size() * n];
        int index = 0;
        for (WordVecPoint[] wordVecPoint : coreList) {
            for (int j = 0; j < wordVecPoint.length; j++) {
                coreResult[index + j] = wordVecPoint[j];
            }
            index = index + wordVecPoint.length;
        }
        WordVecPoint[] allResult = new WordVecPoint[count];
        index = 0;
        for (WordVecPoint[] wordVecPoint : allList) {
            for (int j = 0; j < wordVecPoint.length; j++) {
                allResult[index + j] = wordVecPoint[j];
            }
            index = index + wordVecPoint.length;
        }
        result.add(coreResult);
        result.add(allResult);
        return result;
    }

   

    /**
     * getRelationVecMap: 关系向量集合key为关系，value为同关系的向量集合. <br/>
     * 
     * @author pzh
     * @param relationVec
     *            关系向量集合
     * @return 关系向量Map
     *
     * @since JDK 1.8
     */
    private static Map<String, List<WordVecPoint>> getRelationVecMap(List<WordVecPoint> relationVec) {
        Map<String, List<WordVecPoint>> relationVecMap = new HashMap<String, List<WordVecPoint>>();
        for (WordVecPoint wordVecPoint : relationVec) {
            String relation = wordVecPoint.getWord();
            if (relationVecMap.get(relation) == null) {
                List<WordVecPoint> wordVecPoints = new ArrayList<WordVecPoint>();
                wordVecPoints.add(wordVecPoint);
                relationVecMap.put(relation, wordVecPoints);
            } else {
                relationVecMap.get(relation).add(wordVecPoint);
            }
        }
        return relationVecMap;
    }

    

}
