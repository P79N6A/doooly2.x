package com.doooly.business.pay.bean;

/**
 * 微信预付单返回参数封装
 * 
 *2017-10-02 17:32:27 WANG
 */
public class WxPrePayResult {
	private String appid;
	private String nonce_str;
	private String prepay_id;
	private String return_code;
	private String return_msg;
	private String mch_id;
	private String device_info;
	private String sign;
	private String result_code;
	private String trade_type;
	private String err_code;
	private String err_code_des;
	private String code_url;
	
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
	 * @return the prepay_id
	 */
	public String getPrepay_id() {
		return prepay_id;
	}
	/**
	 * @param prepay_id the prepay_id to set
	 */
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	
	
	/**
	 * @return the return_code
	 */
	public String getReturn_code() {
		return return_code;
	}
	/**
	 * @param return_code the return_code to set
	 */
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	/**
	 * @return the return_msg
	 */
	public String getReturn_msg() {
		return return_msg;
	}
	/**
	 * @param return_msg the return_msg to set
	 */
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
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
	 * @return the result_code
	 */
	public String getResult_code() {
		return result_code;
	}
	/**
	 * @param result_code the result_code to set
	 */
	public void setResult_code(String result_code) {
		this.result_code = result_code;
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
	 * @return the err_code
	 */
	public String getErr_code() {
		return err_code;
	}
	/**
	 * @param err_code the err_code to set
	 */
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	
	
	/**
	 * @return the err_code_des
	 */
	public String getErr_code_des() {
		return err_code_des;
	}
	/**
	 * @param err_code_des the err_code_des to set
	 */
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
	/**
	 * @return the code_url
	 */
	public String getCode_url() {
		return code_url;
	}
	/**
	 * @param code_url the code_url to set
	 */
	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RetDateVo.java [appid=" + appid + 
				", nonce_str=" + nonce_str +
				", return_code="+ return_code + 
				", return_msg="+ return_msg + 
				", mch_id="+ mch_id + 
				", device_info="+ device_info + 
				", sign="+ sign + 
				", result_code="+ result_code + 
				", trade_type="+ trade_type + 
				", err_code="+ err_code + 
				", err_code_des="+ err_code_des + 
				", code_url="+ code_url + 
				", prepay_id=" + prepay_id + "]";
	}}
