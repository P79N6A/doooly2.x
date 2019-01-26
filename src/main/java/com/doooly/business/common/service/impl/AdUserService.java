package com.doooly.business.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdActiveCodeServiceI;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.business.myaccount.service.impl.AdSystemNoitceService;
import com.doooly.business.payment.constants.GlobalResultStatusEnum;
import com.doooly.business.reachLife.LifeGroupService;
import com.doooly.business.user.service.UserService;
import com.doooly.business.thirdpart.constant.ThirdPartConstant;
import com.doooly.business.user.service.UserServiceI;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.ConstantsV2;
import com.doooly.common.constants.DaHuaConstants;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.common.util.MD5Utils;
import com.doooly.common.util.ThirdPartySMSUtil;
import com.doooly.common.util.WechatUtil;
import com.doooly.dao.payment.VoucherCardRecordDao;
import com.doooly.dao.reachad.AdActiveCodeDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdInvitationRecordDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdUserPersonalInfoDao;
import com.doooly.dao.reachlife.LifeGroupDao;
import com.doooly.dao.reachlife.LifeMemberDao;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.payment.VoucherCardRecord;
import com.doooly.entity.reachad.AdActiveCode;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdInvitationRecord;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserConn;
import com.doooly.entity.reachad.AdUserPersonalInfo;
import com.doooly.entity.reachlife.LifeGroup;
import com.doooly.entity.reachlife.LifeMember;
import com.doooly.entity.reachlife.LifeWechatBinding;
import com.doooly.publish.rest.life.impl.FamilyInviteService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author linking
 *
 */
@Service
public class AdUserService implements AdUserServiceI {

	private static final Logger logger = LoggerFactory.getLogger(FamilyInviteService.class);
	public static final String invite_url = ResourceBundle.getBundle("doooly").getString("invite_url");
	private static final String ACTIVATE_CODE_FAIL_COUNT = "activate_code_fail_count_";
	private Lock lock = new ReentrantLock();

	@Autowired
	private StringRedisTemplate stringRedis;

	/** B库用户DAO */
	@Autowired
	private AdUserDao adUserDao;

	/** A库用户Service */
	@Autowired
	private LifeMemberService lifeMemberService;

	/** B库激活码DAO */
	@Autowired
	private AdActiveCodeDao adActiveCodeDao;

	/** A库会员DAO */
	@Autowired
	private LifeMemberDao lifeMemberDao;

	/** B库邀请记录DAO */
	@Autowired
	private AdInvitationRecordDao adInvitationRecordDao;

	/** A库企业Service */
	@Autowired
	private LifeGroupService lifeGroupService;

	/** B库企业DAO */
	@Autowired
	private AdGroupDao adGroupDao;

	/** B库用户信息DAO */
	@Autowired
	private AdUserPersonalInfoDao adUserPersonalInfoDao;

	/** A库企业DAO */
	@Autowired
	private LifeGroupDao lifeGroupDao;
	/** payment库积分卡DAO */
	@Autowired
	private VoucherCardRecordDao voucherCardRecordDao;

	@Autowired
	private AdSystemNoitceService adSystemNoitceService;

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private ConfigDictServiceI configDictServiceI;

	@Autowired
	private AdActiveCodeServiceI adActiveCodeServiceI;



	public AdUser getById(Integer id) {
		return adUserDao.getById(id);
	}

	@Override
	public AdUser getUserByPhone(String phone) {
		return adUserDao.findByMobile(phone);
	}

	@Override
	public AdUser getUserByPhoneAndGroup(String phone, String groupId) {
		AdUser adUser = new AdUser();
		adUser.setTelephone(phone);
		adUser.setGroupNum(Long.valueOf(groupId));
		return adUserDao.getUserByPhoneAndGroup(adUser);
	}

