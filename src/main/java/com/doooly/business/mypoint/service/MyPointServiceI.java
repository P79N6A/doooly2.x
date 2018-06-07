package com.doooly.business.mypoint.service;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * @Description: 积分servcie
 * @author: qing.zhang
 * @date: 2017-05-18
 */
public interface MyPointServiceI {

	JSONObject getFamilyRebateInfo(JSONObject data);

	// 查询可用积分和共享积分
	MessageDataBean queryUserIntegral(String businessId, String username, String password, String userId);

	MessageDataBean getAvailablePoints(String income, String userId, Integer currentPage, Integer pageSize)
			throws IOException;

	MessageDataBean getReturnPoints(String income, String userId, Integer currentPage, Integer pageSize);

	MessageDataBean getAvailablePointDetail(String availablePointsId, String userId);

	MessageDataBean getReturnPointDetail(String returnPointsId, String userId);

	MessageDataBean getIntegralRechargeListData(Long userId, Integer currentPage, Integer pageSize);

	MessageDataBean doIntegralRecharge(Long userId, String cardPassword)throws Exception;

}
