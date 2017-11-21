/** 
 * Project Name:relationredict 
 * File Name:CountData.java 
 * Package Name:com.uestcnslab.relationpredict.test 
 * Date:2017年11月21日下午2:12:30 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.uestcnslab.relationpredict.model.ClusterModel;
import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.LoadModel;

/**
 * @author pzh
 * @version $Id: CountData.java, v 0.1 2017年11月21日 下午2:12:30 pzh Exp $
 */

public class CountData {

    public static void main(String[] args) throws Exception {
        String path = CountData.class.getClass().getResource("/").getPath();
        //加载训练集聚类模型
        
        String filename = path + "data/train_all_cbow200_classify_cluster_5.csv";
        List<ClusterModel> ClusterModels = CsvFileUtil.loadClusterModel(filename);
        Map<String, Integer> result = count(ClusterModels);
        print(result);
        
//        String filename = path + "data/testset.csv";
//        List<DataSetModel> DataSetModels = CsvFileUtil.loadDataSet(filename);
//        Map<String, Integer> result = countDataSetModels(DataSetModels);
//        
//        print(result);
    }

    private static Map<String, Integer> countDataSetModels(List<DataSetModel> DataSetModels) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (DataSetModel dataSetModel :DataSetModels) {
            String relation = dataSetModel.getRelation();
            if (result.get(relation)!=null) {
                result.put(relation, result.get(relation)+1);
            }else {
                result.put(relation, 1);
            }
        }
        return result;
    }

    private static void print(Map<String, Integer> result) {
        for (String key: result.keySet()) {
            System.out.println(key+"条数： "+result.get(key));
        }
    }

    private static Map<String, Integer> count(List<ClusterModel> clusterModels){
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (ClusterModel clusterModel :clusterModels) {
            String relation = clusterModel.getRelation();
            if (result.get(relation)!=null) {
                result.put(relation, result.get(relation)+1);
            }else {
                result.put(relation, 1);
            }
        }
        return result;
    }
    
}
