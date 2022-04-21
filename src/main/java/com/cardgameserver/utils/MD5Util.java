package com.cardgameserver.utils;

import org.springframework.util.DigestUtils;

import java.util.Random;

public class MD5Util {

    public static String md5(String str){
        String s = DigestUtils.md5DigestAsHex(str.getBytes());
        return s;
    }

    /**
     * 将客户端传过来的密码（经过MD5加密） 进行加盐加密处理
     * 结果返回 加盐加密处理的string类型 并且带着盐（1salt 2salt md5 3salt 4salt）
     * 最后得到了36位的密码存入数据库和redis
     * @param inputPass
     * @return
     */

    public static String inputPassToFromPass(String inputPass){
        String newStr="";
        String s1 = Integer.toHexString(new Random().nextInt(16));
        String s2 = Integer.toHexString(new Random().nextInt(16));
        String s3 = Integer.toHexString(new Random().nextInt(16));
        String s4 = Integer.toHexString(new Random().nextInt(16));
        System.out.println(s1+"  "+s2+"  "+s3+"  "+s4);
        newStr+=s1;
        newStr+=s2;
        newStr+=inputPass;
        newStr+=s3;
        newStr+=s4;

        String ans=md5(newStr);
        System.out.println(ans);

        return s1+s2+ans+s3+s4;

    }


    public static void main(String[] args) {
        String s1=md5("123456");
        String ans=inputPassToFromPass(s1);
        System.out.println(ans);

    }



}
