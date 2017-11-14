package com.uestcnslab.relationpredict.stanfordnlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author zhenhao.pzh
 * @version $Id: WordTagger.java, v 0.1 2017年10月31日 下午4:19:41 zhenhao.pzh Exp $
 */
public class WordTagger {
    /**
     * @Fields logger : 日志
     */
    Logger               logger      = Logger.getLogger("lavasoft");
    /**
     * @Fields TAGGER_PATH : 词性标注模型
     */
    public static String TAGGER_PATH = "./stanford-postagger-2015-12-09/models/english-left3words-distsim.tagger";

    /**
     * filterNounWord:过滤非名词性单词. <br/>
     * 
     * @author zhenhao.pzh
     * @param wordList
     *            词集合
     * @return 名词结果
     * @throws Exception
     *
     * @since JDK 1.8
     */
    public List<String> filterNounWord(List<String> wordList) throws Exception {
        List<String> reList = new ArrayList<String>();
        // Initialize the tagger
        MaxentTagger tagger = new MaxentTagger(TAGGER_PATH);
        // The sample string
        for (String word : wordList) {
            String sample = word;
            // The tagged string
            String tagged = tagger.tagString(sample);
            String[] temp = tagged.split("_");
            if (temp.length == 2) {
                if (temp[1].contains("NN")) {
                    reList.add(temp[0]);
                } else {
                    logger.info("过滤掉单词：" + temp[0] + "_" + temp[1]);
                }
            } else {
                logger.info("过滤掉干扰词：" + temp[0]);
            }
        }

        // Output the result
        // System.out.println(tagged);
        return reList;
    }

    /**
     * main:测试代码. <br/>
     * 
     * @author zhenhao.pzh
     * @param args
     *
     * @since JDK 1.8
     */
    public static void main(String[] args) {
        List<String> testList = new ArrayList<>();
        WordTagger wt = new WordTagger();
        testList.add("saltwater");
        testList.add("tails");
        testList.add("nose");
        testList.add("add");
        try {
            testList = wt.filterNounWord(testList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(testList);
    }
}