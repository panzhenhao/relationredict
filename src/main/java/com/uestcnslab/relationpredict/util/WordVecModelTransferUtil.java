/** 
 * Project Name:relationredict 
 * File Name:WordVecModelTransferUtil.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年11月19日下午7:30:20 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/  
  
package com.uestcnslab.relationpredict.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.uestcnslab.relationpredict.model.DataSetModel;
import com.uestcnslab.relationpredict.model.WordVecPoint;
import com.uestcnslab.relationpredict.model.WordVectorModel;

/** @author pzh 
 *  @version $Id: WordVecModelTransferUtil.java, v 0.1 2017年11月19日 下午7:30:20 pzh Exp $ */

public class WordVecModelTransferUtil {
    /**
     * @Fields logger : 日志模型
     */
    private static Logger logger = Logger.getLogger("WordVecModelTransferUtil");
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
    public static List<WordVecPoint> getRelationVec(List<DataSetModel> DataSet,
                                                     WordVectorModel model) {
        List<WordVecPoint> relationVec = new ArrayList<WordVecPoint>();
        for (DataSetModel dataSetModel : DataSet) {
            String relation = dataSetModel.getRelation();
            String word1 = dataSetModel.getWordFirst();
            String word2 = dataSetModel.getWordEnd();
            float[] wv1 = model.getWordMap().get(word1);
            if (wv1 == null) {
                logger.info("不存在词向量:" + word1);
                continue;
            }
            float[] wv2 = model.getWordMap().get(word2);
            if (wv2 == null) {
                logger.info("不存在词向量:" + word2);
                continue;
            }
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
}
  