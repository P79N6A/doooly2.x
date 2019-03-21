package com.doooly.entity.meituan;


import com.doooly.common.meituan.MeituanConstants;

/**
 * Created by Administrator on 2018/12/11.
 */
public class StaffInfoVO {

    //商企通id
    private int entId = MeituanConstants.ENT_ID;

    //兜礼用户id
    private Integer userId;

    //手机号
    private String phone;

    //名字
    private String name;

    //邮箱
    private String email;

    //员工在企业的工号（唯一标示）
    private String entStaffNum;

    //部门
    private String department;

    //员工商企通序列号
    private String serialNum;


    //是否成功添加 10-已成功 20-未成功
    private Integer isSucc;

    //原因
    private String reason;

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getEntId() {
        return entId;
    }

    public void setEntId(int entId) {
        this.entId = entId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntStaffNum() {
        return entStaffNum;
    }

    public void setEntStaffNum(String entStaffNum) {
        this.entStaffNum = entStaffNum;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setIsSucc(Integer isSucc) {
        this.isSucc = isSucc;
    }

    public Integer getIsSucc() {
        return isSucc;
    }
}
