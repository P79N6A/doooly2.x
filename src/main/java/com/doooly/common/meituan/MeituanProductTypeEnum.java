package com.doooly.common.meituan;

/**
 * Created by wanghai on 2018/12/26.
 *
 *  dp_canyin（买单）
     mt_waimai(外卖)
     sqt_center（个人中心）
     sqt_info(个人绑定信息)
     sqt_tmc(tmc绑定)
     sqt_home(商企通首页-集成餐饮和外卖两个业务)
     dp_play(点评休闲娱乐)
     dp_life(点评生活娱乐)
     dp_beauty(丽人)
     mt_hotel(酒店)
     mt_ticket(门票)
     mt_maoyan(猫眼)
 */
public enum MeituanProductTypeEnum {

    CANYIN("dp_canyin","买单"),

    WAIMAI("mt_waimai","外卖"),

    CENTER("sqt_center","个人中心"),

    INFO("sqt_info","个人绑定信息"),

    TMC("sqt_tmc","tmc绑定"),

    HOME("sqt_home","商企通首页-集成餐饮和外卖两个业务"),

    PLAY("dp_play","点评休闲娱乐"),

    LIFE("dp_life","点评生活娱乐"),

    BEAUTY("dp_beauty","丽人"),

    HOTEL("mt_hotel","酒店"),

    TICKET("mt_ticket","门票"),

    MAOYAN("mt_maoyan","猫眼"),

    CAR("mt_car","打车");

    private String code;

    private String desc;

    MeituanProductTypeEnum(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static MeituanProductTypeEnum getMeituanProductTypeByCode(String code) {
        for (MeituanProductTypeEnum meituanProductTypeEnum : MeituanProductTypeEnum.values()) {
            if (meituanProductTypeEnum.getCode().equals(code)) {
                return meituanProductTypeEnum;
            }
        }
        return null;
    }
}
