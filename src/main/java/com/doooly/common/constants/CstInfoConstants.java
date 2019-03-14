package com.doooly.common.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 常量值
 *
 * @Author: Mr.Wu
 * @Date: 2019/3/13
 */
public class CstInfoConstants {
    // ================= 常量类型 对应常量code
    // 模版类型
    public static final int TEMP_TYPE_ONE = 1;          // 首页模版
    // 首页模版楼层类型
    public static final int TEMP_HOME_TYPE_ONE = 1;     // 首页模版-导航栏
    public static final int TEMP_HOME_TYPE_TWO = 2;     // 首页模版-礼包
    public static final int TEMP_HOME_TYPE_THREE = 3;   // 首页模版-广告位-隐藏楼层
    public static final int TEMP_HOME_TYPE_FOUR = 4;    // 首页模版-热门商户-隐藏楼层
    public static final int TEMP_HOME_TYPE_FIVE = 5;    // 首页模版-卡券活动
    public static final int TEMP_HOME_TYPE_SIX = 6;     // 首页模版-活动专区

    // 模版楼层Item关联的数据类型
    public static final int TEMP_ITEM_RELATION_TYPE_ONE = 1;    // 关联自营商品

    // 首页模版隐藏管理的楼层集合
    public static final List<Integer> HOME_HIDE_FLOOR_TYPE_LIST = new ArrayList<>(Arrays
            .asList(TEMP_HOME_TYPE_TWO, TEMP_HOME_TYPE_THREE,TEMP_HOME_TYPE_FOUR));
}
