package com.doooly.business.shanghaibank.api.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpException;

import java.io.IOException;

public class VirAccountApi extends Api {

	public VirAccountApi(String host, int port, String customer, String key, String pfxPath, String pfxPwd) {
		super(host, port, customer, key, pfxPath, pfxPwd);
	}
	/**
	 * 虚账户单笔代缴
	 * @param
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19SingleCharge(String channelFlowNo, String eAcctNo, String amount,
                                      String recvAccount, String recvAccountName, String recvAccountBank,
                                      String usage, String platformSummary) throws HttpException, IOException{
		String servletPath="/i2dbank/C19SingleCharge.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("amount", amount);
		jsonObject.put("recvAccount", recvAccount);
		jsonObject.put("recvAccountName", recvAccountName);
		jsonObject.put("recvAccountBank", recvAccountBank);
		jsonObject.put("usage", usage);
		jsonObject.put("platformSummary", platformSummary);
		return post(servletPath, jsonObject.toJSONString());
	}
	/**
	 * 虚账户提款
	 * @param channelFlowNo
	 * @param eAcctNo
	 * @param amount
	 * @param usage
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19VirWithDrawalsInq(String channelFlowNo, String eAcctNo, String amount,
                                           String usage) throws HttpException, IOException{
		String servletPath="/i2dbank/C19VirWithDrawalsInq.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("amount", amount);
		jsonObject.put("usage", usage);
		return post(servletPath, jsonObject.toJSONString());
	}

	/**
	 * 平台虚账户提款
	 * @param channelFlowNo
	 * @param eAcctNo
	 * @param amount
	 * @param usage
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject C19ChannelVirWithDrawals(String channelFlowNo, String eAcctNo, String amount,
                                           String usage) throws HttpException, IOException{
		String servletPath="/i2dbank/C19ChannelVirWithDrawals.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("amount", amount);
		jsonObject.put("usage", usage);
		return post(servletPath, jsonObject.toJSONString());
	}
	/**
	 * 虚账号单笔代发
	 * @param channelFlowNo
	 * @param eAcctNo
	 * @param amount
	 * @param payAccount
	 * @param payAccountName
	 * @param usage
	 * @param platformSummary
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19VirSReTrigSer(String channelFlowNo, String eAcctNo, String amount,
                                       String payAccount, String payAccountName,
                                       String usage, String platformSummary) throws HttpException, IOException{
		String servletPath="/i2dbank/C19VirSReTrigSer.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("amount", amount);
		jsonObject.put("payAccount", payAccount);
		jsonObject.put("payAccountName", payAccountName);
		jsonObject.put("usage", usage);
		jsonObject.put("platformSummary", platformSummary);
		return post(servletPath, jsonObject.toJSONString());
	}
	/**
	 * 虚账户交易指令状态查询
	 * @param channelFlowNo
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19VirAcctDeleCharSttInq(String channelFlowNo, String channelId) throws HttpException, IOException{
		String servletPath="/i2dbank/C19VirAcctDeleCharSttInq.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		return post(servletPath, jsonObject.toJSONString());
	}

	/**
	 * 虚账户交易明细查询
	 * @param eAcctNo 虚账号
	 * @param beginDate 开始时间
	 * @param endDate   结束时间
	 * @param otherAccNo 对方账号
	 * @param otherAccName 对方户名
	 * @param voucherNo 凭证编号
	 * @param pageIndex 查询页码
	 * @param pageSize  每页多少条记录
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19VirAcctTranDtlQry(String eAcctNo, String beginDate, String endDate,
                                           String otherAccNo, String otherAccName, String voucherNo, String pageIndex, String pageSize) throws HttpException, IOException{
		String servletPath="/i2dbank/C19VirAcctTranDtlQry.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("beginDate", beginDate);
		jsonObject.put("endDate", endDate);
		jsonObject.put("otherAccNo", otherAccNo);
		jsonObject.put("otherAccName", otherAccName);
		jsonObject.put("voucherNo", voucherNo);
		jsonObject.put("pageIndex", pageIndex);
		jsonObject.put("pageSize", pageSize);
		return post(servletPath, jsonObject.toJSONString());
	}


	public JSONObject c19VirAcctBlanceInq(String eAcctNo) throws HttpException, IOException{
		String servletPath="/i2dbank/C19VirAcctBlanceInq.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("eAcctNo", eAcctNo);
		return post(servletPath, jsonObject.toJSONString());
	}

	public JSONObject c19VirAmtFrozOrNot(String channelFlowNo, String eAcctNo,
                                         String txnType, String applyAmount, String platformSummary, String notes)
			throws HttpException, IOException {
		String servletPath="/i2dbank/C19VirAmtFrozOrNot.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		jsonObject.put("eAcctNo", eAcctNo);
		jsonObject.put("txnType", txnType);
		jsonObject.put("applyAmount", applyAmount);
		jsonObject.put("platformSummary", platformSummary);
		jsonObject.put("notes", notes);
		return post(servletPath, jsonObject.toJSONString());
	}
	public JSONObject c19VirOpenIdentifyCard(String channelFlowNo, String custName,
                                             String idNo, String expDay, String mobilePhone, String bindCardNo, String reservedPhone,
                                             String elementNo)
					throws HttpException, IOException {
		String servletPath="/i2dbank/C19VirOpenIdentifyCard.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		jsonObject.put("custName", custName);
		jsonObject.put("idNo", idNo);
		jsonObject.put("expDay", expDay);
		jsonObject.put("mobilePhone", mobilePhone);
		jsonObject.put("bindCardNo", bindCardNo);
		jsonObject.put("reservedPhone", reservedPhone);
		jsonObject.put("elementNo", elementNo);
		return post(servletPath, jsonObject.toJSONString());
	}
	public Object c19VirOpenIdentifyCardInq(String channelFlowNo)
			throws HttpException, IOException {
		String servletPath="/i2dbank/C19VirOpenIdentifyCardInq.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("channelFlowNo", channelFlowNo);
		return post(servletPath, jsonObject.toJSONString());
	}

	/**
	 * 虚账户单要素开户
	 * @param cNName
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19SCrVirSingleAccount(String cNName) throws HttpException, IOException{
		String servletPath="/i2dbank/C19SCrVirSingleAccount.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("cNName", cNName);
		return post(servletPath, jsonObject.toJSONString());
	}

	/**
	 * 单要素虚账户开户状态查询
	 * @param cNName
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public JSONObject c19SCrVirSingleAccountStInq(String cNName) throws HttpException, IOException{
		String servletPath="/i2dbank/C19SCrVirSingleAccountStInq.API";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("cNName", cNName);
		return post(servletPath, jsonObject.toJSONString());
	}
}
