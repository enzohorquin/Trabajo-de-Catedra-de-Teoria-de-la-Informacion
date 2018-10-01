package com.company;

/**
 * Created by Enzo on 3/4/2018.
 */
public class Taller {

    public static void main(String[] args)
    {

        Integer i = new Integer(1000);
        Integer j = new Integer(1001);
        Integer k = new Integer(1000);
        System.out.println(i < j);
        System.out.println(j > k);
        System.out.println(i == k);

        int a = -1;
        a=(int)(byte)a;
        System.out.println(a);

    }
}
