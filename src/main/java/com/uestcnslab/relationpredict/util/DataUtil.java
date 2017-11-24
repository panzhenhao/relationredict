/** 
 * Project Name:relationredict 
 * File Name:DataUtil.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年11月23日下午9:17:42 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/  
  
package com.uestcnslab.relationpredict.util;

import java.util.HashMap;
import java.util.Map;

/** @author pzh 
 *  @version $Id: DataUtil.java, v 0.1 2017年11月23日 下午9:17:42 pzh Exp $ */

public class DataUtil {
    /**
      * @Fields TOTAL_DATA 总训练条数
      */
    public static final Integer  TOTAL_DATA = 5997;
    /**
      * @Fields trainSetRelations 训练集关系及对应条数
      */
    public static  Map<String, Integer> trainSetRelations = new HashMap<String, Integer>();
    static {
        trainSetRelations.put("verb_past", 72);
        trainSetRelations.put("mero", 2046);
        trainSetRelations.put("verb_3rd_past", 78);
        trainSetRelations.put("collective_noun", 183);
        trainSetRelations.put("noun_singplur", 67);
        trainSetRelations.put("event", 2564);
        trainSetRelations.put("prefix$re", 88);
        trainSetRelations.put("verb_3rd", 78);
        trainSetRelations.put("hyper", 822);
    }
}
  