package com.doooly.business.touristCard.datacontract.base;

import java.io.Serializable;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public class BaseResponse implements Serializable {
	public String code;
	public String msg;
	public Object originalRes;
	/** 业务处理结果：00-成功，其他-失败 **/
	public String orderResult;
	/** 结果描述 **/
	public String resultDesc;

	public BaseResponse() {
	}

	public BaseResponse setStatus(ResponseStatus status){
		this.code = status.code();
		this.msg = status.message();
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getOriginalRes() {
		return originalRes;
	}

	public void setOriginalRes(Object originalRes) {
		this.originalRes = originalRes;
	}

	public String getOrderResult() {
		return orderResult;
	}

	public void setOrderResult(String orderResult) {
		this.orderResult = orderResult;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
}
