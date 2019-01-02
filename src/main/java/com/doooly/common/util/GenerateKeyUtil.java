package com.doooly.common.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 生成key的工具类
 *
 * @Author: Mr.Wu
 * @Date: 2018/12/30
 */
public class GenerateKeyUtil {

    /**
     * 生成keys
     * @param length：长度
     * @param num：数量
     * @return
     */
    public static String[] generate(int length, Integer num) {
        if (num != null && num > 0) {
            String[] keys = new String[num];
            for (int i = 0; i < num; i++) {
                keys[i] = generate(length);
            }
            return keys;
        }
        return null;
    }

    /**
     * 生成key
     * @param length key的长度
     * @return
     */
    public static String generate(int length) {
        String key= RandomStringUtils.random(length, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
        return key;
    }

//    public static void main(String[] args) {
//        System.out.println(generate(6, 10000));
//    }
}
