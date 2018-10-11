package com.doooly.business.activity.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.AbstractActivityService;
import com.doooly.business.common.service.impl.AdUserService;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.common.constants.ActivityConstants.ActivityEnum;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.webservice.WebService;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdUser;

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
	@Autowired
	private AdUserService userService;
	@Autowired
	private ConfigDictServiceI configService;

	@Override
	protected Boolean isDoBefore() {
		return true;
	}

	/**
	 * 预导入用户到ad_user表
	 */
	@Override
	protected MessageDataBean doBefore(JSONObject beforeJson) {
		long start = System.currentTimeMillis();
		// 1.验证短信验证码是否有效
		String verificationCodeUrl = PropertiesConstants.dooolyBundle.getString("merchant.api.base.url")
				+ "api/services/rest/checkVerificationCode";
		JSONObject verificationReq = new JSONObject();
		verificationReq.put("businessId", WebService.BUSINESSID);
		verificationReq.put("storesId", WebService.STOREID);
		verificationReq.put("verificationCode", beforeJson.getString("verificationCode"));
		verificationReq.put("cardNumber", beforeJson.getString("phone"));
		String result = HTTPSClientUtils.sendPost(verificationReq, verificationCodeUrl);
		// 验证码验证失败
		if (JSONObject.parseObject(result).getInteger("code") != 0) {
			log.warn("交行活动-手机验证码验证失败，paramJsonReq={}, result={}", beforeJson.toJSONString(), result);
			return new MessageDataBean(ActivityEnum.ACTIVITY_VERIFICATION_CODE_ERROR);
		}
		// 2.插入用户到ad_user表并返回用户ID
		JSONObject userJson = new JSONObject();
		String phone = beforeJson.getString("phone");
		userJson.put("mobile", phone);
		String groupId = configService.getValueByTypeAndKey("ENTERPRISE", "JIAOHANG_GROUP_ID");
		userJson.put("groupId", groupId);
		userJson.put("name", phone);
		userJson.put("isActive", AdUser.USER_ACTIVATION_OFF);// 未激活
		userJson.put("dataSource", 0);// 平台导入
		
		try {
			AdUser user = userService.saveUserAndPersonal(userJson);
			// 3.将用户ID放入请求参数中
			beforeJson.put("userId", user.getId());
			new Thread(new Runnable() {
				@Override
				public void run() {
					//员工类型
					user.setType((short)AdUser.TYPE_EMPLOYEE);
					userService.syncUserASystem(user);
				}
			}).start();
		} catch (Exception e) {
			log.error("交行活动-预保存用户错误", e);
			return null;
		}
		log.info("交行活动-预保存用户成功，phone={},cost(ms)={}", phone, System.currentTimeMillis() - start);
		return null;
	}

}
