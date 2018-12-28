package com.doooly.common.meituan;

/**
 * Created by wanghai on 2018/12/14.
 * 员工标识类型
     10-商企通员工序列号
     20-员工邮箱
     30-员工企业工号
     40-商企通员工编号
     50-手机号
 */
public enum StaffTypeEnum {

    StaffTypeEnum10(10,"商企通员工序列号"),

    StaffTypeEnum20(20,"员工邮箱"),

    StaffTypeEnum30(30,"员工企业工号"),

    StaffTypeEnum40(40,"商企通员工编号"),

    StaffTypeEnum50(50,"手机号");

    protected int code;

    protected String name;

   // StaffTypeEnum30(30,"")

    StaffTypeEnum(int code, String name ) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static StaffTypeEnum getStaffTypeByCode(int code) {
        for (StaffTypeEnum staffTypeEnum : StaffTypeEnum.values()) {
            if (staffTypeEnum.getCode() == code) {
                return staffTypeEnum;
            }
        }
        return null;
    }
}
