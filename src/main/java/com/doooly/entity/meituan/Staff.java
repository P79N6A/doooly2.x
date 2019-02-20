package com.doooly.entity.meituan;


import com.doooly.common.meituan.ContentParams;

import java.util.List;

/**
 * Created by Administrator on 2018/12/11.
 */
public class Staff extends ContentParams {

    //员工标示类型
    private int staffType;

    /**
     * 是否支持更新插入（默认0）
         0-否
         1-是
     */
    private int supportUpsert;

    //员工信息列表
    private List<StaffInfoVO> staffInfos;

    public int getStaffType() {
        return staffType;
    }

    public void setStaffType(int staffType) {
        this.staffType = staffType;
    }

    public List<StaffInfoVO> getStaffInfos() {
        return staffInfos;
    }

    public void setStaffInfos(List<StaffInfoVO> staffInfos) {
        this.staffInfos = staffInfos;
    }

    public int getSupportUpsert() {
        return supportUpsert;
    }

    public void setSupportUpsert(int supportUpsert) {
        this.supportUpsert = supportUpsert;
    }
}
