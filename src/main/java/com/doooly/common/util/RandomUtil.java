package com.doooly.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 随机字符串工具类
 * Created by liangjun on 2017/3/30.
 */
public class RandomUtil {
    /**全数字*/
    private static final String NUMBER_CHAR = "0123456789";
    /**小写字母*/
    private static final String LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    /**小写字母和数字*/
    private static final String LOWER_NUMBER_CHAR = "abcdefghijklmnopqrstuvwxyz0123456789";
    /**过滤数字字母类似的字母*/
    private static final String LOWER_NUMBER_SPECIAL_CHAR = "abcdefghjkmnpqrstuvwxyz23456789";
    /**过滤和数字类似的字母*/
    public static final String LOWER_SPECIAL_CHAR = "abcdefghjkmnpqrstuvwxyz";
    /**过滤和字母类似的数字*/
    private static final String NUMBER_SPECIAL_CHAR = "23456789";

    /**
     *
     */
    private static ThreadLocal<DateFormat> dateTimeFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };
    /**
     * 获取随机数字
     * @param length 数字的长度
     * @return
     */
    public static String getRandomNumber(int length) {
        return getRandomStr(length, NUMBER_CHAR);
    }

    /**
     *  获取随机字符
     * @param length
     * @return
     */
    public static String getRandomCharStr(int length) {
        return getRandomStr(length, LOWER_CHAR);
    }

    /**
     * 获取大写字母随机字符串
     * @param length
     * @return
     */
    public static String getRandomCharUpperStr(int length) {
        return getRandomStr(length, LOWER_CHAR).toUpperCase();
    }

    /**
     * 获取字母加数字随机字符串
     * @param length
     * @return
     */
    public static String getRandomStr(int length) {
        return getRandomStr(length, LOWER_NUMBER_CHAR);
    }

    /**
     * 获取大写字母加数字随机字符串
     * @param length
     * @return
     */
    public static String getRandomUpperStr(int length) {
        return getRandomStr(length, LOWER_NUMBER_CHAR).toUpperCase();
    }

    /**
     * 获取小写字母加数字随机字符串，过滤相似字符数字
     * @param length
     * @return
     */
    public static String getRandomSpecialStr(int length) {
        return getRandomStr(length, LOWER_NUMBER_SPECIAL_CHAR);
    }

    /**
     * 获取UUID
     * @param length
     * @return
     */
    public static String getUUID(int length){
        String str = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0, length);
        return str;
    }

    /**
     * 获取随机数
     * @param length
     * @param charStr
     * @return
     */
    private static String getRandomStr(int length, String charStr){
        StringBuffer buffer = null;
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        if(null == charStr){
            return null;
        }
        buffer = new StringBuffer(charStr);
        int range = buffer.length();
        for (int i = 0; i < length; ++i) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
    /**
     * 获取小写字母随机字符串，过滤和数字相似的字符
     * @param length
     * @return
     */
    public static String getRandomSpecialCharStr(int length) {
        return getRandomStr(length, LOWER_SPECIAL_CHAR);
    }
    /**
     * 获取数字随机字符串，过滤和字母相似的数字
     * @param length
     * @return
     */
    public static String getRandomSpecialNumber(int length) {
        return getRandomStr(length, NUMBER_SPECIAL_CHAR);
    }

    /**
     * 获取指定区间的随机数
     * @param minNumber
     * @param maxNumber
     * @return
     */
    public static int getRandomNumber(int minNumber, int maxNumber) {
        Random random = new Random();
        int number = random.nextInt(maxNumber - minNumber) + minNumber;
        return number;
    }

    /**
     * 生成唯一订单号28位
     * @return
     */
    public static String getTradeNo() {
        String dataTime = dateTimeFormat.get().format(new Date());
        StringBuffer sb = new StringBuffer(dataTime);
        //随机3位
        sb.append(RandomUtil.getRandomNumber(100, 1000));
        //随机4位
        sb.append(RandomUtil.getRandomNumber(1000, 10000));
        //随机4位
        sb.append(RandomUtil.getRandomNumber(1000, 10000));
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println("获取数字：" + RandomUtil.getRandomNumber(6));
        System.out.println("获取字符：" + RandomUtil.getRandomCharStr(32));
        System.out.println("获取大写字符：" + RandomUtil.getRandomCharUpperStr(32));
        System.out.println("获取字符数字：" + RandomUtil.getRandomStr(32));
        System.out.println("获取大写字符数字：" + RandomUtil.getRandomUpperStr(32));
        System.out.println("获取特殊字符数字：" + RandomUtil.getRandomSpecialStr(8));
        System.out.println("获取UUID：" + RandomUtil.getRandomNumber(9, 10));
        System.out.println("获取UUID：" + RandomUtil.getUUID(32));
        for (int i=0; i<20; i++){
            String x = RandomUtil.getTradeNo();
            System.out.println("获取UUID2：" + x +",,," + x.length());
        }

    }
}
