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
import java.util.Scanner;

import com.uestcnslab.relationpredict.model.WordVecPoint;

/**
 * @author pzh
 * @version $Id: Kmeans.java, v 0.1 2017年11月14日 下午9:07:09 pzh Exp $
 */

public class Kmeans {

    private static WordVecPoint[] allPoint;      // 点集
    private static WordVecPoint[] oldCore = null;// old聚类中心
    private static WordVecPoint[] newCore = null;// new聚类中心

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
        this.oldCore = new WordVecPoint[core];// 存放聚类中心
        this.newCore = new WordVecPoint[core];

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
            System.out.println(Arrays.toString(oldCore[i].getVector()));
        }

    }

    /**
     * searchBelong:找出每个点属于哪个聚类中心. <br/>
     * 
     * @author pzh
     *
     * @since JDK 1.8
     */
    public void searchBelong() {

        for (int i = 0; i < allPoint.length; i++) {
            double dist = 999;
            int lable = -1;
            for (int j = 0; j < oldCore.length; j++) {

                double distance = pointDistance(allPoint[i], oldCore[j]);
                if (distance < dist) {
                    dist = distance;
                    lable = j;
                    // po[i].flage = j + 1;// 1,2,3......

                }
            }
            allPoint[i].setFlag(lable + 1);

        }

    }

    // 更新聚类中心
    public void renewCore() {

        for (int i = 0; i < allPoint.length; i++) {
            System.out.println("以<" + Arrays.toString(oldCore[i].getVector())+ ">为中心的点：");
            int numc = 0;
            WordVecPoint tempCore = new WordVecPoint();
            tempCore.setVector(new float[oldCore[i].getVector().length]);
            for (int j = 0; j < allPoint.length; j++) {

                if (allPoint[j].getFlag() == (i + 1)) {
                    System.out.println(Arrays.toString(allPoint[j].getVector()));
                    numc += 1;
                    tempCore.setWord(allPoint[j].getWord());
                    for(int k=0;k<allPoint[j].getVector().length;k++) {
                        tempCore.getVector()[k] +=allPoint[j].getVector()[k];
                    }
                }
            }
            // 新的聚类中心
            newCore[i] = new WordVecPoint();
            newCore[i].setWord(tempCore.getWord());
            newCore[i].setVector(vector); = newcore.y / numc;
            newCore[i].flage = 0;
            System.out.println("新的聚类中心：" + pacoren[i].x + "," + pacoren[i].y);

        }
    }

    /**
     * pointDistance: 点之间的距离. <br/>
     * 
     * @author pzh
     * @param wordVecPoint1
     * @param wordVecPoint2
     * @return 距离
     *
     * @since JDK 1.8
     */
    public double pointDistance(WordVecPoint wordVecPoint1, WordVecPoint wordVecPoint2) {
        float[] f1 = wordVecPoint1.getVector();
        float[] f2 = wordVecPoint2.getVector();
        double sum = 0;
        for (int i = 0; i < f2.length; i++) {
            sum += Math.pow((f1[i] - f2[i]), 2);
        }
        return Math.sqrt(sum);

    }

    public void change_oldtonew(point[] old, point[] news) {
        for (int i = 0; i < old.length; i++) {
            old[i].x = news[i].x;
            old[i].y = news[i].y;
            old[i].flage = 0;// 表示为聚类中心的标志。
        }
    }

    public void movecore() {
        // this.productpoint();//初始化，样本集，聚类中心，
        this.searchbelong();
        this.calaverage();//
        double movedistance = 0;
        int biao = -1;//标志，聚类中心点的移动是否符合最小距离
        for (int i = 0; i < pacore.length; i++) {
            movedistance = distpoint(pacore[i], pacoren[i]);
            System.out.println("distcore:" + movedistance);//聚类中心的移动距离
            if (movedistance < 0.01) {
                biao = 0;

            } else {

                biao = 1;//需要继续迭代，
                break;

            }
        }
        if (biao == 0) {
            System.out.print("迭代完毕！！！！！");
        } else {
            change_oldtonew(pacore, pacoren);
            movecore();
        }

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Kcluster kmean = new Kcluster();
        kmean.productpoint();
        kmean.movecore();
    }

    /**
     * 获取 allPoint
     * 
     * @return allPoint
     */
    public static WordVecPoint[] getAllPoint() {
        return allPoint;
    }

    /**
     * 设置 allPoint
     * 
     * @param allPoint
     */
    public static void setAllPoint(WordVecPoint[] allPoint) {
        Kmeans.allPoint = allPoint;
    }
}
