package com.mro.RAR.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            digest = md5.digest();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.logException("MD5 获取算法失败", e);
        } catch (UnsupportedEncodingException e) {
            LogUtils.logException("MD5 数据转换byte[] 失败", e);
        }
        String result = "";
        int i;
        StringBuffer buf = new StringBuffer("");
        for(int offset=0; offset<digest.length; offset++){
            i = digest[offset];
            if(i<0){
                i+=256;
            }
            if(i<16){
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }

        result = buf.toString();
        return result;
    }

    public static String md5(String path, String token, String date) {

        String result = "";
        token = "Bearer " + token;

        //Content-Type=application/json 请求头文件对应响应值
        result = getMD5Str(path + "&Content-Type=application/json&urlTime=" + date);
        result = getMD5Str(result + '.' + token);

        return result;
    }
}
