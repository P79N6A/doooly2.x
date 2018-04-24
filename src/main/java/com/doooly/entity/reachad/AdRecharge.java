package com.doooly.entity.reachad;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值明细Entity
 * @author XueLing
 * @version 2015-12-15
 */
public class AdRecharge {
	
	private static final long serialVersionUID = 1L;
    private String id;
	private AdUser adUser;		// 会员编号
	private AdGroup adGroup;		// 企业编号
	private String type;       //1.给所有员工充值 2.给所有家属充值 3.给单个员工充值 4.返利积分 0.其他
	private BigDecimal amount;		// 积分金额
	private Date createDate;		// 开始 创建时间
    private String rechargeDate;//充值时间 createDate格式化

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdUser getAdUser() {
        return adUser;
    }

    public void setAdUser(AdUser adUser) {
        this.adUser = adUser;
    }

    public AdGroup getAdGroup() {
        return adGroup;
    }

    public void setAdGroup(AdGroup adGroup) {
        this.adGroup = adGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

    public void setRechargeDate(String rechargeDate) {
        this.rechargeDate = rechargeDate;
    }
}