package com.xyb.tape.ui;

import java.math.BigDecimal;

/**
 * Created  on 2017/11/7.
 *
 * @author xyb
 */

public class MathUtil {
    public static  String towDecimal(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static  int compareTwoNum(float num1,float num2){
        String num1Str= towDecimal(num1);
        String num2Str= towDecimal(num2);
        return new BigDecimal(num1Str).compareTo(new BigDecimal(num2Str));
    }

    public static  String zeroDecimal(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 保留一位小数
     *
     * @param value
     * @return
     */
    public static  String oneDecimal(float value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    }

}
