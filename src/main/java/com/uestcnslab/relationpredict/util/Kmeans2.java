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
import com.uestcnslab.relationpredict.model.WordVecRelationModel;

/**
 * @author pzh
 * @version $Id: Kmeans.java, v 0.1 2017年11月14日 下午9:07:09 pzh Exp $
 */

public class Kmeans2 {
    /**
     * @Fields logger : 日志模型
     */
    Logger                                logger  = Logger.getLogger("Kmeans");

    /**
     * 待聚类的点集
     */
    private static WordVecRelationModel[] allPoint;
    /**
     * old聚类中心
     */
    private static WordVecRelationModel[] oldCore = null;
    /**
     * new聚类中心
     */
    private static WordVecRelationModel[] newCore = null;

    /**
     * @Fields iter :标志，聚类中心点的移动是否符合最小距离或迭代次数超过设置值
     */
    private int                           iter    = 0;
    /**
     * @Fields iterMax : 默认次数
     */
    private int                           iterMax = 1000;

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
        Kmeans2.oldCore = new WordVecRelationModel[core];// 存放聚类中心
        Kmeans2.newCore = new WordVecRelationModel[core];

        Random rand = new Random();
        int temp[] = new int[core];
        temp[0] = rand.nextInt(allPoint.length);
        oldCore[0] = new WordVecRelationModel();
        oldCore[0].setRelation(allPoint[temp[0]].getRelation());
        oldCore[0].setWordFirst(allPoint[temp[0]].getWordFirst());
        oldCore[0].setWordEnd(allPoint[temp[0]].getWordEnd());
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
                oldCore[i] = new WordVecRelationModel();
                oldCore[i].setRelation(allPoint[thistemp].getRelation());
                oldCore[i].setWordFirst(allPoint[thistemp].getWordFirst());
                oldCore[i].setWordEnd(allPoint[thistemp].getWordEnd());
                oldCore[i].setVector(allPoint[thistemp].getVector());
                //聚类中心标记
                oldCore[i].setFlag(0);
            }
        }
        logger.info("初始聚类中心：");
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
            WordVecRelationModel tempCore = new WordVecRelationModel();
            tempCore.setVector(new float[oldCore[i].getVector().length]);
            for (int j = 0; j < allPoint.length; j++) {

                if (allPoint[j].getFlag() == (i + 1)) {
                    logger.info(Arrays.toString(allPoint[j].getVector()));
                    numc += 1;
                    tempCore.setRelation(allPoint[j].getRelation());
                    tempCore.setWordFirst(allPoint[j].getWordFirst());
                    tempCore.setWordEnd(allPoint[j].getWordEnd());
                    for (int k = 0; k < allPoint[j].getVector().length; k++) {
                        tempCore.getVector()[k] += allPoint[j].getVector()[k];
                    }
                }
            }
            // 新的聚类中心
            newCore[i] = new WordVecRelationModel();
            newCore[i].setRelation(tempCore.getRelation());
            newCore[i].setWordFirst(tempCore.getWordFirst());
            newCore[i].setWordEnd(tempCore.getWordEnd());
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
    private void changeOldToNew(WordVecRelationModel[] oldCore2, WordVecRelationModel[] newCore2) {
        for (int i = 0; i < oldCore2.length; i++) {
            oldCore2[i].setRelation(newCore2[i].getRelation());
            oldCore2[i].setWordFirst((newCore2[i].getWordFirst()));
            oldCore2[i].setWordEnd(newCore2[i].getWordEnd());
            float[] fs = new float[oldCore2[i].getVector().length];
            for (int j = 0; j < newCore2[i].getVector().length; j++) {
                fs[j] = newCore2[i].getVector()[j];
            }
            oldCore2[i].setVector(fs);
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

        for (int i = 0; i < oldCore.length; i++) {
            movedistance = Distance.pointDistance(oldCore[i], newCore[i]);
            logger.debug("distcore:" + movedistance);//聚类中心的移动距离
            if (movedistance < 0.1) {
                iter = iterMax;
            } else {
                iter++;//需要继续迭代，
                break;
            }
        }
        if (iter >= iterMax) {
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
        Kmeans2 kmeans = new Kmeans2();

        WordVecRelationModel test1 = new WordVecRelationModel();
        test1.setRelation("test1");
        test1.setWordFirst("w1f");
        test1.setWordEnd("w1e");
        float[] f1 = new float[3];
        f1[0] = -2;
        f1[1] = -2;
        f1[2] = -2;
        test1.setVector(f1);

        WordVecRelationModel test2 = new WordVecRelationModel();
        test2.setRelation("test2");
        test2.setWordFirst("w2f");
        test2.setWordEnd("w2e");
        float[] f2 = new float[3];
        f2[0] = -1;
        f2[1] = -1;
        f2[2] = -1;
        test2.setVector(f2);

        WordVecRelationModel test3 = new WordVecRelationModel();
        test3.setRelation("test3");
        test3.setWordFirst("w3f");
        test3.setWordEnd("w3e");
        float[] f3 = new float[3];
        f3[0] = 3;
        f3[1] = 3;
        f3[2] = 3;
        test3.setVector(f3);

        WordVecRelationModel test4 = new WordVecRelationModel();
        test4.setRelation("test4");
        test4.setWordFirst("w4f");
        test4.setWordEnd("w4e");
        float[] f4 = new float[3];
        f4[0] = 2;
        f4[1] = 2;
        f4[2] = 2;
        test4.setVector(f4);

        WordVecRelationModel test5 = new WordVecRelationModel();
        test5.setRelation("test5");
        test5.setWordFirst("w5f");
        test5.setWordEnd("w5e");
        float[] f5 = new float[3];
        f5[0] = 5;
        f5[1] = 5;
        f5[2] = 5;
        test5.setVector(f5);

        WordVecRelationModel[] wvp = new WordVecRelationModel[5];
        wvp[0] = test1;
        wvp[1] = test2;
        wvp[2] = test3;
        wvp[3] = test4;
        wvp[4] = test5;

        Kmeans2.setAllPoint(wvp);
        kmeans.initOldCore(2);
        kmeans.kmeansStart();
        for (int i = 0; i < wvp.length; i++) {
            System.out.println(wvp[i]);
        }
    }

    /**
     * 获取 allPoint
     * 
     * @return allPoint
     */
    public static WordVecRelationModel[] getAllPoint() {
        return allPoint;
    }

    /**
     * 设置 allPoint
     * 
     * @param allPoint
     */
    public static void setAllPoint(WordVecRelationModel[] allPoint) {
        Kmeans2.allPoint = allPoint;
    }

    /**
     * 获取 oldCore
     * 
     * @return oldCore
     */
    public static WordVecRelationModel[] getOldCore() {
        return oldCore;
    }

    /**
     * 设置 oldCore
     * 
     * @param oldCore
     */
    public static void setOldCore(WordVecRelationModel[] oldCore) {
        Kmeans2.oldCore = oldCore;
    }

    /**
     * 获取 newCore
     * 
     * @return newCore
     */
    public static WordVecRelationModel[] getNewCore() {
        return newCore;
    }

    /**
     * 设置 newCore
     * 
     * @param newCore
     */
    public static void setNewCore(WordVecRelationModel[] newCore) {
        Kmeans2.newCore = newCore;
    }

    /**
     * 获取 iterMax
     * 
     * @return iterMax
     */
    public int getIterMax() {
        return iterMax;
    }

    /**
     * 设置 iterMax
     * 
     * @param iterMax
     */
    public void setIterMax(int iterMax) {
        this.iterMax = iterMax;
    }
}
