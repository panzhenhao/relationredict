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
    /**
      * @Fields idRelationMap : 关系映射
      */
    public static  Map<Integer, String> idRelationMap = new HashMap<Integer, String>();
    /**
      * @Fields relationIdMap : 关系映射
      */
    public static  Map<String, Integer> relationIdMap = new HashMap<String, Integer>();
    static {
        relationIdMap.put("verb_past",0);
        relationIdMap.put("mero",1);
        relationIdMap.put("verb_3rd_past",2);
        relationIdMap.put("collective_noun",3);
        relationIdMap.put("noun_singplur",4);
        relationIdMap.put("event",5);
        relationIdMap.put("prefix$re",6);
        relationIdMap.put("verb_3rd",7);
        relationIdMap.put("hyper",8);
    }
    static {
        idRelationMap.put(0,"verb_past");
        idRelationMap.put(1,"mero");
        idRelationMap.put(2,"verb_3rd_past");
        idRelationMap.put(3,"collective_noun");
        idRelationMap.put(4,"noun_singplur");
        idRelationMap.put(5,"event");
        idRelationMap.put(6,"prefix$re");
        idRelationMap.put(7,"verb_3rd");
        idRelationMap.put(8,"hyper");
    }
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
  