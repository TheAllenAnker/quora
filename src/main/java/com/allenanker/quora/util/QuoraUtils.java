package com.allenanker.quora.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class QuoraUtils {
    private static final Logger logger = LoggerFactory.getLogger(QuoraUtils.class);

    public static int ANONYMOUS_USER_ID = 1;

    public static String getJSONString(int code, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);

        return jsonObject.toJSONString();
    }

    public static String MD5(String key) {
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // acquire a MessageDigest object
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // update with specified bits
            mdInst.update(btInput);
            // get encrypted text
            byte[] md = mdInst.digest();
            // parse it into string
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte b : md) {
                str[k++] = hexDigits[b >>> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("Generate MD5 Failed", e);
            return null;
        }
    }
}
