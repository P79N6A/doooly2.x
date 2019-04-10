package com.doooly.business.order.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 商家商品
 * @author 2017-09-24 09:54:32 WANG
 *
 */
public class MerchantProdcutVo {
	
	private int merchantId;
	private String remarks;
    //订单类型 1 礼包订单
    private String orderType;
    private String giftBagId;		// 礼包表主键,礼品所属那个礼包, orderType为1时传递
    //优惠券ID
    private String couponId;
	private List<ProductSkuVo> productSku ;
	
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<ProductSkuVo> getProductSku() {
		return productSku;
	}
	public void setProductSku(List<ProductSkuVo> productSku) {
		this.productSku = productSku;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getGiftBagId() {
        return giftBagId;
    }

    public void setGiftBagId(String giftBagId) {
        this.giftBagId = giftBagId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
