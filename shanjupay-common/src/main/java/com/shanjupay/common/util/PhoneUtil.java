package com.shanjupay.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {
    /**
     * 校验用户手机号是否合法
     * @param phone
     * @return
     */
    public static Boolean isMatches(String phone){
        String regex ="^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

   /* public static void main(String[] args) {
        System.out.println(isMatches("19806891072"));
    }*/
}
