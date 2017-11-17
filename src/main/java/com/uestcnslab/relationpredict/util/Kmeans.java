/** 
 * Project Name:relationredict 
 * File Name:Kmeans.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年11月14日下午9:07:09 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.util;

import java.util.Arrays;
import java.util.Random;

import org.apache.log4j.Logger;

import com.uestcnslab.relationpredict.model.WordVecPoint;

/**
 * @author pzh
 * @version $Id: Kmeans.java, v 0.1 2017年11月14日 下午9:07:09 pzh Exp $
 */

public class Kmeans {
    /**
     * @Fields logger : 日志模型
     */
    Logger                        logger  = Logger.getLogger("Kmeans");

    /**
     * 待聚类的点集
     */
    private static WordVecPoint[] allPoint;
    /**
     * old聚类中心
     */
    private static WordVecPoint[] oldCore = null;
    /**
     * new聚类中心
     */
    private static WordVecPoint[] newCore = null;

    /**
     * initOldCore:初始化聚类中心. <br/>
     * 
     * @author pzh
     * @param core
     *            目标聚类中心数
     *
     * @since JDK 1.8
     */
    public void initOldCore(int core) {
        Kmeans.oldCore = new WordVecPoint[core];// 存放聚类中心
        Kmeans.newCore = new WordVecPoint[core];

        Random rand = new Random();
        int temp[] = new int[core];
        temp[0] = rand.nextInt(allPoint.length);
        oldCore[0] = new WordVecPoint();
        oldCore[0].setWord(allPoint[temp[0]].getWord());
        oldCore[0].setVector(allPoint[temp[0]].getVector());
        //聚类中心标记
        oldCore[0].setFlag(0);
        // 避免产生重复的中心
        for (int i = 1; i < core; i++) {
            int flage = 0;
            int thistemp = rand.nextInt(allPoint.length);
            for (int j = 0; j < i; j++) {
                if (temp[j] == thistemp) {
                    flage = 1;// 有重复
                    break;
                }
            }
            if (flage == 1) {
                i--;
            } else {
                oldCore[i] = new WordVecPoint();
                oldCore[i].setWord(allPoint[thistemp].getWord());
                oldCore[i].setVector(allPoint[thistemp].getVector());
                //聚类中心标记
                oldCore[i].setFlag(0);
            }
        }
        System.out.println("初始聚类中心：");
        for (int i = 0; i < oldCore.length; i++) {
            logger.info(Arrays.toString(oldCore[i].getVector()));
        }
    }

    /**
     * searchBelong:找出每个点属于哪个聚类中心. <br/>
     * 
     * @author pzh
     *
     * @since JDK 1.8
     */
    private void searchBelong() {
        for (int i = 0; i < allPoint.length; i++) {
            double dist = 999;
            int lable = -1;
            for (int j = 0; j < oldCore.length; j++) {
                double distance = Distance.pointDistance(allPoint[i], oldCore[j]);
                if (distance < dist) {
                    dist = distance;
                    lable = j;
                    // po[i].flage = j + 1;// 1,2,3......
                }
            }
            allPoint[i].setFlag(lable + 1);
        }
    }

    /**
     * renewCore: 更新聚类中心. <br/>
     * 
     * @author pzh
     *
     * @since JDK 1.8
     */
    private void renewCore() {
        for (int i = 0; i < oldCore.length; i++) {
            logger.info("以<" + Arrays.toString(oldCore[i].getVector()) + ">为中心的点：");
            int numc = 0;
            WordVecPoint tempCore = new WordVecPoint();
            tempCore.setVector(new float[oldCore[i].getVector().length]);
            for (int j = 0; j < allPoint.length; j++) {

                if (allPoint[j].getFlag() == (i + 1)) {
                    logger.info(Arrays.toString(allPoint[j].getVector()));
                    numc += 1;
                    tempCore.setWord(allPoint[j].getWord());
                    for (int k = 0; k < allPoint[j].getVector().length; k++) {
                        tempCore.getVector()[k] += allPoint[j].getVector()[k];
                    }
                }
            }
            // 新的聚类中心
            newCore[i] = new WordVecPoint();
            newCore[i].setWord(tempCore.getWord());
            for (int j = 0; j < tempCore.getVector().length; j++) {
                tempCore.getVector()[j] = tempCore.getVector()[j] / numc;
            }
            newCore[i].setVector(tempCore.getVector());
            newCore[i].setFlag(0);
            logger.info("新的聚类中心：" + Arrays.toString(newCore[i].getVector()));
        }
    }

