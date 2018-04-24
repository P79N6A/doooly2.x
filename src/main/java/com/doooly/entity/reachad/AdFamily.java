package com.doooly.entity.reachad;

import java.util.Date;

/**
 * @Description:家庭表
 * @author: qing.zhang
 * @date: 2017-07-25
 */
public class AdFamily {
    private Long id;
    private Integer pointShareSwitch;//积分共享开关 0：开 1：关
    private Date pointShareDate;//积分共享开通时间
    private Date createDate;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPointShareSwitch() {
        return pointShareSwitch;
    }

    public void setPointShareSwitch(Integer pointShareSwitch) {
        this.pointShareSwitch = pointShareSwitch;
    }

    public Date getPointShareDate() {
        return pointShareDate;
    }

    public void setPointShareDate(Date pointShareDate) {
        this.pointShareDate = pointShareDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
