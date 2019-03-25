package com.doooly.business.order.vo;

/**
 * 商品SKU
 * @author 2017-09-24 09:42:32 WANG
 */
public class ProductSkuVo {
	//商品ID
	private int productId;
	//SKU 
	private int skuId;
	//购买数量
	private int buyNum;
    //商品类型
    private int productType;
    //话费充值运营商 cmcc cucc ctc
    private String operator;
    //订单类型 1 礼包订单
    private String orderType;
    private String giftBagId;		// 礼包表主键,礼品所属那个礼包, orderType为1时传递

	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getSkuId() {
		return skuId;
	}
	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
}
