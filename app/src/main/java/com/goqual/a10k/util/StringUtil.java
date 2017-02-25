package com.goqual.a10k.util;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class StringUtil {
    public static String charArrToString(char[] arr) {
        for(int i = 0; i<arr.length; i++) {
            if(arr[i] == 0) {
                arr[i] = '0';
            }
        }
        return new String(arr);
    }
}
