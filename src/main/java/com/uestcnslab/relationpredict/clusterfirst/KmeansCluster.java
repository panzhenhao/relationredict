/** 
 * Project Name:relationredict 
 * File Name:Kmeans.java 
 * Package Name:com.uestcnslab.relationpredict.main 
 * Date:2017年11月17日下午8:47:49 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.clusterfirst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.model.WordVecRelationModel;
import com.uestcnslab.relationpredict.model.WordVectorModel;
import com.uestcnslab.relationpredict.util.CsvFileUtil;
import com.uestcnslab.relationpredict.util.Kmeans2;
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
		// 1.加载数据集
		String path = CsvFileUtil.class.getClass().getResource("/").getPath();
		String filename = path + "data/trainset.csv";
		List<DataSetModel> DataSet = CsvFileUtil.loadDataSet(filename);
		logger.info("第一阶段：数据集加载完成！");

		// 2.加载模型
		// WordVectorModel model = LoadModel.loadModel(path + "trunk/cbowVectors.bin");
		WordVectorModel model = LoadModel.loadModel(path + "trunk/cbowVectors.bin");
		// WordVectorModel model = LoadModel.loadModel(path+"GloVe-1.2/vectors.bin");
		logger.info("第二阶段：模型加载完成！");

		// 3.计算关系向量模型
		List<WordVecRelationModel> relationVec = WordVecModelTransferUtil.getDataSetRelationVec(DataSet, model);
		logger.info("第三阶段：关系向量模型计算完成！");

		// ４.1聚类
		List<WordVecRelationModel[]> clusterResult = useKmeans(relationVec, 40);
		logger.info("第四阶段：聚类模型计算完成！");
		
		// ５.回写文件
		filename = System.getProperty("user.dir")+"/src/main/resources/cluster-first-data/train_core_cbow200_40.csv";
		writeDataClusterToCsv(filename, clusterResult.get(0));
		logger.info("第五阶段1：聚类中心文件csv完成！");
		filename = System.getProperty("user.dir")+"/src/main/resources/cluster-first-data/train_all_cbow200_40.csv";
		writeDataClusterToCsv(filename, clusterResult.get(1));
		logger.info("第五阶段2：聚类数据集文件csv完成！");
	}

	private static List<WordVecRelationModel[]> useKmeans(List<WordVecRelationModel> relationVec, int n) {
		Kmeans2 kmeans = new Kmeans2();

		List<WordVecRelationModel[]> result = new ArrayList<WordVecRelationModel[]>();

		WordVecRelationModel[] wvp = new WordVecRelationModel[relationVec.size()];
		for (int i = 0; i < wvp.length; i++) {
			wvp[i] = relationVec.get(i);
		}
		Kmeans2.setAllPoint(wvp);
		kmeans.initOldCore(n);
		kmeans.kmeansStart();
		WordVecRelationModel[] core = Kmeans2.getNewCore();

		result.add(core);
		result.add(wvp);
		return result;
	}

	/**
	 * writeDataClusterToCsv: 将聚类中心回写csv文件. <br/>
	 * 
	 * @author pzh
	 * @param filename
	 *            文件路径名称
	 * @param wordVecRelationModels
	 *            聚类中心数
	 *
	 * @since JDK 1.8
	 */
	private static void writeDataClusterToCsv(String filename, WordVecRelationModel[] wordVecRelationModels) {
		String[] headers = { "id", "relation", "word1", "word2", "flag", "vector" };
		List<String[]> list = new ArrayList<String[]>();
		for (int i = 0; i < wordVecRelationModels.length; i++) {
			String id = String.valueOf(i);
			String relation = wordVecRelationModels[i].getRelation();
			String word1 = wordVecRelationModels[i].getWordFirst();
			String word2 = wordVecRelationModels[i].getWordEnd();
			String vector = Arrays.toString(wordVecRelationModels[i].getVector());
			String flag = String.valueOf(wordVecRelationModels[i].getFlag());
			String[] content = { id, relation, word1, word2, flag, vector };
			list.add(content);
		}
		CsvFileUtil.csvWrite(filename, headers, list);
	}

}
