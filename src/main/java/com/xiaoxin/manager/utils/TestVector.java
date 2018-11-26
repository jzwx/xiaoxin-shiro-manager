package com.xiaoxin.manager.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * @Author:jzwx
 * @Desicription: TestVector
 * @Date:Created in 2018-11-23 17:08
 * @Modified By:
 */
public class TestVector {
    public void test01(){
        Vector<String> hs = new Vector<String>();
        hs.add("aa");
        hs.add("bb");
        hs.add("cc");
        hs.add("aa");
        hs.add("dd");
        printSet2(hs);
        List<String> hl = new ArrayList<String>();
    }

    public void printSet2(Vector<String> hs){
        Enumeration<String> elements = hs.elements();
        while (elements.hasMoreElements()){
            System.out.println(elements.nextElement());
        }
    }

    public static void main(String[] args){
        new TestVector().test01();
    }
}