    /**
     * changeOldToNew:将旧的聚类中心替换为新的聚类中心. <br/>
     * 
     * @author pzh
     * @param oldCore2
     *            旧的聚类中心
     * @param newCore2
     *            新的聚类中心
     *
     * @since JDK 1.8
     */
    private void changeOldToNew(WordVecPoint[] oldCore2, WordVecPoint[] newCore2) {
        for (int i = 0; i < oldCore2.length; i++) {
            oldCore2[i].setWord(newCore2[i].getWord());
            for (int j = 0; j < newCore2[i].getVector().length; j++) {
                float[] fs = oldCore2[i].getVector();
                fs[j] = newCore2[i].getVector()[j];
                oldCore2[i].setVector(fs);
            }
            oldCore2[i].setFlag(0);// 表示为聚类中心的标志。
        }
    }

    /**
     * movecore:(这里用一句话描述这个方法的作用). <br/>
     * 
     * @author pzh
     *
     * @since JDK 1.8
     */
    public void kmeansStart() {
        // this.productpoint();//初始化，样本集，聚类中心，
        this.searchBelong();
        this.renewCore();//
        double movedistance = 0;
        int biao = -1;//标志，聚类中心点的移动是否符合最小距离
        for (int i = 0; i < allPoint.length; i++) {
            movedistance = Distance.pointDistance(allPoint[i], allPoint[i]);
            logger.debug("distcore:" + movedistance);//聚类中心的移动距离
            if (movedistance < 0.01) {
                biao = 0;
            } else {
                biao = 1;//需要继续迭代，
                break;
            }
        }
        if (biao == 0) {
            logger.info("迭代完毕！");
        } else {
            changeOldToNew(oldCore, newCore);
            kmeansStart();
        }

    }

    /** 
     * main:test. <br/> 
     * 
     * @author pzh 
     * @param args 
     *
     * @since JDK 1.8 
     */ 
    public static void main(String[] args) {
        Kmeans kmeans = new Kmeans();

        WordVecPoint test1 = new WordVecPoint();
        test1.setWord("test1");
        float[] f1 = new float[3];
        f1[0] = -2;
        f1[1] = -2;
        f1[2] = -2;
        test1.setVector(f1);

        WordVecPoint test2 = new WordVecPoint();
        test2.setWord("test2");
        float[] f2 = new float[3];
        f2[0] = -1;
        f2[1] = -1;
        f2[2] = -1;
        test2.setVector(f2);

        WordVecPoint test3 = new WordVecPoint();
        test1.setWord("test3");
        float[] f3 = new float[3];
        f3[0] = 1;
        f3[1] = 1;
        f3[2] = 1;
        test3.setVector(f3);

        WordVecPoint test4 = new WordVecPoint();
        test4.setWord("test4");
        float[] f4 = new float[3];
        f4[0] = 2;
        f4[1] = 2;
        f4[2] = 2;
        test4.setVector(f4);
        WordVecPoint[] wvp = new WordVecPoint[4];
        wvp[0] = test1;
        wvp[1] = test2;
        wvp[2] = test3;
        wvp[3] = test4;

        kmeans.setAllPoint(wvp);
        kmeans.initOldCore(2);
        kmeans.kmeansStart();
    }

    /**
     * 获取 allPoint
     * 
     * @return allPoint
     */
    public WordVecPoint[] getAllPoint() {
        return allPoint;
    }

    /**
     * 设置 allPoint
     * 
     * @param allPoint
     */
    public void setAllPoint(WordVecPoint[] allPoint) {
        Kmeans.allPoint = allPoint;
    }
}
