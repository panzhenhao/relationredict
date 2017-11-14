/** 
 * Project Name:relationpredict 
 * File Name:LoadDataSet.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年11月1日上午11:30:52 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.util;

import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;
import com.uestcnslab.relationpredict.model.DataSetModel;

/**
 * @author zhenhao.pzh
 * @version $Id: LoadDataSet.java, v 0.1 2017年11月1日 上午11:33:54 zhenhao.pzh Exp $
 */
public class LoadDataSet {
    /**
     * loadDataFormCsv:从Csv文件中加载数据集. <br/>
     * 
     * @author zhenhao.pzh
     * @param path 模型路径
     * 
     * @return  数据集
     * 
     * @since JDK 1.8
     */
    public List<DataSetModel> loadDataFormCsv(String path) {
        List<DataSetModel> result = new ArrayList<DataSetModel>();
        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(path);

            // 读表头,有为true
            if (csvReader.readHeaders()) {
                String[] headers = csvReader.getHeaders();
            }

            while (csvReader.readRecord()) {
                DataSetModel dataSetModel = new DataSetModel();
                dataSetModel.setRelation(csvReader.get("relation"));
                dataSetModel.setWordFirst(csvReader.get("wordFirst"));
                dataSetModel.setWordEnd(csvReader.get("wordEnd"));
                result.add(dataSetModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
