package com.doooly.business.pay.bean;

import com.doooly.business.pay.utils.WxUtil;

/**
 * 返回微信回调结果
 * @author 2017-10-13 13:54:41 WANG
 *
 */
public class RetWxBean {

	private String return_code;
	private String return_msg;
	
	public RetWxBean(String return_code, String return_msg) {
		super();
		this.return_code = return_code;
		this.return_msg = return_msg;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	
	public String toXmlString() {
		return WxUtil.toXml(this, RetWxBean.class);
	}
	
	
	
}
