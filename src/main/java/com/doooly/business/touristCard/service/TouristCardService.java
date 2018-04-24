package com.doooly.business.touristCard.service;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.touristCard.datacontract.entity.SctcdAccount;
import com.doooly.business.touristCard.datacontract.entity.SctcdRechargeDetail;
import com.doooly.business.touristCard.datacontract.request.*;
import com.doooly.business.touristCard.datacontract.response.*;

import java.util.List;

/**
 * Created by 王晨宇 on 2018/1/16.
 */
public interface TouristCardService {
	public final static String SUCCESS_CODE = "01";

	/**
	 * 查询用户旅游卡充值记录
	 */
	List<SctcdRechargeDetail> findRechargeHistory(FindRechargeHistoryRequest request);

	/**
	 * 统计用户旅游卡充值次数
	 */
	int countRechargeNum(CountRechargeNumRequest request);

	/**
	 * 查询用户旅游卡绑定信息记录，一个用户可能绑定多张旅游卡
	 */
	List<SctcdAccount> findSctcdAccount(FindSctcdAccountRequest request);

	/**
	 * 查询用户旅游卡绑定信息记录，用户之前可能逻辑删除此卡
	 */
	List<SctcdAccount> findOneSctcdAccount(FindSctcdAccountRequest request);

	/**
	 * 用户逻辑删除，选中的旅游卡绑定信息
	 */
	int abandonedSctcdAccount(int id);

	/**
	 * 启用之前用户逻辑删除的旅游卡绑定信息
	 */
	int enableSctcdAccount(int id);

	/**
	 * 验证开户
	 */
	VerifyAccountResponse verifyAccount(VerifyAccountRequest request) throws Exception;

	/**
	 * 新开户
	 */
	NewAccountResponse newAccount(NewAccountRequest request) throws Exception;

	/**
	 * 账户充值
	 */
	AccountRechargeResponse accountRecharge(OrderVo order);

	/**
	 * 账户余额查询
	 */
	QueryAccountBalanceResponse queryAccountBalance(QueryAccountBalanceRequest request) throws Exception;

	/**
	 * 卡片余额查询
	 */
	QueryCardBalanceResponse queryCardBalance(QueryCardBalanceRequest request) throws Exception;

}
