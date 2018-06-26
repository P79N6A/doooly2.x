package com.doooly.business.shanghaibank.api.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpException;

import java.io.IOException;
import java.util.UUID;

public class PDFAPI extends Api{

	public PDFAPI(String host, int port, String channels, String key, String pfxPath, String pfxPwd) {
		super(host, port, channels, key, pfxPath, pfxPwd);
	}
	public JSONObject c19PrintReceipt(String eAcctNo, String startDate, String endDate, String stmt) throws HttpException, IOException{
		String servletPath="/i2dbank/C19PrintReceipt.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("startDate", startDate);
		jsonObject.put("endDate", endDate);
		jsonObject.put("stmt", stmt);
		return post(servletPath,jsonObject.toJSONString());
	}
	public JSONObject c19RlectronicReceipt(String eAcctNo, String date) throws HttpException, IOException{
		String servletPath="/i2dbank/C19RlectronicReceipt.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("date", date);
		jsonObject.put("feeBaseLogId", UUID.randomUUID().toString());
		return post(servletPath,jsonObject.toJSONString());
	}
	
	/**
	 * 虚账户打印代缴凭证电子回单
	 * @param eAcctNo
	 * @param startDate
	 * @param endDate
	 * @param channelFlowNo
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19VirAcctPrintReceipt(String eAcctNo, String startDate, String endDate, String channelFlowNo) throws HttpException, IOException{
		String servletPath="/i2dbank/C19VirAcctPrintReceipt.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("startDate", startDate);
		jsonObject.put("endDate", endDate);
		jsonObject.put("channelFlowNo", channelFlowNo);
		return post(servletPath,jsonObject.toJSONString());
	}
}
