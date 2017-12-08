/** 
 * Project Name:relationredict 
 * File Name:Distance.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年11月16日下午2:03:16 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.util;

import com.uestcnslab.relationpredict.model.AttributeMode;
import com.uestcnslab.relationpredict.model.ClusterModel;
import com.uestcnslab.relationpredict.model.WordVecPoint;
import com.uestcnslab.relationpredict.model.WordVecRelationModel;

/**
 * @author pzh
 * @version $Id: Distance.java, v 0.1 2017年11月16日 下午2:03:16 pzh Exp $
 */

public class Distance {
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
    public static double pointDistance(WordVecPoint wordVecPoint1, WordVecPoint wordVecPoint2) {
        float[] f1 = wordVecPoint1.getVector();
        float[] f2 = wordVecPoint2.getVector();
        double sum = 0;
        for (int i = 0; i < f2.length; i++) {
            sum += Math.pow((f1[i] - f2[i]), 2);
        }
        return Math.sqrt(sum);
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
    public static double pointDistanceCos(WordVecPoint wordVecPoint1, WordVecPoint wordVecPoint2) {
        float[] f1 = wordVecPoint1.getVector();
        float[] f2 = wordVecPoint2.getVector();
        double numerator = 0;
        double f1Length = 0;
        double f2Length = 0;
        for (int i = 0; i < f2.length; i++) {
            numerator += f1[i] * f2[i];
            f1Length += f1[i] * f1[i];
            f2Length += f2[i] * f2[i];
        }
        double cos = numerator/(Math.sqrt(f1Length)*Math.sqrt(f2Length));
        return cos;
    }
    /**
     * pointDistance:点之间的距离. <br/>
     * 
     * @author pzh
     * @param wordVecPoint
     * @param clusterModel
     * @return
     *
     * @since JDK 1.8
     */
    public static double pointDistance(WordVecPoint wordVecPoint, ClusterModel clusterModel) {
        float[] f1 = wordVecPoint.getVector();
        float[] f2 = clusterModel.getVector();
        double numerator = 0;
        double f1Length = 0;
        double f2Length = 0;
        for (int i = 0; i < f2.length; i++) {
            numerator += f1[i] * f2[i];
            f1Length += f1[i] * f1[i];
            f2Length += f2[i] * f2[i];
        }
        double cos = numerator/(Math.sqrt(f1Length)*Math.sqrt(f2Length));
        return cos;
    }

    /** 
     * pointDistance:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param wordVecRelationModel
     * @param wordVecRelationModel2
     * @return 
     *
     * @since JDK 1.8 
     */ 
    public static double pointDistance(WordVecRelationModel wordVecRelationModel,
                                       WordVecRelationModel wordVecRelationModel2) {
        float[] f1 = wordVecRelationModel.getVector();
        float[] f2 = wordVecRelationModel2.getVector();
        double sum = 0;
        for (int i = 0; i < f2.length; i++) {
            sum += Math.pow((f1[i] - f2[i]), 2);
        }
        return Math.sqrt(sum);
    }

    /** 
     * pointDistance:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param attributeMode
     * @return 
     *
     * @since JDK 1.8 
     */ 
    public static double pointDistance(AttributeMode attributeMode) {
        float[] f1 = attributeMode.getCoreVector();
        float[] f2 = attributeMode.getRelationVector();
        double sum = 0;
        for (int i = 0; i < f2.length; i++) {
            sum += Math.pow((f1[i] - f2[i]), 2);
        }
        return Math.sqrt(sum);
    }
    
    /** 
     * pointDistance:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param f1
     * @param f2
     * @return 
     *
     * @since JDK 1.8 
     */ 
    public static double pointDistance(float[] f1,float[] f2) {
        double sum = 0;
        for (int i = 0; i < f2.length; i++) {
            sum += Math.pow((f1[i] - f2[i]), 2);
        }
        return Math.sqrt(sum);
    }

    /** 
     * pointDistanceCos:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @author pzh 
     * @param f1
     * @param f2
     * @return 
     *
     * @since JDK 1.8 
     */ 
    public static double pointDistanceCos(float[] f1, float[] f2) {
        double numerator = 0;
        double f1Length = 0;
        double f2Length = 0;
        for (int i = 0; i < f2.length; i++) {
            numerator += f1[i] * f2[i];
            f1Length += f1[i] * f1[i];
            f2Length += f2[i] * f2[i];
        }
        double cos = numerator/(Math.sqrt(f1Length)*Math.sqrt(f2Length));
        return cos;
    }
}