	@Override
	public AdUser getCurrentUser(HttpServletRequest request) throws Exception {
		AdUser adUser = new AdUser();
		try {
			if (request != null) {
				String token = request.getHeader(ConstantsLogin.TOKEN);
				String channel = request.getHeader(ConstantsLogin.CHANNEL);
				logger.info("====【getCurrentUser】-token：" + token + ",==channel：" + channel);

				if (StringUtils.isNotEmpty(token)) {
					String userId = redisTemplate.opsForValue().get(token);
					adUser = adUserDao.getCurrentUser(userId);
					if (adUser != null) {
						return adUser;
					}
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 得到用户id
	 */
	@Override
	public long getID(AdUser user) {
		AdUser adUser = adUserDao.get(user);
		if (adUser == null) {
			return -1;
		}
		return adUser.getId();
	}

	@Override
	public AdUser get(String mobile, String cardNumber) throws Exception {
		AdUser user = new AdUser();
		user.setCardNumber(cardNumber);
		user.setTelephone(mobile);
		return adUserDao.get(user);
	}

	@Override
	public boolean modifyPassword(String mobile, String newPwd) {
		int isSuccess = adUserDao.updatePwd(mobile, newPwd);
		if (isSuccess == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 用户激活操作
	 */
	@Override
	public int userActivation(String cardNumber) throws Exception {
		AdUser adUser = new AdUser();
		adUser.setCardNumber(cardNumber);
		adUser.setDataSyn(AdUser.DATA_SYN_ON);// 设置该会员数据能被同步
		adUser.setUpdateDate(new Date());// 设置更新(操作激活)时间
		adUser.setIsActive(AdUser.USER_ACTIVATION_ON);// 设置激活标识为"激活"
		adUser.setActiveDate(new Date());// 设置激活时间

		return adUserDao.updateActiveStatus(adUser);
	}

	/**
	 * 用户激活操作
	 */
	@Override
	public int userActivationLogin(HashMap<String, Object> param) throws Exception {
		AdUser adUser = new AdUser();
		adUser.setTelephone(param.get("mobile").toString());
		adUser.setCardNumber(param.get("cardNumber").toString());
		adUser.setDataSyn(AdUser.DATA_SYN_ON);// 设置该会员数据能被同步
		adUser.setUpdateDate(new Date());// 设置更新(操作激活)时间
		adUser.setIsActive(AdUser.USER_ACTIVATION_ON);// 设置激活标识为"激活"
		adUser.setActiveDate(new Date());// 设置激活时间
		adUser.setPassword(param.get("password").toString());// 设置登录密码
		adUser.setPayPassword(param.get("password").toString());// 设置支付密码
        //白名单激活
        //数据来源 0:平台导入 1:员工自主申请(白名单)，2：企业口令激活，3：卡激活，4：专属码
        logger.info("类型标示为白名单激活：{}",param.get("mobile").toString());
        //userServiceI.updatePersonInfoDataSources(param.get("mobile").toString(),1);
		return adUserDao.updateActiveStatus(adUser);
	}

	/**
	 * 查看会员号是否可用
	 */
	@Override
	public boolean isCardNumberAvailable(String cardNumber) throws Exception {
		AdUser adUser = new AdUser();
		adUser.setCardNumber(cardNumber);
		AdUser temp = adUserDao.findAvailableByCardNumber(adUser);
		if (temp == null) {
			return false;
		}
		return true;
	}

	@Override
	/**
	 * 批量给会员发送活动通知短信
	 */
	public JSONObject batchSendSms(AdUser user, JSONObject paramSmSJson, String mobiles, String alidayuSmsCode,
								   String smsContent, Boolean alidayuFlag) {
		JSONObject result = new JSONObject();
		int sendResult = -1;
		List<String> failedTelList = new ArrayList<String>();
		int successCount = 0;// 发送成功条数
		int failedCount = 0;// 发送失败条数
		// 0.若手机号为空则根据查询条件直接从数据库查询会员手机号
		// if(StringUtils.isBlank(mobiles)){
		// //1.会员总记录数
		// int totalUserCount = adUserDao.getUserCounts(user);
		// int offset = 0;//当前偏移量，用于分页获取手机号
		// //2.获取会员手机号
		// //2.1组装查询条件
		// Map<String,Object> paramMap = new HashMap<String,Object>();
		// paramMap.put("groupNum", user.getGroupNum());
		// paramMap.put("cardNumber", user.getCardNumber());
		// paramMap.put("type", user.getType());
		//
		// while(offset<totalUserCount){
		// paramMap.put("offset", offset);
		// paramMap.put("page",
		// ThirdPartySMSConstatns.SMSSendConfig.MOBILE_MAX_COUNT);
		// //2.2获取会员手机号
		// mobiles = adUserDao.getUserTels(paramMap);
		// if(StringUtils.isNotEmpty(mobiles)){
		// //批量发送短信
		// sendResult = ThirdPartySMSUtil.sendMsg(mobiles, paramSmSJson,
		// alidayuSmsCode, smsContent,alidayuFlag);
		// int length = mobiles.split(",").length;
		// if(sendResult==0){
		// successCount+=length;
		// }else{
		// failedCount+=length;
		// failedTelList.add(mobiles);
		// }
		// }
		// offset += ThirdPartySMSConstatns.SMSSendConfig.MOBILE_MAX_COUNT;
		// }
		// }else{
		// 批量发送短信
		sendResult = ThirdPartySMSUtil.sendMsg(mobiles, paramSmSJson, alidayuSmsCode, smsContent, alidayuFlag);
		int length = mobiles.split(",").length;
		if (sendResult == 0) {
			successCount += length;
		} else {
			failedCount += length;
			failedTelList.add(mobiles);
		}
		// }
		// 汇总批量发送短信结果
		result.put("successCount", successCount);
		result.put("failedCount", failedCount);
		result.put("failTelList", failedTelList);
		return result;
	}

	/**
	 * 验证用户信息(登录)
	 */
	@Override
	public JSONObject validateUserInfo(JSONObject param) throws Exception {
		logger.info("validateUserInfo() param = " + param);
		JSONObject jsonResult = new JSONObject();
		// 登录成功,返回数据库加密密码
		String dataBasePassword = "";
		try {
			AdUser userParam = new AdUser();
			// 登录名
			String loginName = param.getString("loginName");
			userParam.setCardNumber(loginName);
			userParam.setTelephone(loginName);
			// 是否需要验证密码
			String is_check = param.getString("is_check");
			// 接收登录输入密码
			String inputPassword = param.getString("password");
			// 终端渠道
			String channel = param.getString(ConstantsLogin.CHANNEL);
			//20190109 第三方登录激活zhangqing
			MessageDataBean messageDataBean = this.userBind(param);
			if(messageDataBean.getCode().equals(MessageDataBean.failure_code)){
				jsonResult.put(ConstantsLogin.CODE, MessageDataBean.failure_code);
				jsonResult.put(ConstantsLogin.MESS, MessageDataBean.failure_mess);
				return jsonResult;
			}
			final AdUser loginUser = adUserDao.getUserInfo(userParam);
			logger.info("====【validateUserInfo】登录名获取用户信息：" + JSONObject.toJSONString(loginUser));
			if (loginUser == null) {
				jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_NOT_EXIST.getCode());
				jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_NOT_EXIST.getMsg());
				return jsonResult;
			} else {
				// 非武钢企业会员不可以登录武钢APP
				if (channel.equals(ConstantsLogin.CHANNEL_WISCOAPP)) {
					AdUser userModel = adUserDao.getUserByTelephoneBloc(loginName);
					if (userModel == null) {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.NOT_WUGANG_USER.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.NOT_WUGANG_USER.getMsg());
						return jsonResult;
					}
				}
				// 放入用户主键
				jsonResult.put("userId", loginUser.getId());
				if ("1".equals(loginUser.getDelFlag())) {
					if (loginUser.getIsAudit() != null && loginUser.getIsAudit() == 0
							&& loginUser.getDataSources() != null && loginUser.getDataSources() == 1) {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.IS_CHECKING.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.IS_CHECKING.getMsg());
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						jsonResult.put("date", sdf.format(loginUser.getUpdateDate()));
						return jsonResult;
					} else if (loginUser.getIsAudit() != null && loginUser.getIsAudit() == 2
							&& loginUser.getDataSources() != null && loginUser.getDataSources() == 1) {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.CHECK_NOT_PASS.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.CHECK_NOT_PASS.getMsg());
						return jsonResult;
					} else {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_DELETED.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_DELETED.getMsg());
						return jsonResult;
					}
				}
				if ("3".equals(loginUser.getIsActive()) || "4".equals(loginUser.getIsActive())) {
					jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.FREEZE_OR_CANCEL.getCode());
					jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.FREEZE_OR_CANCEL.getMsg());
					return jsonResult;
				}
				if ("1".equals(is_check)) {
					// 验证是否激活
					if (!"2".equals(loginUser.getIsActive())) {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.TELLOGIN_TYPE.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.TELLOGIN_TYPE.getMsg());
						jsonResult.put("password", dataBasePassword);
						return jsonResult;
					}
					// md5Password = MD5Utils.encode(inputPassword);
					// 加密密码
					String md5Password = inputPassword;
					logger.info("====【validateUserInfo】传入密码：" + md5Password + ",数据库密码：" + loginUser.getPassword());
					if (!loginUser.getPassword().equals(md5Password)) {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.PASSWORD_ERROR.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.PASSWORD_ERROR.getMsg());
						return jsonResult;
					}
				}
				// 返回密码
				dataBasePassword = loginUser.getPassword();
				if (!"2".equals(loginUser.getIsActive())) {
					if (param.get("sendMessage") != null) {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_NOT_ACTIVED.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_NOT_ACTIVED.getMsg());
						jsonResult.put("password", dataBasePassword);
						return jsonResult;
					}
					// =========未激活状态下,自动激活============
					// 激活,生成随机明文密码,start
					String randomPassword = "";
					Random random = new Random();
					for (int i = 0; i < 6; i++) {
						randomPassword += String.valueOf(random.nextInt(9) + 1);
					}
					// 激活操作,保存加密密码
					String activeMd5Pwd = MD5Utils.encode(randomPassword);
					// 激活,生成随机明文密码end
					HashMap<String, Object> activeParam = new HashMap<String, Object>();
					activeParam.put("mobile", loginUser.getTelephone());
					activeParam.put("cardNumber", loginUser.getCardNumber());
					activeParam.put("password", activeMd5Pwd);
					// 激活B系统数据
					int updateCount = this.userActivationLogin(activeParam);
					// 激活A系统数据
					boolean updateStatus = lifeMemberService.memberActivationLogin(activeParam);
					logger.info("====【validateUserInfo】B系统更新updateCount：" + updateCount + ",A系统更新updateStatus："
							+ updateStatus);
					if (!updateStatus || updateCount == 0) {
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.FAIL.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.FAIL.getMsg());
						return jsonResult;
					} else {
						// 如果家属激活，给邀请人发消息推送
						if (loginUser.getType() == AdUser.TYPE_FAMILY) {
							logger.info("===开始给邀请人发送消息 开始");
							new Thread(new SendMsgJob(loginUser)).start();
							logger.info("===开始给邀请人发送消息 结束");
						}
						jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_NOT_ACTIVED.getCode());
						jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_NOT_ACTIVED.getMsg());
						jsonResult.put("password", dataBasePassword);
						jsonResult.put("randomPassword", randomPassword);
						return jsonResult;
					}
				}
			}
		} catch (Exception e) {
			logger.info("=== e = {}", e);
			e.printStackTrace();
			jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_NOT_ACTIVED.getCode());
			jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_NOT_ACTIVED.getMsg());
			throw new RuntimeException();
		}

		jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.SUCCESS.getCode());
		jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.SUCCESS.getMsg());
		jsonResult.put("password", dataBasePassword);
		return jsonResult;
	}

	/**
	 * 会员验证,有卡激活
	 *
	 */
	@Override
	@Transactional
	public JSONObject validateAndActive(JSONObject paramData) throws Exception {
		// 返回数据
		JSONObject resultData = new JSONObject();
		try {
			// 开始毫秒数
			Long startTime = System.currentTimeMillis();
			JSONObject validateResult = this.validateActiveCode(paramData);
			logger.info("====【validateAndActive】-【個人专属码】验证激活参数耗时：" + (System.currentTimeMillis() - startTime));

			if (ConstantsLogin.CodeActive.SUCCESS.getCode().equals(validateResult.getString(ConstantsLogin.CODE))) {
				resultData = this.doActive(validateResult);
				logger.info("====【validateAndActive】-【個人专属码】验证,激活总耗时：" + (System.currentTimeMillis() - startTime));
			} else {
				resultData.put(ConstantsLogin.CODE, validateResult.getString(ConstantsLogin.CODE));
				resultData.put(ConstantsLogin.MESS, validateResult.getString(ConstantsLogin.MESS));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.FAIL.getMsg());
			logger.info("====【validateAndActive】-系统异常-error：" + e.getMessage());
			throw new RuntimeException();
		}
		logger.info("====【validateAndActive】返回数据-resultData：" + resultData.toJSONString());
		return resultData;
	}

	/**
	 * 企业口令激活
	 *
	 */
	@Override
	@Transactional
	public MessageDataBean groupCommandActive(JSONObject paramData) throws Exception {
		// 返回数据
		MessageDataBean dataBean = new MessageDataBean();
		try {
			// 开始毫秒数
			Long startTime = System.currentTimeMillis();
			// 验证参数
			JSONObject validateResult = this.validateCommandActive(paramData);
			logger.info("====【groupCommandActive】-【企业口令】验证激活参数耗时：" + (System.currentTimeMillis() - startTime));

			if (validateResult != null
					&& ConstantsLogin.CommandActive.SUCCESS.getCode().equals(validateResult.get(ConstantsLogin.CODE))) {
				// 激活用户
				dataBean = this.execCommandActive(paramData);
				logger.info("====【groupCommandActive】-【企业口令】验证,激活总耗时：" + (System.currentTimeMillis() - startTime));
			} else {
				return new MessageDataBean(validateResult.get(ConstantsLogin.CODE).toString(),
						validateResult.get(ConstantsLogin.MSG).toString());
			}
			// 激活,存储数据
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("====【groupCommandActive】系统异常-error:" + e.getMessage() + "====");
			throw new RuntimeException();
		}
		logger.info("====【groupCommandActive】返回数据-dataBean：" + dataBean.toJsonString());
		return dataBean;
	}

	/**
	 * 企业口令激活-验证参数
	 */
	public JSONObject validateCommandActive(JSONObject paramJson) throws Exception {
		JSONObject resultJson = new JSONObject();

		try {
			// 验证口令与企业是否匹配
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("groupCommand", paramJson.get("groupCommand").toString());
			paramMap.put("groupId", paramJson.get("groupId").toString());
			logger.info("====【validateCommandActive】-验证口令与企业是否匹配,参入参数：" + paramMap.toString());
			List<AdGroup> groupList = adGroupDao.getGroupListByCommand(paramMap);
			if (groupList != null && groupList.size() > 0) {
				// 验证手机号是否存在
				AdUser paramAdUser = new AdUser();
				paramAdUser.setTelephone(paramJson.get("mobile").toString());
				AdUser adUser = adUserDao.findByTelephone(paramAdUser);
				if (adUser == null) {
					resultJson.put(ConstantsLogin.CODE, ConstantsLogin.CommandActive.SUCCESS.getCode());
					resultJson.put(ConstantsLogin.MSG, ConstantsLogin.CommandActive.SUCCESS.getMsg());
				} else {
					resultJson.put(ConstantsLogin.CODE, ConstantsLogin.CommandActive.MOBILE_EXIST.getCode());
					resultJson.put(ConstantsLogin.MSG, ConstantsLogin.CommandActive.MOBILE_EXIST.getMsg());
				}
			} else {
				resultJson.put(ConstantsLogin.CODE, ConstantsLogin.CommandActive.COMMAND_GROUP_MATCH_ERROR.getCode());
				resultJson.put(ConstantsLogin.MSG, ConstantsLogin.CommandActive.COMMAND_GROUP_MATCH_ERROR.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【validateCommandActive】-验证参数返回结果-resultJson：" + resultJson.toJSONString());

		return resultJson;
	}

	/**
	 * 用户自动激活
	 * @param adUser
	 * @return Is
	 */
	public JSONObject userAutoActive(AdUser adUser) {
		JSONObject jsonResult = new JSONObject();
		try {
			// 激活,生成随机明文密码,start
			String randomPassword = "";
			Random random = new Random();
			for (int i = 0; i < 6; i++) {
				randomPassword += String.valueOf(random.nextInt(9) + 1);
			}
			// 激活操作,保存加密密码
			String activeMd5Pwd = MD5Utils.encode(randomPassword);
			// 激活,生成随机明文密码end
			HashMap<String, Object> activeParam = new HashMap<String, Object>();
			activeParam.put("mobile", adUser.getTelephone());
			activeParam.put("cardNumber", adUser.getCardNumber());
			activeParam.put("password", activeMd5Pwd);
			// 激活B系统数据
			int updateCount = this.userActivationLogin(activeParam);
			// 激活A系统数据
			boolean updateStatus = lifeMemberService.memberActivationLogin(activeParam);
			logger.info("====【validateUserInfo】B系统更新updateCount：" + updateCount + ",A系统更新updateStatus："
					+ updateStatus);
			if (!updateStatus || updateCount == 0) {
				jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.FAIL.getCode());
				jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.FAIL.getMsg());
				return jsonResult;
			} else {
				jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.SUCCESS.getCode());
				jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.SUCCESS.getMsg());
				return jsonResult;
			}
		} catch (Exception e) {
			logger.info("=== e = {}", e);
			e.printStackTrace();
			jsonResult.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_NOT_ACTIVED.getCode());
			jsonResult.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_NOT_ACTIVED.getMsg());
			return jsonResult;
		}
	}

	@Autowired
	private UserServiceI userServiceI;

	/**
	 * 企业口令激活-执行激活
	 */
	public MessageDataBean execCommandActive(JSONObject paramJson) throws Exception {
		MessageDataBean dataBean = new MessageDataBean();

		try {
			// 新增B库ad_user表用户数据
			AdUser adUser = this.saveUserAndPersonal(paramJson);
			LifeMember lifeMember = new LifeMember();
			if (adUser != null) {
				// 新增A库xx_member表用户数据
				lifeMember = this.saveMember(adUser);

                //更新类型为企业口令激活
                //数据来源 0:平台导入(白名单) 2：企业口令激活，3：卡激活，4：专属码
                logger.info("更新类型为企业口令激活:{}",adUser.getTelephone());
                userServiceI.updatePersonInfoDataSources(adUser.getTelephone(),2);

			}
			// 返回数据
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("password", adUser.getPassword());
			dataMap.put("memberId", lifeMember.getId());
			dataMap.put("mobile", lifeMember.getMobile());
			dataBean = new MessageDataBean(ConstantsLogin.CommandActive.SUCCESS.getCode(),
					ConstantsLogin.CommandActive.SUCCESS.getMsg());
			dataBean.setData(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【execCommandActive】-验证参数返回结果-dataBean：" + dataBean.toJsonString());

		return dataBean;
	}

	/**
	 * 存储B库ad_user表,ad_user_personal_info数据
	 */
	public AdUser saveUserAndPersonal(JSONObject jsonParam) throws Exception {
		// 用户信息
		AdUser adUserParam = new AdUser();
		String telephone = jsonParam.get("mobile").toString();
		AdUser userInfo = adUserDao.findByMobile(telephone);
		Long newGroupId = jsonParam.getLong("groupId");
		adUserParam.setGroupNum(newGroupId);
		if (userInfo == null) {
			// 生成密码
			String password = RandomStringUtils.randomNumeric(6);
			String md5Pwd = MD5Utils.encode(password);
			// 新增用户参数
			adUserParam.setTelephone(telephone);
			adUserParam.setName(String.valueOf(jsonParam.get("name")));
			adUserParam.setPassword(md5Pwd);
//			adUserParam.setPayPassword(md5Pwd);
			adUserParam.setDataSyn(AdUser.DATA_SYN_ON);
			adUserParam.setRemarks(String.valueOf(jsonParam.get("remarks")));
			String isActive = jsonParam.getString("isActive");
			if (StringUtils.isEmpty(isActive)) {
				adUserParam.setIsActive(AdUser.USER_ACTIVATION_ON);
				adUserParam.setActiveDate(new Date());
			} else {
				adUserParam.setIsActive(isActive);
			}
			adUserParam.setCreateBy("0");
			adUserParam.setCreateDate(new Date());
			adUserParam.setUpdateBy("0");
			adUserParam.setUpdateDate(adUserParam.getCreateDate());
			adUserParam.setGroupNum(jsonParam.getLong("groupId"));
			// 设置为5秒超时
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				try {
					String groupMaxCardNumberKey = "group_max_cardNumber:" + adUserParam.getGroupNum();
					String groupCardNumber = stringRedis.opsForValue().get(groupMaxCardNumberKey);
					if (StringUtils.isNotEmpty(groupCardNumber)) {
						groupCardNumber = String.valueOf(stringRedis.opsForValue().increment(groupMaxCardNumberKey, 1));
					} else {
						// 生成卡号
						groupCardNumber = adUserDao.createCardNumber(String.valueOf(adUserParam.getGroupNum()));
						// 设置有效期一天
						stringRedis.opsForValue().set(groupMaxCardNumberKey, groupCardNumber, 1, TimeUnit.DAYS);
					}
					adUserParam.setCardNumber(groupCardNumber);
				} catch (Exception e) {
					logger.error("保存用户错误", e);
					throw e;
				} finally {
					lock.unlock();
				}
			}
			// 执行插入
			adUserDao.saveUser(adUserParam);
			// 回传明文密码,发短信
			adUserParam.setPassword(password);

			// 异步保存用户工号等信息
			new Thread(new Runnable() {
				@Override
				public void run() {
					AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
					adUserPersonalInfo.setId(adUserParam.getId());
					// 数据来源
					Integer dataSource = jsonParam.getInteger("dataSource");
					if (dataSource != null) {
						adUserPersonalInfo.setDataSources(dataSource);
					} else {
						adUserPersonalInfo.setDataSources(2);
						adUserPersonalInfo.setAuthFlag("0");
					}
					// 认证标识
					if (!StringUtils.isEmpty(jsonParam.getString("workerNumber"))) {
						adUserPersonalInfo.setWorkNumber(jsonParam.get("workerNumber").toString());
					}
					adUserPersonalInfo.setIsSetPassword(0);
					adUserPersonalInfoDao.insert(adUserPersonalInfo);

				}
			}).start();
		} else {
			// 如果用户已存在则更新企业ID
			adUserParam.setId(userInfo.getId());
			if (userInfo.getGroupNum().longValue() != newGroupId.longValue()) {
				adUserDao.updateByPrimaryKeySelective(adUserParam);
				logger.info("该用户已存在，更新用户所在企业，telephone={}", telephone);
			}
			// 设置激活状态
			adUserParam.setIsActive(userInfo.getIsActive());
			adUserParam.setCardNumber(userInfo.getCardNumber());
			adUserParam.setOldGroupNum(userInfo.getGroupNum());
		}

		return adUserParam;
	}

	/**
	 * 存储A库xx_member表数据
	 */
	public LifeMember saveMember(AdUser adUser) throws Exception {
		LifeMember lifeMemberParam = new LifeMember();

		try {
			lifeMemberParam.setAdId(adUser.getId().toString());
			lifeMemberParam.setMobile(adUser.getTelephone());
			lifeMemberParam.setUsername(adUser.getCardNumber());
			lifeMemberParam.setName(adUser.getName());
			lifeMemberParam.setMemberType(0);
			lifeMemberParam.setIsEnabled(2);
			lifeMemberParam.setLoginFailureCount(0);
			lifeMemberParam.setDeleteFlg("0");
			lifeMemberParam.setCreateUser("0");
			lifeMemberParam.setCreateDate(new Date());
			lifeMemberParam.setModifyUser("0");
			lifeMemberParam.setModifyDate(new Date());
			// 查询对应A系统企业
			LifeGroup lifeGroup = new LifeGroup();
			lifeGroup.setAdId(adUser.getGroupNum().toString());
			lifeGroup.setDelFlg("0");
			lifeGroup = lifeGroupDao.getGroupByGroupId(lifeGroup);
			if (lifeGroup != null) {
				lifeMemberParam.setGroupId(Long.valueOf(lifeGroup.getId()));
			} else {
				logger.info("====【saveMember】A库未查询到与B库对应的企业====");
				throw new RuntimeException();
			}

			int memberCount = lifeMemberDao.saveMember(lifeMemberParam);
			if (memberCount <= 0) {
				logger.info("====【saveMember】插入member失败=====");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return lifeMemberParam;
	}

	/**
	 * 激活码验证会员信息
	 */
	public JSONObject validateActiveCode(JSONObject paramData) throws Exception {
		// 返回数据
		JSONObject resultData = new JSONObject();
		try {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());
			String vActivate = paramData.getString("vActivate");
			String mobile = paramData.getString("mobile");
			String workNumber = paramData.getString("workNumber");

			// 获取激活码用户信息
			AdActiveCode adActiveCode = adActiveCodeDao.findCodeStatusByCode(vActivate);
			if (adActiveCode != null) {
				if (adActiveCode.getAdUser() == null || adActiveCode.getAdUser().getAdGroup() == null) {
					logger.info("====【validateActiveCode】用户信息:" + adActiveCode.getAdUser() + "为空,或用户对应企业信息:"
							+ adActiveCode.getAdUser().getAdGroup() + "为空=====");
					// 根据姓名和工号没有匹配到会员
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.USER_NOT_EXIST.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.USER_NOT_EXIST.getMsg());
					resultData.put("memberId", null);
					return resultData;
				}
				logger.info("====【validateActiveCode】根据激活码获取用户信息-是否使用isUsed：" + adActiveCode.getIsUsed()
						+ ",是否激活isActive:" + adActiveCode.getAdUser().getIsActive() + ",企业简称groupShortName:"
						+ adActiveCode.getAdUser().getAdGroup().getGroupShortName() + ",员工姓名name:"
						+ adActiveCode.getAdUser().getName() + ",企业主键groupNum:" + adActiveCode.getAdUser().getGroupNum()
						+ ",激活码主键adActiveCodeId:" + adActiveCode.getId());
				String isUsed = adActiveCode.getIsUsed();
				String isActive = adActiveCode.getAdUser().getIsActive();
				// 激活码使用状态
				if (("0").equals(isUsed)) {
					// 激活码对应用户激活状态
					if ("2".equals(isActive)) {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.IS_ACTIVED.getCode());
						resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.IS_ACTIVED.getMsg());
					} else {
						resultData.put("groupShortName", adActiveCode.getAdUser().getAdGroup().getGroupShortName());
						resultData.put("telephone", mobile);
						resultData.put("name", adActiveCode.getAdUser().getName());
						resultData.put("workNumber", workNumber);
						resultData.put("groupNum", adActiveCode.getAdUser().getGroupNum());
						resultData.put("adActiveCodeId", adActiveCode.getId());
					}
				} else if (isUsed.equals("1")) {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.IS_USED.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.IS_USED.getMsg());
				} else {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getMsg());
				}
			} else {
				resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_NOT_EXIST.getCode());
				resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.CODE_NOT_EXIST.getMsg());
				logger.info("====【validateActiveCode】激活码信息-AdActiveCode：" + adActiveCode);
			}
		} catch (Exception e) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.FAIL.getMsg());
			e.printStackTrace();
			logger.info("====【validateActiveCode】service系统错误========");
		}
		logger.info("====【validateActiveCode】返回数据-resultData：" + resultData.toJSONString());
		return resultData;
	}

	/**
	 * 有卡激活-执行激活（专属码）
	 */
	public JSONObject doActive(JSONObject data) throws Exception {
		logger.info("====【doActive】-传入参数：" + data.toJSONString());
		JSONObject result = new JSONObject();
		try {
			String password = "";
			Random random = new Random();
			for (int i = 0; i < 6; i++) {
				password += String.valueOf(random.nextInt(9) + 1);
			}
			String md5Pwd = MD5Utils.encode(password);
			// 激活码验证通过后得到参数
			String groupShortName = data.getString("groupShortName");
			groupShortName = URLDecoder.decode(groupShortName, "utf-8");
			String telephone = data.getString("telephone");
			String name = data.getString("name");
			name = URLDecoder.decode(name, "utf-8");
			String workNumber = data.getString("workNumber");
			String groupNum = data.getString("groupNum");
			String adActiveCodeId = data.getString("adActiveCodeId");
			// 手机号/工号获取会员信息
			AdUser adUser = new AdUser();
			adUser.setTelephone(telephone);
			AdGroup adGroup = new AdGroup();
			adGroup.setId(Long.valueOf(groupNum));
			adUser.setAdGroup(adGroup);
			AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
			adUserPersonalInfo.setWorkNumber(workNumber);
			adUser.setPersonalInfo(adUserPersonalInfo);
			AdActiveCode adActiveCode = new AdActiveCode();
			adActiveCode.setId(Long.valueOf(adActiveCodeId));
			adUser.setAdActiveCode(adActiveCode);
			// 统一查询方法
			// AdUser adUser = adUserDao.getUserByTelWorkNum(adUser);
			AdUser telUser = adUserDao.findByTelephone(adUser);
			AdUser workNumUser = adUserDao.findUserByNameAndWorkNumber(adUser);
			if (null == workNumUser) {
				// 根据姓名和工号没有匹配到会员
				result.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.WORKERNUM_CODE_NOT_EXIST.getCode());
				result.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.WORKERNUM_CODE_NOT_EXIST.getMsg());
				result.put("memberId", null);
				return result;
			}
			if (telUser == null) {
				// 激活A系统
				String username = workNumUser.getCardNumber();
				LifeMember lifeMember = lifeMemberDao.findMemberByUsername(username);
				lifeMember.setMobile(telephone);
				lifeMember.setIsEnabled(2);
				lifeMember.setLoginFailureCount(0);
				lifeMember.setModifyDate(new Date());
				lifeMember.setPassword(md5Pwd);
				lifeMemberDao.updateActiveStatus(lifeMember);

				// 激活B系统
				if (null != workNumUser.getPersonalInfo()) {
					AdUserPersonalInfo personalInfo = workNumUser.getPersonalInfo();
					personalInfo.setWorkNumber(workNumber);
					workNumUser.setPersonalInfo(personalInfo);
				} else {
					AdUserPersonalInfo adUserPersonalInfoNew = new AdUserPersonalInfo();
					adUserPersonalInfoNew.setWorkNumber(workNumber);
					adUserPersonalInfoNew.setId(workNumUser.getId());
					adUserDao.savePersonalInfo(adUserPersonalInfoNew);
				}
				workNumUser.setTelephone(telephone);
				workNumUser.setIsActive("2");
				workNumUser.setDataSyn(AdUser.DATA_SYN_ON);// 设置第三方数据同步为可以同步
				Date now = new Date();
				workNumUser.setActiveDate(now);
				workNumUser.setUpdateDate(now);
				workNumUser.setPassword(md5Pwd);
				adUserDao.updateUserPersonal(workNumUser);
				adActiveCodeDao.updateCodeIsUsed(adActiveCodeId);
				result.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());// 激活成功并更新会员信息
				result.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());
				result.put("memberId", lifeMember.getId());
				result.put("password", password);
			} else {
				// 根据手机号查找到会员(宝化工员工通过提货券通道进来的)
				// 验证姓名和工号
				// AdUser user =
				// adUserService.findUserByNameAndWorkNumber(groupNum,
				// name, workNumber,adActiveCodeId);
				if (null != telUser.getPersonalInfo()) {
					AdUserPersonalInfo personalInfo = telUser.getPersonalInfo();
					personalInfo.setWorkNumber(workNumber);
					telUser.setPersonalInfo(personalInfo);
				} else {
					AdUserPersonalInfo adUserPersonalInfoNew = new AdUserPersonalInfo();
					adUserPersonalInfoNew.setWorkNumber(workNumber);
					adUserPersonalInfoNew.setId(adUser.getId());
					adUserDao.savePersonalInfo(adUserPersonalInfoNew);
				}
				if (telUser.getId().longValue() == workNumUser.getId().longValue()) {
					telUser.setIsActive("2");
					telUser.setDataSyn(AdUser.DATA_SYN_ON);// 设置第三方数据同步为可以同步
					Date now = new Date();
					telUser.setActiveDate(now);
					telUser.setUpdateDate(now);
					telUser.setPassword(md5Pwd);
					adUserDao.updateUserPersonal(telUser);
					adActiveCodeDao.updateCodeIsUsed(adActiveCodeId);

					LifeMember lifeMember = lifeMemberDao.findMemberByMobile(telephone);
					lifeMember.setIsEnabled(2);
					lifeMember.setLoginFailureCount(0);
					lifeMember.setModifyDate(new Date());
					lifeMember.setPassword(md5Pwd);
					lifeMember.setAdId(String.valueOf(telUser.getId()));
					lifeMemberDao.updateActiveStatus(lifeMember);

					result.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());// 激活成功并更新会员信息
					result.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());
					result.put("memberId", lifeMember.getId());
					result.put("password", password);

				} else {
					telUser.setGroupNum(workNumUser.getGroupNum());
					telUser.setName(name);
					telUser.setIsActive("2");
					telUser.setDataSyn(AdUser.DATA_SYN_ON);// 设置第三方数据同步为可以同步
					Date now = new Date();
					telUser.setActiveDate(now);
					telUser.setUpdateDate(now);
					telUser.setPassword(md5Pwd);
					adUserDao.updateUserPersonal(telUser);
					adActiveCodeDao.updateUserId(String.valueOf(workNumUser.getId()), String.valueOf(telUser.getId()));
					adActiveCodeDao.updateCodeIsUsed(adActiveCodeId);
					adUserDao.deleteAdUser(workNumUser);// 此处暂时是逻辑删除

					LifeMember lifeMember = lifeMemberDao.findMemberByMobile(telephone);
					LifeMember bean = new LifeMember();
					bean.setAdId(String.valueOf(workNumUser.getId()));
					LifeMember member = lifeMemberDao.findLifeMemberByAdId(bean);

					lifeMember.setGroupId(member.getGroupId());
					lifeMember.setName(name);
					lifeMember.setIsEnabled(2);
					lifeMember.setLoginFailureCount(0);
					lifeMember.setModifyDate(new Date());
					lifeMember.setPassword(md5Pwd);
					lifeMember.setAdId(String.valueOf(telUser.getId()));
					lifeMemberDao.updateActiveStatus(lifeMember);
					lifeMemberDao.deleteMember(member);// 此处暂时是逻辑删除

					result.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());// 激活成功并更新会员信息
					result.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());
					result.put("memberId", lifeMember.getId());
					result.put("password", password);
				}
			}

            //更新类型为专属码激活
            //数据来源 0:平台导入(白名单) 2：企业口令激活，3：卡激活，4：专属码
            logger.info("更新类型为专属码激活:{}",telephone);
            userServiceI.updatePersonInfoDataSources(telephone,4);

		} catch (Exception e) {
			e.printStackTrace();
			result.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			result.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.FAIL.getMsg());
			result.put("memberId", null);
			result.put("password", null);
			logger.info("====【doActive】系统错误========" + e.getMessage());
			throw new RuntimeException();
		}
		logger.info("====【doActive】返回数据-result：" + result.toJSONString());
		return result;
	}

	/**
	 * app家属邀请验证手机是否可以添加
	 *
	 * @param data
	 * @return
	 */
	public JSONObject checkTelephone(JSONObject data) {
		JSONObject res = new JSONObject();
		String telephone = data.getString("telephone");
		String userId = data.getString("userId");
		String channel = data.getString("channel");
		try {
			AdUser user = adUserDao.findByMobile(telephone);
			if (user == null) {
				res.put("code", "1000");
				return res;
			}
			if (user.getType() == 0) {
				// 改手机已经是会员
				res.put("code", "1001");
				res.put("msg", "该手机已是会员");
				return res;
			} else if (user.getType() == 1) {
				// 查询该家属去记录表中查询是否存在
				AdInvitationRecord adInvitationRecord = adInvitationRecordDao
						.findRecodByInviteeId(user.getId().toString());
				if (adInvitationRecord != null) {
					if (!userId.equals(adInvitationRecord.getInviterId() + "")) {
						res.put("code", "1007");
						res.put("msg", "该手机已经不是您的家属，不能邀请！");
						return res;
					} else if (userId.equals(adInvitationRecord.getInviterId() + "")) {
						if (user.getDelFlag().equals("1")) {
							// 修改用户状态
							AdUser record = new AdUser();
							record.setId(user.getId());
							record.setDelFlag("0");
							int u1 = adUserDao.updateByPrimaryKeySelective(record);
							int u2 = lifeMemberDao.updateFlgByAdId(userId, "0");
							// 减少邀请机会
							int u3 = adInvitationRecordDao.reduceInvitationAvail(userId);
							// 修改邀请记录
							int u4 = adInvitationRecordDao.updateDateById(adInvitationRecord.getId());
							logger.info(
									"updateByPrimaryKeySelective={}  updateFlgByAdId={}  reduceInvitationAvail={} updateDateById={}",
									u1, u2, u3, u4);

							res.put("code", "1004");
							res.put("msg", "该手机号再次被邀请！");
							return res;
						}
						if (user.getIsActive().equals("2")) {
							res.put("code", "1011");
							res.put("msg", "该手机已经是您的家属！");
							return res;
						}
						if (user.getIsActive().equals("1")) {
							res.put("code", "1002");
							res.put("msg", "该手机已经是您的家属，但未激活！");
							return res;
						}
					} else {
						res.put("code", "1008");
						res.put("msg", "该手机已经是您的家属！");
						return res;
					}
				} else {
					// 如果邀请记录表为空，查询他的推荐人,推荐人不为空则新增一条邀请记录表
					if (user.getSourceCardNumber() != null) {
						AdUser adUser = adUserDao.findByCardNumber(user.getSourceCardNumber());
						AdInvitationRecord adInvitationRecord1 = new AdInvitationRecord();
						adInvitationRecord1.setCode(null);
						adInvitationRecord1.setInviteeId(user.getId().intValue());
						adInvitationRecord1.setInviterId(adUser.getId().intValue());
						adInvitationRecord1.setCreateDate(new Date());// 创建时间
						adInvitationRecord1.setUpdateDate(new Date());// 更新时间
						// adInvitationRecord1.setIsNewRecord(true);
						adInvitationRecord1.setFlag("2");
						adInvitationRecord1.setChannel(channel);
						adInvitationRecordDao.insert(adInvitationRecord1);
						// 更新邀请记录表信息
						// adInvitationDao.updateByUserId(adUser.getId().intValue());
						res.put("code", "1003");
						res.put("msg", "该手机添加家属成功");
						return res;
					}
				}
			}

		} catch (Exception e) {
			res.put("code", "4000");
			res.put("msg", "服务器异常");
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 同步添加，修改账户信息
	 * @return
	 * @throws Exception
	 */
	// @Transactional(rollbackFor = Exception.class)
	@Override
	public boolean syncUserASystem(AdUser adUser) {
		try {
			LifeGroup lifegroup = lifeGroupService.getGroupByGroupId(String.valueOf(adUser.getGroupNum()));
			if (lifegroup != null) {
				AdUser userdata = this.findByCardNumber(adUser.getCardNumber());
				LifeMember lifemember = new LifeMember();
				// if (StringUtils.isEmpty(lifemember.getId().toString())) {
				lifemember.setIsNewRecord(true);
				lifemember.setId(0L);
				// }
				lifemember.setUsername(userdata.getCardNumber());
				lifemember.setMobile(userdata.getTelephone()); // TODO
				lifemember.setName(userdata.getName());
				lifemember.setEmail(userdata.getMailbox());
				if (org.apache.commons.lang3.StringUtils.isNotBlank(userdata.getSex())) {
					lifemember.setGender(Integer.valueOf(userdata.getSex()));
				}
				lifemember.setIdentityCard(userdata.getIdentityCard());

				lifemember.setIsEnabled(Integer.valueOf(userdata.getIsActive()));
				lifemember.setIsLocked(false);
				lifemember.setLoginFailureCount(0);
				lifemember.setGroupId(Long.valueOf(lifegroup.getId())); // 获取groupId
				lifemember.setMemberType(userdata.getType().intValue());
				lifemember.setCreateDate(userdata.getCreateDate());
				lifemember.setModifyDate(userdata.getUpdateDate());
				lifemember.setDeleteFlg(userdata.getDelFlag());
				lifemember.setAdId(userdata.getId() + "");
				lifemember.setSourceCardNumber(userdata.getSourceCardNumber());
				lifeMemberService.insert(lifemember);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据会员卡号获取会员信息
	 *
	 * @param cardNumber
	 * @return
	 */
	public AdUser findByCardNumber(String cardNumber) {
		return adUserDao.findByCardNumber(cardNumber);
	}

	public List<String> getIds(List<String> userIds) {
		return adUserDao.getIds(userIds);
	}

	public void addIntegral(Long userId, BigDecimal integralForEach) {
		// TODO Auto-generated method stub
		adUserDao.addIntegral(userId, integralForEach);
	}

	@Autowired
	private UserService userService;

	/**
	 * 验证企业口令是否存在,专属码是否可用,激活码是否可用并激活
	 *
	 */
	@Override
	@Transactional
	public JSONObject verifyCodeAndActivation(JSONObject paramData) throws Exception {
		// 返回数据
		JSONObject resultData = new JSONObject();
		boolean isFailed = true;
		String code = paramData.getString("code");
		String telephone = paramData.getString("mobile");
		String staffNum = paramData.getString("staffNum");
		String email = paramData.getString("email");
		String groupId = paramData.getString("groupId");
		try {
			Long startTime = System.currentTimeMillis();
			if (code.length() == 6) {
				if (StringUtils.isNotBlank(staffNum)) {
					//福特激活处理
					if (StringUtils.isBlank(telephone)) {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getCode());
						resultData.put(ConstantsLogin.MSG, "手机号为空");
					} else if (StringUtils.isBlank(staffNum)) {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getCode());
						resultData.put(ConstantsLogin.MSG, "工号为空");
					} else if (StringUtils.isBlank(email)) {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getCode());
						resultData.put(ConstantsLogin.MSG, "邮箱为空");
					} else if (StringUtils.isBlank(groupId)) {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getCode());
						resultData.put(ConstantsLogin.MSG, "用户单位为空");
					} else {
						resultData = adActiveCodeServiceI.validateFordUser(code,telephone,staffNum,email,groupId);
						if (resultData != null && ConstantsLogin.CodeActive.SUCCESS.getCode().equals(resultData.getString("code"))) {
							isFailed = false;
							try {
								resultData.put(Constants.CHANNEL,Constants.CHANNEL_H5);
								resultData = userService.userLogin(resultData);
							} catch (Exception e) {
								logger.info("verifyCodeAndActivation登录异常：",e);
							}
						}
					}
				} else {
					// 个人专属码
					JSONObject validateResult = this.validateCode(code);
					if (ConstantsLogin.CodeActive.SUCCESS.getCode().equals(validateResult.getString(ConstantsLogin.CODE))) {
						// resultData = this.doActive(validateResult);
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
						resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());
						isFailed = false;
						logger.info("====【verifyCodeAndActivation】-【個人专属码】验证,激活总耗时："
								+ (System.currentTimeMillis() - startTime));
					} else {
						resultData.put(ConstantsLogin.CODE, validateResult.getString(ConstantsLogin.CODE));
						resultData.put(ConstantsLogin.MESS, validateResult.getString(ConstantsLogin.MESS));
						logger.info("====【verifyCodeAndActivation】-【個人专属码】验证,券码不存在,激活总耗时："
								+ (System.currentTimeMillis() - startTime));
					}
				}
			} else if (code.length() == 8) {
				// 企业口令
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("groupCommand", code);
				paramMap.put("groupId", null);
				List<AdGroup> groupList = adGroupDao.getGroupListByCommand(paramMap);
				if (groupList != null && groupList.size() > 0) {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());
					isFailed = false;
					logger.info("====【verifyCodeAndActivation】-验证企业口令成功,参入参数：" + code);
				} else {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CommandActive.GROUP_NO_DATA.getCode());
					resultData.put(ConstantsLogin.MSG, ConstantsLogin.CommandActive.GROUP_NO_DATA.getMsg());
					logger.info("====【verifyCodeAndActivation】-验证企业口令不成功,参入参数：" + code);
				}
				logger.info("====【verifyCodeAndActivation】-【企业口令】验证,激活总耗时：" + (System.currentTimeMillis() - startTime));
			} else if (code.length() == 12) {
				// 激活码,验证并激活
				// 判断激活码是否被使用
				VoucherCardRecord voucherCardRecord = voucherCardRecordDao.findByActivationCode(code);
				if (voucherCardRecord != null) {
					Date now = new Date();
					if (voucherCardRecord.getCardActivationStatus() == 0
							|| voucherCardRecord.getApplicationStatus() != 1
							|| now.getTime() < voucherCardRecord.getBeginTime().getTime()) {
						resultData.put(ConstantsLogin.CODE, ConstantsV2.IntegralCode.NOT_ACTIVATE.getCode());
						resultData.put(ConstantsLogin.MSG, ConstantsV2.IntegralCode.NOT_ACTIVATE.getMsg());
						logger.info("====【verifyCodeAndActivation】返回数据-resultData：" + resultData.toJSONString());
						return resultData;
					}
					if (voucherCardRecord.getCardActivationStatus() == 2) {
						resultData.put(ConstantsLogin.CODE, ConstantsV2.IntegralCode.IS_FREEZE.getCode());
						resultData.put(ConstantsLogin.MSG, ConstantsV2.IntegralCode.IS_FREEZE.getMsg());
						logger.info("====【verifyCodeAndActivation】返回数据-resultData：" + resultData.toJSONString());
						return resultData;
					}
					if (now.getTime() > voucherCardRecord.getEndTime().getTime()) {
						resultData.put(ConstantsLogin.CODE, ConstantsV2.IntegralCode.WRONG_TIME.getCode());
						resultData.put(ConstantsLogin.MSG, ConstantsV2.IntegralCode.WRONG_TIME.getMsg());
						logger.info("====【verifyCodeAndActivation】返回数据-resultData：" + resultData.toJSONString());
						return resultData;
					}
					if (voucherCardRecord.getActivationCodeUseStatus() != 0
							|| voucherCardRecord.getCardUseStatus() != 0) {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.IS_USED.getCode());
						resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.IS_USED.getMsg());
						logger.info("====【verifyCodeAndActivation】返回数据-resultData：" + resultData.toJSONString());
						return resultData;
					}
					// 进行激活操作
					AdGroup group = adGroupDao.findGroupByID(voucherCardRecord.getGroupId());
					AdUser user = adUserDao.findByMobile(telephone);
					String password = "";
					Random random = new Random();
					for (int i = 0; i < 6; i++) {
						password += String.valueOf(random.nextInt(9) + 1);
					}
					String md5Pwd = MD5Utils.encode(password);
					if (user != null) {
						// 存在手机号(A库是否存在手机号)
						// 将b库数据还原正常,del_flag=0,is_active=2
						user.setGroupNum(Long.valueOf(group.getId()));
						user.setPassword(md5Pwd);
						user.setPayPassword(md5Pwd);
						adUserDao.updateActiveAndDelFlagById(user);

						dealLifeMemberDataForActive(user);
						voucherCardRecord.setActivationCodeUseUid(user.getId() + "");
					} else {
						// 不存在手机号(A库是否存在手机号)
						AdUser u = newAdUser(telephone, group, md5Pwd);
						adUserDao.insert(u);
						// 插入个人扩展属性
						AdUser adUser = adUserDao.findByMobile(telephone);
						AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
						adUserPersonalInfo.setId(adUser.getId());
						adUserPersonalInfo.setIsSetPassword(0);
						adUserPersonalInfoDao.insert(adUserPersonalInfo);
						// 插入数据
						AdUser findByMobile = adUserDao.findByMobile(telephone);
						dealLifeMemberDataForActive(u);
						voucherCardRecord.setActivationCodeUseUid(findByMobile.getId() + "");
					}
					LifeMember memberdata = lifeMemberDao.findMemberByTelephone(telephone);
					resultData.put("memberId", memberdata.getId());
					resultData.put("password", password);
					// 修改激活码状态activation_code_use_status activation_code_use_uid
					// activation_code_use_time
					voucherCardRecord.setActivationCodeUseStatus(1);
					voucherCardRecord.setActivationCodeUseTime(new Date());
					voucherCardRecord.setCardUseTime(new Date());
					voucherCardRecordDao.updateActiveData(voucherCardRecord);


					//更新类型为卡激活
                    //数据来源 0:平台导入(白名单) 2：企业口令激活，3：卡激活，4：专属码
                    logger.info("更新类型为卡激活:{}",telephone);
                    userServiceI.updatePersonInfoDataSources(telephone,3);



					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());
					isFailed = false;
					logger.info("====【verifyCodeAndActivation】-激活码激活成功,参入参数：" + code);
				} else {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_NOT_EXIST.getCode());
					resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.CODE_NOT_EXIST.getMsg());
				}
				logger.info("====【verifyCodeAndActivation】-【激活码】验证,激活总耗时：" + (System.currentTimeMillis() - startTime));
			} else {
				resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getCode());
				resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getMsg());
				logger.info("====【verifyCodeAndActivation】-参数位数不合理,参数为：" + code);
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.FAIL.getMsg());
			logger.info("====【verifyCodeAndActivation】-系统异常-error：" + e.getMessage());
			throw new RuntimeException();
		}
		String old = redisTemplate.opsForValue().get(ACTIVATE_CODE_FAIL_COUNT + telephone);
		if (isFailed) {
			if (org.apache.commons.lang3.StringUtils.isBlank(old)) {
				redisTemplate.opsForValue().set(ACTIVATE_CODE_FAIL_COUNT + telephone, "1", 5 * 60 * 1000,
						TimeUnit.MILLISECONDS);
				resultData.put("failCount", 1);
			} else {
				if (Integer.valueOf(old) > 4) {
					redisTemplate.delete(ACTIVATE_CODE_FAIL_COUNT + telephone);
					resultData.put("failCount", 1);
				} else {
					redisTemplate.boundValueOps(ACTIVATE_CODE_FAIL_COUNT + telephone).increment(1);
					resultData.put("failCount", Integer.valueOf(old) + 1);
				}
			}
		} else {
			// String old =
			// redisTemplate.opsForValue().get(ACTIVATE_CODE_FAIL_COUNT+telephone);
			if (org.apache.commons.lang3.StringUtils.isBlank(old)) {
				resultData.put("failCount", 0);
			} else {
				resultData.put("failCount", Integer.valueOf(old));
			}
		}
		logger.info("====【verifyCodeAndActivation】返回数据-resultData：" + resultData.toJSONString());
		return resultData;
	}

	/*  @Override
      @Cacheable(module = "ADUSERSERVICE", event = "GETUSER", key = "id",
              expires = RedisConstants.REDIS_USER_CACHE_EXPIRATION_DATE, required = true)*/
	public AdUser getUser(AdUser adUser) {
		return adUserDao.getUser(adUser);
	}

	private AdUser newAdUser(String telephone, AdGroup group, String md5Pwd) {
		AdUser u = new AdUser();
		u.setTelephone(telephone);
		u.setGroupNum(Long.valueOf(group.getId()));
		u.setName(telephone);
		String countUser = adUserDao.find12NumberCountByGroupId(group.getId() + "", group.getGroupNum());
		if (!countUser.equals("0000000") && countUser.length() > 7) {
			countUser = countUser.substring(countUser.length() - 7);
		}
		countUser = (Integer.valueOf(countUser) + 1) + "";
		if (countUser.length() < 7) {
			int length = countUser.length();
			String wPrefix = "";
			for (int j = 0; j < 7 - length; j++) {
				wPrefix += "0";
			}
			countUser = wPrefix + countUser;
		}
		u.setCardNumber(group.getGroupNum() + countUser);
		u.setPassword(md5Pwd);
		u.setPayPassword(md5Pwd);
		u.setIsPayPassword("1");
		Date nowDate = new Date();
		u.setDataSyn(AdUser.DATA_SYN_ON);
		u.setCreateBy("0");
		u.setUpdateBy("0");
		u.setCreateDate(nowDate);
		u.setUpdateDate(nowDate);
		u.setActiveDate(nowDate);
		// user.setCardRange(2);
		u.setIsActive(AdUser.USER_ACTIVATION_ON);
		u.setDelFlag(0 + "");
		u.setIntegral(new BigDecimal(0));
		u.setLineCredit(new BigDecimal(0));
		u.setType((short) 0);
		u.setSourceCardNumber("0");
		u.setState(0);
		return u;
	}

	private void dealLifeMemberDataForActive(AdUser user) {
		LifeMember member = lifeMemberDao.findMemberByTelephone(user.getTelephone());
		if (member == null) {
			// 执行A库数据插入
			this.syncUserASystem(user);
		} else {
			// 将a库数据还原正常,del_flag=0,is_enable=2
			AdUser userdata = this.findByCardNumber(user.getCardNumber());
			user.setId(userdata.getId());
			LifeGroup lifegroup = lifeGroupService.getGroupByGroupId(user.getGroupNum() + "");
			member.setAdId(userdata.getId() + "");
			member.setName(userdata.getName());
			if (org.apache.commons.lang3.StringUtils.isNotBlank(userdata.getSex())) {
				member.setGender(Integer.valueOf(userdata.getSex()));
			}
			member.setIdentityCard(userdata.getIdentityCard());
			member.setIsEnabled(Integer.parseInt(userdata.getIsActive()));
			member.setIsLocked(false);
			member.setGroupId(Long.valueOf(lifegroup.getId())); // 获取groupId
			member.setMemberType(userdata.getType().intValue());
			member.setCreateDate(userdata.getCreateDate());
			member.setModifyDate(userdata.getUpdateDate());
			member.setDeleteFlg(userdata.getDelFlag());
			member.setAdId(userdata.getId() + "");
			member.setSourceCardNumber(userdata.getSourceCardNumber());
			lifeMemberDao.updateActiveAndDelFlagById(member);
		}

	}

	class SendMsgJob implements Runnable {

		private AdUser loginUser;

		public SendMsgJob() {
		}

		public SendMsgJob(AdUser user) {
			this.loginUser = user;
		}

		@Override
		public void run() {
			String userId = String.valueOf(loginUser.getId());
			try {
				AdInvitationRecord record = adInvitationRecordDao.findRecodByInviteeId(userId);
				logger.info("SendMsgJob userId=" + userId + ",record.id=" + (record != null ? record.getId() : "null"));
				if (record != null) {
					String from = record.getChannel();
					if (!StringUtils.isEmpty(from)) {
						AdUser user = adUserDao.getById(record.getInviterId());
						String dateStr = DateUtils.formatDate(new Date(), "yyyy年MM月dd日 HH:mm");
						String telephone = loginUser.getTelephone();
						if (from.endsWith("app")) {
							String content = "您邀请的亲友(手机尾号" + telephone.substring(7, 11) + ")已成功开通兜礼会员。";
							adSystemNoitceService.sendMessage(String.valueOf(user.getId()), "invitation", content);
						} else {
							// 公众号推送
							LifeWechatBinding wechatBind = WechatUtil.getLifeWechatBinding(user.getTelephone(),
									user.getCardNumber());
							JSONObject data = new JSONObject();
							data.put("dateStr", dateStr);
							data.put("telphone", telephone);
							data.put("openId", wechatBind.getOpenId());
							data.put("url", invite_url);
							data.put("channel", from.startsWith("wisco") ? "wugang" : "doooly");
							redisTemplate.convertAndSend("FAMILY_INVITE", data.toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("SendMsgJob userId=" + userId + ", e = +", e);
			}
		}
	}

	/**
	 * 激活码验证会员信息
	 */
	public JSONObject validateCode(String code) throws Exception {
		// 返回数据
		JSONObject resultData = new JSONObject();
		try {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.SUCCESS.getMsg());

			// 获取激活码用户信息
			AdActiveCode adActiveCode = adActiveCodeDao.findCodeStatusByCode(code);
			if (adActiveCode != null) {
				if (adActiveCode.getAdUser() == null || adActiveCode.getAdUser().getAdGroup() == null) {
					logger.info("====【validateActiveCode】用户信息:" + adActiveCode.getAdUser() + "为空,或用户对应企业信息:"
							+ adActiveCode.getAdUser().getAdGroup() + "为空=====");
					// 根据姓名和工号没有匹配到会员
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.USER_NOT_EXIST.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.USER_NOT_EXIST.getMsg());
					resultData.put("memberId", null);
					return resultData;
				}
				logger.info("====【validateActiveCode】根据激活码获取用户信息-是否使用isUsed：" + adActiveCode.getIsUsed()
						+ ",是否激活isActive:" + adActiveCode.getAdUser().getIsActive() + ",企业简称groupShortName:"
						+ adActiveCode.getAdUser().getAdGroup().getGroupShortName() + ",员工姓名name:"
						+ adActiveCode.getAdUser().getName() + ",企业主键groupNum:" + adActiveCode.getAdUser().getGroupNum()
						+ ",激活码主键adActiveCodeId:" + adActiveCode.getId());
				String isUsed = adActiveCode.getIsUsed();
				String isActive = adActiveCode.getAdUser().getIsActive();
				// 激活码使用状态
				if (("0").equals(isUsed)) {
					// 激活码对应用户激活状态
					if ("2".equals(isActive)) {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.IS_ACTIVED.getCode());
						resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.IS_ACTIVED.getMsg());
					} else {
						resultData.put("groupShortName", adActiveCode.getAdUser().getAdGroup().getGroupShortName());
						resultData.put("name", adActiveCode.getAdUser().getName());
						resultData.put("groupNum", adActiveCode.getAdUser().getGroupNum());
						resultData.put("adActiveCodeId", adActiveCode.getId());
					}
				} else if (isUsed.equals("1")) {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.IS_USED.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.IS_USED.getMsg());
				} else {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getCode());
					resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.CODE_STATE_ERROR.getMsg());
				}
			} else {
				resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.CODE_NOT_EXIST.getCode());
				resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.CODE_NOT_EXIST.getMsg());
				logger.info("====【validateActiveCode】激活码信息-AdActiveCode：" + adActiveCode);
			}
		} catch (Exception e) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MESS, ConstantsLogin.CodeActive.FAIL.getMsg());
			e.printStackTrace();
			logger.info("====【validateActiveCode】service系统错误========");
		}
		logger.info("====【validateActiveCode】返回数据-resultData：" + resultData.toJSONString());
		return resultData;
	}

	@Override
	public MessageDataBean userBind(JSONObject paramJson) {
		// 开始毫秒数
		Long startTime = System.currentTimeMillis();
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			//获取渠道来源第三方
			String thirdPartyChannel = paramJson.getString(Constants.THIRDPARTYCHANNEL);
			if(DaHuaConstants.THIRDPARTYCHANNEL_DAHUA.equals(thirdPartyChannel)){
				//大华渠道去绑定的手机号和用户信息
				String url = configDictServiceI.getValueByTypeAndKey("THIRD_PART_DAHUA", "USER_INFO_URL");
				String groupId = configDictServiceI.getValueByTypeAndKey("THIRD_PART_DAHUA", DaHuaConstants.THIRDPARTYCHANNEL_DAHUA);
				String thirdUserToken = paramJson.getString("thirdUserToken");
				JSONObject param = new JSONObject();
				param.put("jsonData",thirdUserToken);
				JSONObject jsonObject = HttpClientUtil.httpPost(url + DaHuaConstants.USER_INFO_URL, param);
				if(jsonObject!= null && "200".equals(jsonObject.getString("ResultCode")) && "true".equals(jsonObject.getString("IsSuccess"))){
					//说明请求成功绑定用户信息
					JSONObject result = JSONObject.parseObject(jsonObject.getString("Result"));
					String fItemName = result.getString("FItemName");//大华姓名
					String FDeptName = result.getString("FDeptName");
					String FDeptId = result.getString("FDeptId");
					String FIsValid = result.getString("FIsValid");
					String FShortTel = result.getString("FShortTel");
					String SupervisorId = result.getString("SupervisorId");
					String SupervisorName = result.getString("SupervisorName");
					String FItemNumber = result.getString("FItemNumber");//大华工号
					String FVersionUsed = result.getString("FVersionUsed");
					String FEmail = result.getString("FEmail");
					String FBirthDay = result.getString("FBirthDay");
					String mobile = paramJson.getString("loginName");
					Map<String,Object> params = new HashMap<>();
					params.put("workNumber",FItemNumber);
					params.put("adGroupId",groupId);
					//根据工号和企业查询
					AdUserConn isUser = adUserPersonalInfoDao.getIsUser(params);
					if(isUser != null){
						if(StringUtils.isNotBlank(isUser.getTelephone()) && !mobile.equals(isUser.getTelephone())){
							//手机号不正确
							messageDataBean.setCode(ConstantsLogin.Login.FAIL.getCode());
							messageDataBean.setMess("手机号不正确，请确认后重试");
						}else {
							//直接更新用户信息
							AdUser adUserParam = new AdUser();
							adUserParam.setId(isUser.getId());
							adUserParam.setName(fItemName);
							adUserParam.setTelephone(mobile);
							adUserDao.updateByPrimaryKeySelective(adUserParam);
							messageDataBean.setCode(ConstantsLogin.Login.SUCCESS.getCode());
							messageDataBean.setMess(ConstantsLogin.Login.SUCCESS.getMsg());
						}
					}else {
						//说明没有绑定过工号，直接走激活流程
						// 激活用户
						JSONObject paramData = new JSONObject();
						paramData.put("mobile",mobile);
						paramData.put("groupId",groupId);
						paramData.put("name",fItemName);
						paramData.put("workerNumber",FItemNumber);
						paramData.put("remarks","大华企业登录激活");
						messageDataBean = this.execCommandActive(paramData);
						logger.info("====【userBind】-【userBind】验证,激活总耗时：" + (System.currentTimeMillis() - startTime));
					}
				}else {
					messageDataBean.setCode(ConstantsLogin.Login.FAIL.getCode());
					messageDataBean.setMess(ConstantsLogin.Login.FAIL.getMsg());
				}
			}else {
				messageDataBean.setCode(ConstantsLogin.Login.SUCCESS.getCode());
				messageDataBean.setMess(ConstantsLogin.Login.SUCCESS.getMsg());
			}
		} catch (Exception e) {
			messageDataBean.setCode(ConstantsLogin.Login.FAIL.getCode());
			messageDataBean.setMess(ConstantsLogin.Login.FAIL.getMsg());
			logger.info("====【userBind】系统错误==========");
		}
		logger.info("====【userBind】-返回数据：" + messageDataBean.toJsonString());
		return messageDataBean;
	}


}
