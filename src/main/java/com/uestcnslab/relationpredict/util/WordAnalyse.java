/** 
 * Project Name:relationpredict 
 * File Name:WordAnalyse.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年10月31日上午10:49:37 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/
/** 
 * Project Name: relationpredict 
 * File Name: WordAnalyse.java 
 * Package Name: com.uestcnslab.relationpredict.util 
 * Date: 2017年10月31日 上午10:49:37 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
 */

package com.uestcnslab.relationpredict.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.uestcnslab.relationpredict.model.WordVectorModel;

/**
 * @author zhenhao.pzh
 * @version $Id: WordAnalyse.java, v 0.1 2017年10月31日 上午10:49:37 zhenhao.pzh Exp
 *          $
 */
public class WordAnalyse {

    /**
     * @Fields TOP_N_SIZE : top n 的结果
     */
    private static int TOP_N_SIZE = 20;

    /**
     * 得到近义词
     * 
     * @param word
     *            目标词
     * @param wordMap
     *            模型
     * @return 结果集
     */
    public Set<WordEntry> distance(String word, HashMap<String, float[]> wordMap) {
        float[] wordVector = getWordVector(word, wordMap);
        if (wordVector == null) {
            return null;
        }
        Set<Entry<String, float[]>> entrySet = wordMap.entrySet();
        float[] tempVector = null;
        List<WordEntry> wordEntrys = new ArrayList<WordEntry>(TOP_N_SIZE);
        String name = null;
        for (Entry<String, float[]> entry : entrySet) {
            name = entry.getKey();
            if (name.equals(word)) {
                continue;
            }
            float dist = 0;
            tempVector = entry.getValue();
            for (int i = 0; i < wordVector.length; i++) {
                dist += wordVector[i] * tempVector[i];
            }
            insertTopN(name, dist, wordEntrys);
        }
        return new TreeSet<WordEntry>(wordEntrys);
    }

    /**
     * 获取近义词 word2-（word0-word1）
     * 
     * @param word0 第一个输入词
     * @param word1 第二个输入词
     * @param word2 第三个输入词
     * @param wordVectorModel 模型类
     * 
     * @return 结果集
     */
    public TreeSet<WordEntry> analogy(String word0, String word1, String word2,
                                      WordVectorModel wordVectorModel) {
        //模型
        HashMap<String, float[]> wordMap = wordVectorModel.getWordMap();
        //向量维度
        int size = wordVectorModel.getSize();
        
        float[] wv0 = getWordVector(word0, wordMap);
        float[] wv1 = getWordVector(word1, wordMap);
        float[] wv2 = getWordVector(word2, wordMap);

        if (wv1 == null || wv2 == null || wv0 == null) {
            return null;
        }
        float[] wordVector = new float[size];
        for (int i = 0; i < size; i++) {
            wordVector[i] = wv1[i] - wv0[i] + wv2[i];
        }
        float[] tempVector;
        String name;
        List<WordEntry> wordEntrys = new ArrayList<WordEntry>(TOP_N_SIZE);
        for (Entry<String, float[]> entry : wordMap.entrySet()) {
            name = entry.getKey();
            if (name.equals(word0) || name.equals(word1) || name.equals(word2)) {
                continue;
            }
            float dist = 0;
            tempVector = entry.getValue();
            for (int i = 0; i < wordVector.length; i++) {
                dist += wordVector[i] * tempVector[i];
            }
            insertTopN(name, dist, wordEntrys);
        }
        return new TreeSet<WordEntry>(wordEntrys);
    }

    /**
     * insertTopN: 将结果词及相似度插入到topN的结果集中. <br/>
     * 
     * @author zhenhao.pzh
     * @param name
     *            结果词
     * @param score
     *            相思度
     * @param wordsEntrys
     *            topN的结果集中
     *
     * @since JDK 1.8
     */
    public void insertTopN(String name, float score, List<WordEntry> wordsEntrys) {
        if (wordsEntrys.size() < TOP_N_SIZE) {
            wordsEntrys.add(new WordEntry(name, score));
            return;
        }
        float min = Float.MAX_VALUE;
        int minOffe = 0;
        for (int i = 0; i < TOP_N_SIZE; i++) {
            WordEntry wordEntry = wordsEntrys.get(i);
            if (min > wordEntry.score) {
                min = wordEntry.score;
                minOffe = i;
            }
        }

        if (score > min) {
            wordsEntrys.set(minOffe, new WordEntry(name, score));
        }

    }

    /**
     * @author zhenhao.pzh
     * @version $Id: WordAnalyse.java, v 0.1 2017年10月31日 上午11:04:25 zhenhao.pzh
     *          Exp $
     */
    public class WordEntry implements Comparable<WordEntry> {
        /**
         * @Fields name : 结果词
         */
        public String name;
        /**
         * @Fields score : 相似度
         */
        public float  score;

        /**
         * Creates a new instance of WordEntry.
         * 
         * @param name
         *            结果词
         * @param score
         *            相似度
         */
        public WordEntry(String name, float score) {
            this.name = name;
            this.score = score;
        }

        /** * @see java.lang.Object#toString() */
        @Override
        public String toString() {
            return this.name + "\t" + score;
        }

        /** * @see java.lang.Comparable#compareTo(java.lang.Object) */
        @Override
        public int compareTo(WordEntry o) {
            if (this.score > o.score) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    /**
     * 得到词向量
     * 
     * @param word
     *            目标词
     * @param wordMap
     *            模型
     * @return 词向量
     */
    public float[] getWordVector(String word, HashMap<String, float[]> wordMap) {
        return wordMap.get(word);
    }

}
