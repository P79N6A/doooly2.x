package com.doooly.business.pay.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 微信预支付单参数封装
 * 
 *2017-10-02 17:32:27 WANG
 */
public class WxPrePayParams {
	// 必备参数appid, mch_id, nonce_str, sign, body, 
	// out_trade_no, total_fee, spbill_create_ip, notify_url, trade_type
	
	// 微信分配的公众账号ID（企业号corpid即为此appId）
	private String appid;
	// 微信支付分配的商户号
	private String mch_id;
	// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	private String device_info;
	// 随机字符串，不长于32位
	private String nonce_str;
	// 签名，详见签名生成算法
	private String sign;
	// 商品或支付单简要描述 String(128)
	private String body;
	// 商品或支付单简要描述 String(8192)
	private String detail;
	// 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据(否)
	private String attach;
	// 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	private String out_trade_no;
	// 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private String fee_type;
	// 订单总金额，单位为分
	private String total_fee;
	// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	private String spbill_create_ip;
	// 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
	private String time_start;	
	// 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010
	private String time_expire;	
	// 商品标记，代金券或立减优惠功能的参数
	private String goods_tag;	
	// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	private String notify_url;
	// 取值如下：JSAPI，NATIVE，APP
	private String trade_type;
	// trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义
	private String product_id;
	// no_credit--指定不能使用信用卡支付
	private String limit_pay;
	// trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
	private String openid;
	
	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}
	/**
	 * @param appid the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the mch_id
	 */
	public String getMch_id() {
		return mch_id;
	}
	/**
	 * @param mch_id the mch_id to set
	 */
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	/**
	 * @return the nonce_str
	 */
	public String getNonce_str() {
		return nonce_str;
	}
	/**
	 * @param nonce_str the nonce_str to set
	 */
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	/**
	 * @return the notify_url
	 */
	public String getNotify_url() {
		return notify_url;
	}
	/**
	 * @param notify_url the notify_url to set
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * @return the out_trade_no
	 */
	public String getOut_trade_no() {
		return out_trade_no;
	}
	/**
	 * @param out_trade_no the out_trade_no to set
	 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	/**
	 * @return the spbill_create_ip
	 */
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	/**
	 * @param spbill_create_ip the spbill_create_ip to set
	 */
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	/**
	 * @return the total_fee
	 */
	public String getTotal_fee() {
		return total_fee;
	}
	/**
	 * @param total_fee the total_fee to set
	 */
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	/**
	 * @return the trade_type
	 */
	public String getTrade_type() {
		return trade_type;
	}
	/**
	 * @param trade_type the trade_type to set
	 */
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the attach
	 */
	public String getAttach() {
		return attach;
	}
	/**
	 * @param attach the attach to set
	 */
	public void setAttach(String attach) {
		this.attach = attach;
	}
	/**
	 * @return the device_info
	 */
	public String getDevice_info() {
		return device_info;
	}
	/**
	 * @param device_info the device_info to set
	 */
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/**
	 * @return the fee_type
	 */
	public String getFee_type() {
		return fee_type;
	}
	/**
	 * @param fee_type the fee_type to set
	 */
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	/**
	 * @return the time_start
	 */
	public String getTime_start() {
		return time_start;
	}
	/**
	 * @param time_start the time_start to set
	 */
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	/**
	 * @return the time_expire
	 */
	public String getTime_expire() {
		return time_expire;
	}
	/**
	 * @param time_expire the time_expire to set
	 */
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	/**
	 * @return the goods_tag
	 */
	public String getGoods_tag() {
		return goods_tag;
	}
	/**
	 * @param goods_tag the goods_tag to set
	 */
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return product_id;
	}
	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	/**
	 * @return the limit_pay
	 */
	public String getLimit_pay() {
		return limit_pay;
	}
	/**
	 * @param limit_pay the limit_pay to set
	 */
	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
