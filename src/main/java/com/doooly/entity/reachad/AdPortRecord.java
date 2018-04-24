package com.doooly.entity.reachad;

import java.util.Date;

/**
 * @Description: 兜礼接口用户点击记录表
 * @author: qing.zhang
 * @date: 2018-03-15
 */
public class AdPortRecord {
    private Long id; //主键id
    private Long userId;		// 用户id
    private String portName;		// 接口名称
    private String remark;		// 备注
    private Date creatDate;		// 创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }
}
