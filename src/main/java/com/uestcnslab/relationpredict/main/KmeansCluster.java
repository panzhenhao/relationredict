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
import java.util.List;

import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.model.WordVecPoint;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.Kmeans;
import com.uestcnslab.relationpredict.util.LoadModel;

/**
 * @author pzh
 * @version $Id: Kmeans.java, v 0.1 2017年11月17日 下午8:47:49 pzh Exp $
 */

public class KmeansCluster {

    public static void main(String[] args) throws Exception {
        //1.加载数据集
        String path = CsvFileUtil.class.getClass().getResource("/").getPath();
        String filename = path + "data/trainset.csv";
        List<DataSetModel> DataSet = loadDataSet(filename);

        //2.加载模型
        WordVectorModel model = LoadModel.loadModel(path + "trunk/cbowVectors.bin");
        //WordVectorModel model = LoadModel.loadModel(path+"trunk/skipVectors.bin");
        //WordVectorModel model = LoadModel.loadModel(path+"GloVe-1.2/vectors.bin");

        //3.计算关系向量模型
        List<WordVecPoint> relationVec = getRelationVec(DataSet, model);

        //４.聚类
        WordVecPoint[] core = useKmeans(relationVec, 5);
        
        //５.回写文件
        filename = "/home/pzh/git/relationredict/src/main/resources/data/test.csv";
        writeDataClusterToCsv(filename, core);
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
        String[] headers = { "relation", "vector" };
        List<String[]> list = new ArrayList<String[]>();
        for (int i = 0; i < core.length; i++) {
            String relation = core[i].getWord();
            String vector = Arrays.toString(core[i].getVector());
            String[] content = { relation, vector };
            list.add(content);
        }
        CsvFileUtil.csvWrite(filename, headers, list);
    }

    /**
     * useKmeans: 使用k-means聚类算法. <br/>
     * 
     * @author pzh
     * @param relationVec
     *            聚类数据集
     * @param n
     *            聚类中心个数
     * @return 聚类中心
     *
     * @since JDK 1.8
     */
    private static WordVecPoint[] useKmeans(List<WordVecPoint> relationVec, int n) {
        Kmeans kmeans = new Kmeans();
        WordVecPoint[] wvp = new WordVecPoint[relationVec.size()];
        kmeans.setAllPoint(wvp);
        kmeans.initOldCore(n);
        kmeans.kmeansStart();
        WordVecPoint[] core = kmeans.getNewCore();
        return core;
    }

    /**
     * getRelationVec:获取关系向量集合. <br/>
     * 
     * @author pzh
     * @param DataSet
     *            训练集
     * @param model
     *            词向量集
     * @return 关系向量集合
     *
     * @since JDK 1.8
     */
    private static List<WordVecPoint> getRelationVec(List<DataSetModel> DataSet,
                                                     WordVectorModel model) {
        List<WordVecPoint> relationVec = new ArrayList<WordVecPoint>();
        for (DataSetModel dataSetModel : DataSet) {
            String relation = dataSetModel.getRelation();
            String word1 = dataSetModel.getWordFirst();
            String word2 = dataSetModel.getWordEnd();
            float[] wv1 = model.getWordMap().get(word1);
            float[] wv2 = model.getWordMap().get(word2);
            float[] wordVecRelation = new float[wv2.length];
            for (int i = 0; i < wordVecRelation.length; i++) {
                wordVecRelation[i] = wv1[i] - wv2[i];
            }
            WordVecPoint WordVecPoint = new WordVecPoint();
            WordVecPoint.setWord(relation);
            WordVecPoint.setVector(wordVecRelation);
            relationVec.add(WordVecPoint);
        }
        return relationVec;
    }

    /**
     * loadDataSet: 加载训练集合. <br/>
     * 
     * @author pzh
     * @param path
     * @return
     * @throws Exception
     *
     * @since JDK 1.8
     */
    private static List<DataSetModel> loadDataSet(String filename) throws Exception {
        CsvFileUtil csvFileUtil = new CsvFileUtil(filename);
        String line = csvFileUtil.readLine();
        System.out.println(line);
        List<DataSetModel> trainSetModelList = new ArrayList<DataSetModel>();
        while ((line = csvFileUtil.readLine()) != null) {
            DataSetModel dataSetModel = new DataSetModel();
            String[] str = line.split(",");
            dataSetModel.setRelation(str[0]);
            dataSetModel.setWordFirst(str[1]);
            dataSetModel.setWordEnd(str[2]);
            trainSetModelList.add(dataSetModel);
        }
        return trainSetModelList;
    }

}
