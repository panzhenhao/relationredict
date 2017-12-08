/** 
 * Project Name:relationpredict 
 * File Name:FileUtil.java 
 * Package Name:com.uestcnslab.relationpredict.util 
 * Date:2017年12月8日下午3:39:21 
 * Copyright (c) 2017, panzhenhao@hotmail.com All Rights Reserved. 
 * 
*/

package com.uestcnslab.relationpredict.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author pzh
 * @version $Id: FileUtil.java, v 0.1 2017年12月8日 下午3:39:21 pzh Exp $
 */

public class FileUtil {
    public static void main(String[] args) {
        try {
            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader("c://test.txt");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                sb.append(str + "/n");

                System.out.println(str);
            }

            br.close();
            reader.close();

            // write string to file
            FileWriter writer = new FileWriter("c://test2.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());

            bw.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeLine() {
        
    }
}
