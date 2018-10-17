package com.doooly.common.util;

import com.github.binarywang.java.emoji.EmojiConverter;

/**
 * @Description: 表情转换工具类
 * @author: qing.zhang
 * @date: 2018-10-16
 */
public class EmojiUtils {

    private static EmojiConverter emojiConverter = EmojiConverter.getInstance();

    /**
     * 将表情编码转换成可读的表情字符
     * @param emojiStr
     * @return
     */
    public static String emojiConverterUnicodeStr(String emojiStr) {
        String result = emojiConverter.toUnicode(emojiStr);
        return result;
    }

    /**
     * 将带有表情的字符转换为表情编码（存入数据库）
     * @param str
     * @return
     */
    public static String emojiConverterToAlias(String str) {
        String result = emojiConverter.toAlias(str);
        return result;
    }

    public static void main(String[] args) {
        String s = emojiConverterToAlias("睿渠攻城狮\uD83E\uDD81");
        System.out.println(s);
        String s1 = emojiConverterUnicodeStr("积分变动通知");
        System.out.println(s1);
    }

}
