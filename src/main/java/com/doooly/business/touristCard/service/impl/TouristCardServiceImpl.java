package com.doooly.business.touristCard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.touristCard.datacontract.base.ResponseStatus;
import com.doooly.business.touristCard.datacontract.entity.SctcdAccount;
import com.doooly.business.touristCard.datacontract.entity.SctcdRechargeDetail;
import com.doooly.business.touristCard.datacontract.request.*;
import com.doooly.business.touristCard.datacontract.response.*;
import com.doooly.business.touristCard.service.TouristCardService;
import com.doooly.business.touristCard.utils.Rsa;
import com.doooly.business.touristCard.utils.XmlUtil;
import com.doooly.common.constants.PropertiesHolder;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.common.util.RSAUtils;
import com.doooly.dao.reachad.TouristCardDao;
import com.doooly.entity.reachad.AdUserBusinessExpansion;
import com.google.common.base.Charsets;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
@Service
public class TouristCardServiceImpl implements TouristCardService {
	private static Logger logger = LoggerFactory.getLogger(TouristCardServiceImpl.class);

	@Autowired
	private TouristCardDao touristCardDao;

	@Override
	public List<SctcdRechargeDetail> findRechargeHistory(FindRechargeHistoryRequest request) {
		return touristCardDao.findRechargeHistory(request);
	}

	@Override
	public int countRechargeNum(CountRechargeNumRequest request) {
		return touristCardDao.countRechargeNum(request);
	}

	@Override
	public List<SctcdAccount> findSctcdAccount(FindSctcdAccountRequest request) {
		return touristCardDao.findSctcdAccount(request);
	}

	@Override
	public List<SctcdAccount> findOneSctcdAccount(FindSctcdAccountRequest request) {
		return touristCardDao.findOneSctcdAccount(request);
	}

	@Override
	public int abandonedSctcdAccount(int id) {
		AdUserBusinessExpansion adUserBusinessExpansion = new AdUserBusinessExpansion();
		adUserBusinessExpansion.setId(id);
		adUserBusinessExpansion.setDelFlag("1");
		return touristCardDao.updateSctcdAccountDelFlag(adUserBusinessExpansion);
	}

	@Override
	public int enableSctcdAccount(int id) {
		AdUserBusinessExpansion adUserBusinessExpansion = new AdUserBusinessExpansion();
		adUserBusinessExpansion.setId(id);
		adUserBusinessExpansion.setDelFlag("0");
		return touristCardDao.updateSctcdAccountDelFlag(adUserBusinessExpansion);
	}

	@Override
	public VerifyAccountResponse verifyAccount(VerifyAccountRequest request) throws Exception {
		String url = PropertiesHolder.getProperty("SCTCD_SERVICE_URL");
		String paramsString = request.getParamsString();
		logger.info("调用旅游卡<<验证开户>>接口，url = {}, paramsString = {}", url, paramsString);
		String resXML = HttpClientUtil.httpsGet(url + "?" + paramsString);
		logger.info("旅游卡<<验证开户>>接口响应的，resXML = {}", resXML);

		String sctcdResponseXML = XmlUtil.getSingleNodeXML(resXML, "//sctcd/response");
		XStream xstream = new XStream();
		xstream.alias("response", VerifyAccountResponse.class);
		VerifyAccountResponse response = (VerifyAccountResponse) xstream.fromXML(sctcdResponseXML);
		response.setStatus(ResponseStatus.SUCCESS);
		response.setOriginalRes(resXML);
		return response;
	}

	@Override
	public NewAccountResponse newAccount(NewAccountRequest request) throws Exception {
		String url = PropertiesHolder.getProperty("SCTCD_SERVICE_URL");
		String paramsString = request.getParamsString();
		logger.info("调用旅游卡<<新开户>>接口，url = {}, paramsString = {}", url, paramsString);
		String resXML = HttpClientUtil.httpsGet(url + "?" + paramsString);
		logger.info("旅游卡<<新开户>>接口响应的，resXML = {}", resXML);

		String sctcdResponseXML = XmlUtil.getSingleNodeXML(resXML, "//sctcd/response");
		XStream xstream = new XStream();
		xstream.alias("response", NewAccountResponse.class);
		NewAccountResponse response = (NewAccountResponse) xstream.fromXML(sctcdResponseXML);

		//将旅游卡<<新开户>>成功的信息，保存到数据库中
		if ("00".equals(response.getOrderResult())) {
			touristCardDao.addSctcdAccount(new AdUserBusinessExpansion(request));
			response.setStatus(ResponseStatus.SUCCESS);
		} else {
			response.setStatus(ResponseStatus.FAIL);
		}
		response.setOriginalRes(resXML);
		return response;
	}

