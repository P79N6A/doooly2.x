package com.doooly.entity.reachad;

import java.math.BigDecimal;

/**
 * @className: AdAppUserShoppingThrift
 * @description: 兜礼会员，购物节省信息
 * @author: wangchenyu
 * @date: 2018-02-23 17:50
 */
public class AdAppUserShoppingThrift {
	private int thriftTotal;
	private BigDecimal thriftAmount;

	public int getThriftTotal() {
		return thriftTotal;
	}

	public void setThriftTotal(int thriftTotal) {
		this.thriftTotal = thriftTotal;
	}

	public BigDecimal getThriftAmount() {
		return thriftAmount;
	}

	public void setThriftAmount(BigDecimal thriftAmount) {
		this.thriftAmount = thriftAmount;
	}
}
