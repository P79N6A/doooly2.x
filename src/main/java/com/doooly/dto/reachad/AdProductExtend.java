package com.doooly.dto.reachad;

import com.doooly.entity.reachad.AdProduct;

public class AdProductExtend extends AdProduct {

    private String businessNum;  // 商户编号
    private String isStar;      //是否品牌馆,1：是品牌馆，0：不是品牌馆

    public String getBusinessNum() {
        return businessNum;
    }

    public void setBusinessNum(String businessNum) {
        this.businessNum = businessNum;
    }

    public String getIsStar() {
        return isStar;
    }

    public void setIsStar(String isStar) {
        this.isStar = isStar;
    }
}
