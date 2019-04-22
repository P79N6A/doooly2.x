package com.doooly.common.meituan;

/**
 * @Author: wanghai
 * @Date:2019/4/22 9:43
 * @Copyright:reach-life
 * @Description:业务类型（见下表）
 */
public enum MeituanBusinessTypeEnum {

    MAI_DAN("买单",1),

    YU_DING("预定",2),

    TUAN_GOU("团购",3),

    WAI_MAI("外卖",4),

    DA_CHE("打车",5),

    JIU_DIAN("酒店",10),

    MEN_PIAO("门票",11),

    JI_PIAO("机票",12),

    HUO_CHE_PIAO("火车票",13),

    DIAN_YIN_PIAO("电影票",14),

    SAO_MA_FU("扫码付",15),

    XIAN_XIA_MAI_DAN("线下买单",16),

    QI_TIAN("其他",-1);

    private String msg;

    private int code;

    MeituanBusinessTypeEnum(String msg,int code) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
