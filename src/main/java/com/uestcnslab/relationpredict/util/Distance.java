/** 
 * Project Name:relationredict 
 * File Name:Distance.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年11月16日下午2:03:16 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.util;

import com.uestcnslab.relationpredict.model.WordVecPoint;

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
}
