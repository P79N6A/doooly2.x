package com.doooly.business.activity.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.AbstractActivityService;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.common.constants.ActivityConstants.ActivityEnum;
import com.doooly.common.constants.Constants.MerchantApiConstants;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdUserEnterpriseChangeDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserEnterpriseChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 
 * @ClassName: XingFuJiaoHangActivityService
 * @Description: 幸福交行活动
 * @author hutao
 * @date 2018年10月9日
 *
 */
@Service
public class XingFuJiaoHangActivityService extends AbstractActivityService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final String PROJECT_ACTIVITY_URL = PropertiesConstants.dooolyBundle.getString("project.activity.url");
	@Autowired
	private AdUserServiceI userService;
	@Autowired
	private ConfigDictServiceI configService;
	@Autowired
	private AdUserEnterpriseChangeDao enterpriseChangeDao;
	@Override
	protected Boolean isDoBefore() {
		return true;
	}

	/**
	 * 预导入用户到ad_user表
	 */
	@Override
	public MessageDataBean doBefore(JSONObject beforeJson) {
		long start = System.currentTimeMillis();
		// 1.验证短信验证码是否有效
		JSONObject verificationReq = new JSONObject();
		verificationReq.put("businessId", WebService.BUSINESSID);
		verificationReq.put("storesId", WebService.STOREID);
		verificationReq.put("verificationCode", beforeJson.getString("verificationCode"));
		verificationReq.put("cardNumber", beforeJson.getString("phone"));
		String result = HTTPSClientUtils.sendPost(verificationReq, MerchantApiConstants.CHECK_VERIFICATION_CODE_URL);
		log.info("交行活动-验证码手机验证码耗时cost={}", System.currentTimeMillis() - start);
		// 验证码验证失败
		if (JSONObject.parseObject(result).getInteger("code") != 0) {
			log.warn("交行活动-手机验证码验证失败，paramJsonReq={}, result={}", beforeJson.toJSONString(), result);
			return new MessageDataBean(ActivityEnum.ACTIVITY_VERIFICATION_CODE_ERROR);
		}
		long saveStart = System.currentTimeMillis();
		// 2.插入用户到ad_user表并返回用户ID
		JSONObject userJson = new JSONObject();
		String phone = beforeJson.getString("phone");
		userJson.put("mobile", phone);
		String groupId = configService.getValueByTypeAndKey("ENTERPRISE", "JIAOHANG_GROUP_ID");
		userJson.put("groupId", groupId);
		userJson.put("name", phone);
		userJson.put("isActive", AdUser.USER_ACTIVATION_OFF);// 未激活
		userJson.put("dataSource", 0);// 平台导入

		if (beforeJson.getString("remarks") != null && !"".equals(beforeJson.getString("remarks"))) {
			userJson.put("remarks", beforeJson.getString("remarks"));
		}

		try {
			AdUser user = userService.saveUserAndPersonal(userJson);
			log.info("交行活动-保存用户耗时cost={}", System.currentTimeMillis() - saveStart);

			// 3.将用户ID放入请求参数中
			beforeJson.put("userId", user.getId());
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 员工类型
					user.setType((short) AdUser.TYPE_EMPLOYEE);
					userService.syncUserASystem(user);
					if (user.getOldGroupNum() != null && user.getGroupNum().longValue() != user.getOldGroupNum().longValue()) {
						enterpriseChangeDao.insert(new AdUserEnterpriseChange(user.getId(), user.getOldGroupNum(),
								user.getGroupNum(), Integer.valueOf(user.getIsActive())));
					}
				}
			}).start();
		} catch (Exception e) {
			log.error("交行活动-预保存用户错误", e);
			return null;
		}
		log.info("交行活动-预保存用户成功，phone={},cost(ms)={}", phone, System.currentTimeMillis() - start);
		return null;
	}

	/**
	 * 发放抽奖码
	 * 1.发放券前准备（扩展）可选
	 * 2.验证活动有效性
	 * 3.发放抽奖码
	 * 4.发放券后业务处理（扩展）可选
	 *
	 * @return
	 * @author wuzhangyi
	 * @date 创建时间：2019年01月03日 上午10:05:27
	 * @version 1.0
	 * @parameter
	 */
	public final MessageDataBean sendLotteryCode(JSONObject sendJsonReq) {
		long start = System.currentTimeMillis();
		MessageDataBean result = null;
		// 1.发放券前准备（扩展）可选
		if (isDoBefore()) {
			result = doBefore(sendJsonReq);
			if (result != null) {
				return result;
			}
		}

		try {
			// 用户ID
			String userId = sendJsonReq.getString("userId");
			// 活动ID
			String activityId = sendJsonReq.getString("activityId");
			// 活动企业id
			String groupId = configService.getValueByTypeAndKey("ENTERPRISE", "JIAOHANG_GROUP_ID");
			result = getCode(activityId, userId, groupId);

			if (!MessageDataBean.success_code.equals(result.getCode()) || ActivityEnum.ACTIVITY_RECEIVED.getCode().equals(result.getCode())) {
				log.warn("验证活动有效性失败，paramReqJson={}, error={}", sendJsonReq.toString(), JSONObject.toJSONString(result));
				return result;
			}

			AdUser adUser = userService.getById(Integer.valueOf(userId));

			if (!"2".equals(adUser.getIsActive())) {
				JSONObject jsonActive = userService.userAutoActive(adUser);
				if ("1000".equals(jsonActive.getString("code"))) {
					log.info("用户激活成功");
				}
			}

			// 4.发放券后业务处理（扩展）可选
			if (isDoAfter()) {
				doAfter(sendJsonReq);
			}

			log.info("活动发放礼品券流程结束，activityId={}, userId={}, result={}, cost(ms)={}", activityId, userId,
					JSONObject.toJSONString(result), System.currentTimeMillis() - start);
		} catch (NumberFormatException e) {
			result.setCode(MessageDataBean.failure_code);
			result.setMess(MessageDataBean.failure_mess);
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 根据活动获得券码
	 * @param activityId
	 * @param userId
	 * @return
	 */
	public MessageDataBean getCode(String activityId, String userId, String groupId) {
		MessageDataBean messageDataBean = new MessageDataBean();

		JSONObject json = new JSONObject();
		json.put("activityId", activityId);
		json.put("userId", userId);
		JSONObject resultJson = HttpClientUtil.httpPost(PROJECT_ACTIVITY_URL + "lottery/getCode", json);
		log.info("根据活动获得券码：" + resultJson.toJSONString());

		if (MessageDataBean.success_code.equals(resultJson.getString("code")) ||
				ActivityEnum.ACTIVITY_RECEIVED.getCode().equals(resultJson.getString("code"))) {
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setMess("获取抽奖码成功");

			if (ActivityEnum.ACTIVITY_RECEIVED.getCode().equals(resultJson.getString("code"))) {
				messageDataBean = new MessageDataBean(ActivityEnum.ACTIVITY_RECEIVED);
			}

			JSONObject jsonObject = (JSONObject) JSONObject.parse(resultJson.getString("data"));
			HashMap<String, Object> map = new HashMap<>();
			map.put("code", jsonObject.getString("code"));
			map.put("endTime", jsonObject.getString("endTime"));
			map.put("startTime", jsonObject.getString("startTime"));
			map.put("now", System.currentTimeMillis() + "");
			messageDataBean.setData(map);

		} else {
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess(resultJson.getString("msg"));
		}
		return messageDataBean;
	}

}
