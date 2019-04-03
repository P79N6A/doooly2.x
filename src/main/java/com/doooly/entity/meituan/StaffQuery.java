package com.doooly.entity.meituan;


import com.doooly.common.meituan.ContentParams;

import java.util.List;

/**
 * Created by Administrator on 2018/12/14.
 */
public class StaffQuery extends ContentParams {

    //员工类型
    private int staffType;

    private String vendorName;

    private List<String> staffContents;

    public int getStaffType() {
        return staffType;
    }

    public void setStaffType(int staffType) {
        this.staffType = staffType;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public List<String> getStaffContents() {
        return staffContents;
    }

    public void setStaffContents(List<String> staffContents) {
        this.staffContents = staffContents;
    }
}