	@Override
	public AccountRechargeResponse accountRecharge(OrderVo order) {
		AccountRechargeRequest request = new AccountRechargeRequest(order);
		AccountRechargeResponse response = new AccountRechargeResponse();
		try {
			response = accountRecharge(request);
			logger.info("accountRechargeResponse = " + JSONObject.toJSONString(response));
		} catch (Exception e) {
			logger.error("accountRecharge Error", e);
			response.setStatus(ResponseStatus.ERROR);
		}
		return response;
	}

	private AccountRechargeResponse accountRecharge(AccountRechargeRequest request) throws Exception {
		String url = PropertiesHolder.getProperty("SCTCD_SERVICE_URL");
		String RSA_MERCHANT_PRIVATE_KEY = PropertiesHolder.getProperty("RSA_MERCHANT_PRIVATE_KEY");
		String RSA_SCTCD_PUBLIC_KEY = PropertiesHolder.getProperty("RSA_SCTCD_PUBLIC_KEY");
		String paramsString = request.getParamsString();

		//生成sign签名
		String sign = RSAUtils.sign(request.getSignContent(), RSA_MERCHANT_PRIVATE_KEY, Charsets.UTF_8.name());
		paramsString = paramsString.replaceAll("@sign", sign);

		logger.info("调用旅游卡<<账户充值>>接口，url = {}, paramsString = {}", url, paramsString);
		String resXML = HttpClientUtil.httpsGet(url + "?" + paramsString);
		logger.info("旅游卡<<账户充值>>接口响应的，resXML = {}", resXML);

		String sctcdResponseXML = XmlUtil.getSingleNodeXML(resXML, "//sctcd/response");
		XStream xstream = new XStream();
		xstream.alias("response", AccountRechargeResponse.class);
		AccountRechargeResponse response = (AccountRechargeResponse) xstream.fromXML(sctcdResponseXML);

		//验证返回的sign签名
		boolean isTrue = false;
		String resSign = response.getSign();
		if ( StringUtils.isEmpty(resSign) ) {
			isTrue = true;
		} else {
			isTrue = Rsa.verifyOrderInfo(
					Rsa.pub_Calc(resSign, RSA_SCTCD_PUBLIC_KEY),
					response.getMerchantOrderNum(),
					response.getMerchantOrderFee(),
					response.getPayResult());
		}

		if (isTrue) {
			response.setStatus(ResponseStatus.SUCCESS);
		} else {
			response.setStatus(ResponseStatus.VERIFY_SIGN_ERROR);
		}
		response.setOriginalRes(resXML);
		return response;
	}

	@Override
	public QueryAccountBalanceResponse queryAccountBalance(QueryAccountBalanceRequest request) throws Exception {
		String url = PropertiesHolder.getProperty("SCTCD_SERVICE_URL");
		String paramsString = request.getParamsString();
		logger.info("调用旅游卡<<账户余额查询>>接口，url = {}, paramsString = {}", url, paramsString);
		String resXML = HttpClientUtil.httpsGet(url + "?" + paramsString);
		logger.info("旅游卡<<账户余额查询>>接口响应的，resXML = {}", resXML);

		String sctcdResponseXML = XmlUtil.getSingleNodeXML(resXML, "//sctcd/response");
		XStream xstream = new XStream();
		xstream.alias("response", QueryAccountBalanceResponse.class);
		QueryAccountBalanceResponse response = (QueryAccountBalanceResponse) xstream.fromXML(sctcdResponseXML);
		response.setStatus(ResponseStatus.SUCCESS);
		response.setOriginalRes(resXML);
		return response;
	}

	@Override
	public QueryCardBalanceResponse queryCardBalance(QueryCardBalanceRequest request) throws Exception {
		String url = PropertiesHolder.getProperty("SCTCD_SERVICE_URL");
		String paramsString = request.getParamsString();
		logger.info("调用旅游卡<<卡片余额查询>>接口，url = {}, paramsString = {}", url, paramsString);
		String resXML = HttpClientUtil.httpsGet(url + "?" + paramsString);
		logger.info("旅游卡<<卡片余额查询>>接口响应的，resXML = {}", resXML);

		String sctcdResponseXML = XmlUtil.getSingleNodeXML(resXML, "//sctcd/response");
		XStream xstream = new XStream();
		xstream.alias("response", QueryCardBalanceResponse.class);
		QueryCardBalanceResponse response = (QueryCardBalanceResponse) xstream.fromXML(sctcdResponseXML);
		response.setStatus(ResponseStatus.SUCCESS);
		response.setOriginalRes(resXML);
		return response;
	}
}
