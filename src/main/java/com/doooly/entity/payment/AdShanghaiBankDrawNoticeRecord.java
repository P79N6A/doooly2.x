package com.doooly.entity.payment;

import java.util.Date;

/**
 * 上海银行虚账户提款通知Entity
 * @author qing.zhang
 * @version 2018-05-28
 */
public class AdShanghaiBankDrawNoticeRecord {
    private Long id;//主键
    private String noticeId;		// 通知流水号
    private String virAccNo;		// 虚账户账号
    private String virAccName;		// 虚账户户名
    private String detailId;		// 原交易请求流水号
    private String otherAccNo;		// 对方账户账号
    private String otherAccName;		// 对方账户户名
    private String amt;		// 代缴金额
    private String tranDirection;		// 交易方向1:借;2:贷
    private String purpose;		// 用途
    private String memo;		// 平台摘要
    private String remark;		// 用途
    private String tranTime;		// 交易时间
    private Date createDate;//创建时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getVirAccNo() {
        return virAccNo;
    }

    public void setVirAccNo(String virAccNo) {
        this.virAccNo = virAccNo;
    }

    public String getVirAccName() {
        return virAccName;
    }

    public void setVirAccName(String virAccName) {
        this.virAccName = virAccName;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getOtherAccNo() {
        return otherAccNo;
    }

    public void setOtherAccNo(String otherAccNo) {
        this.otherAccNo = otherAccNo;
    }

    public String getOtherAccName() {
        return otherAccName;
    }

    public void setOtherAccName(String otherAccName) {
        this.otherAccName = otherAccName;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getTranDirection() {
        return tranDirection;
    }

    public void setTranDirection(String tranDirection) {
        this.tranDirection = tranDirection;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}