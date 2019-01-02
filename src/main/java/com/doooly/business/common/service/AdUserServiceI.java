package com.doooly.business.common.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdUser;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 
 * @author Albert
 *
 */
public interface AdUserServiceI {

	/**
	 * 获取当前用户信息
	 * 
	 * @param HttpServletRequest(接口注解参数-@Context
	 *            HttpServletRequest request) request token-登录验证参数 ,channel-请求来源
	 * @return AdUser
	 * @throws Exception
	 */
	public AdUser getCurrentUser(HttpServletRequest request) throws Exception;

	public long getID(AdUser user);

	public AdUser getById(Integer id);

	/**
	 * 根据手机号码获得用户
	 * @param phone
	 * @return
	 */
	public AdUser getUserByPhone(String phone);

	/**
	 * 根据手机号码和企业id获得用户
	 * @param phone
	 * @param groupId
	 * @return
	 */
	public AdUser getUserByPhoneAndGroup(String phone, String groupId);

	/**
	 * 根据会员卡号或会员手机号查找 若只有手机号则会员卡号为NULL 若只有会员卡号手机号则为NULL
	 * 
	 * @param user
	 * @return
	 */
	public AdUser get(String mobile, String cardNumber) throws Exception;

	/**
	 * 修改密码
	 * 
	 * @param mobile
	 * @param pwd
	 * @return 是否修改成功
	 */
	public boolean modifyPassword(String mobile, String pwd);

	/**
	 * 用户激活
	 * 
	 * @param cardNumber
	 * @return
	 */
	public int userActivation(String cardNumber) throws Exception;

	/**
	 * 用户激活-登录
	 * 
	 * @param cardNumber
	 * @return
	 */
	public int userActivationLogin(HashMap<String, Object> param) throws Exception;

	public boolean isCardNumberAvailable(String cardNumber) throws Exception;

	public JSONObject batchSendSms(AdUser user, JSONObject paramJson, String mobiles, String alidayuSmsCode,
			String smsContent, Boolean alidayuFlag);

	/**
	 * 会员登录验证,无卡激活 （新兜礼）
	 * 
	 * @param req
	 * @return
	 */
	public JSONObject validateUserInfo(JSONObject param) throws Exception;

	/**
	 * 会员有卡激活 （新兜礼）
	 * 
	 * @param req
	 * @return
	 */
	public JSONObject validateAndActive(JSONObject param) throws Exception;

	/**
	 * 企业口令激活
	 * 
	 * @param groupCommand-企业口令
	 * @param groupId-企业id
	 * @param mobile-手机号
	 * @param name-姓名
	 * @param workerNumber-工号
	 * @return
	 */
	public MessageDataBean groupCommandActive(JSONObject param) throws Exception;

	public boolean syncUserASystem(AdUser adUser);

	public void addIntegral(Long userId, BigDecimal integralForEach);

	public JSONObject verifyCodeAndActivation(JSONObject param) throws Exception;
}
