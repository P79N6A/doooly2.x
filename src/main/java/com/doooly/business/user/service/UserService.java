package com.doooly.business.user.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.*;
import com.doooly.business.common.utils.GenerateImageCodeUtil;
import com.doooly.business.common.utils.ReqTransFormatUtils;
import com.doooly.business.myaccount.service.MyAccountServiceI;
import com.doooly.business.user.utils.AccountUtil;
import com.doooly.common.constants.Constants;
import com.doooly.common.constants.Constants.AppClientType;
import com.doooly.common.constants.Constants.MemberType;
import com.doooly.common.constants.Constants.ResponseCode;
import com.doooly.common.constants.Constants.ResponseEnum;
import com.doooly.common.dto.BaseRes;
import com.doooly.common.token.TokenUtil;
import com.doooly.dao.reachad.AdBlocBlackUserDao;
import com.doooly.dao.reachad.AdBlocGroupLoginDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachlife.LifeMemberDao;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.dto.user.*;
import com.doooly.entity.reachad.*;
import com.doooly.entity.reachlife.LifeMember;
import com.doooly.pay.dto.BasePayRes;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员服务类(主)
 * 
 * @author 赵清江
 * @date 2016-06-28
 * @version 1.0
 */
@Service
public class UserService implements UserServiceI {

	private static Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private WSServiceI wsService;
	@Autowired
	private AppTokenServiceI tokenService;
	@Autowired
	private AppClientServiceI clientService;
	@Autowired
	private AdUserServiceI adUserService;
	@Autowired
	private AdGroupServiceI adGroupService;
	@Autowired
	private AdActiveCodeServiceI adActiveCodeService;
	@Autowired
	private LifeMemberServiceI lifeMemberService;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	protected StringRedisTemplate redisTemplate;
	@Autowired
	private AdGroupDao adGroupDao;
	@Autowired
	private LifeMemberDao memberDao;
	@Autowired
	private AdBlocGroupLoginDao blocGroupLoginDao;
	@Autowired
	private AdBlocBlackUserDao blocBlackUserDao;

	@Autowired
	protected MyAccountServiceI myAccountService;

	// 会员token，唯一标识，放入缓存
	private static String TOKEN_KEY = "token:%s";

	// 会员token名称
	private static String TOKEN_NAME = "token";

	/**
	 * 处理会员登录
	 */
	@Override
	public LoginRes manageLogin(LoginReq req) {
		LoginRes response = new LoginRes();

		try {
			if (req.getLoginType() == ACCOUNT_LOGIN) {
				response = checkAccountLogin(req);
			} else if (req.getLoginType() == MOBILE_LOGIN) {
				response = checkMobileLogin(req);
			} else
				;

		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 处理会员登出
	 */
	@Override
	public LogoutRes manageLogout(LogoutReq req) {
		AppToken token = new AppToken();
		String account = req.getAccount();
		if (account.length() == 11) {
			token.setMobile(account);
		} else {
			token.setCard(account);
		}
		token.setToken(req.getToken());
		tokenService.unBind(token);
		LogoutRes res = new LogoutRes();
		res.setSuccessBaseResponse();
		return res;
	}

	/**
	 * 获取验证码
	 */
	@Override
	public GetVerifyCodeRes getVerifyCode(GetVerifyCodeReq req) {
		GetVerifyCodeRes response = new GetVerifyCodeRes();
		try {
			response = wsService.GetVerifyCode(req);
			if (response.getCode().equals(ResponseCode.SUCCESS.getCode())) {
				switch (req.getMsgType()) {
				case GetVerifyCodeReq.MOBILE_TEXT:
					response.setVerificationCode(null);
					response.setVerificationCode18(null);
					return response;
				case GetVerifyCodeReq.MOBILE_DIRECT:
					response.setVerificationCode18(null);
					return response;
				case GetVerifyCodeReq.DIMENSION:
					response.setVerificationCode(null);
					String code_QR_URL = GenerateImageCodeUtil.generateQRCode(response.getVerificationCode18(), null);
					String code_BR_URL = GenerateImageCodeUtil.generateBarCode(response.getVerificationCode18(), null);
					response.setBarCodeUri(code_BR_URL);
					response.setDimCodeUri(code_QR_URL);
					return response;
				default:
					response.setServerUnkownErrorResponse();
					response.setVerificationCode(null);
					response.setVerificationCode18(null);
					response.setBarCodeUri(null);
					response.setDimCodeUri(null);
					break;
				}
			}
		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			response.setVerificationCode(null);
			response.setVerificationCode18(null);
			response.setBarCodeUri(null);
			response.setDimCodeUri(null);
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 验证验证码
	 */
	@Override
	public CheckVerifyCodeRes checkVerifyCode(CheckVerifyCodeReq req) {
		CheckVerifyCodeRes response = new CheckVerifyCodeRes();
		try {
			// 验证手机格式
			if (!AccountUtil.isMobile(req.getMobile())) {
				response.setCode(ResponseCode.ACCOUNT_MOBILE_FORMAT_ERROR_FAILURE.getCode());
				response.setMsg(ResponseCode.ACCOUNT_MOBILE_FORMAT_ERROR_FAILURE.getDesc());
				return response;
			}
			// 验证验证码
			BasePayRes basePayRes = wsService.CheckVerifyCode(req.getStoresId(), req.getMobile(), req.getVerifyCode());
			System.out.println("验证码验证信息: " + basePayRes.getCode() + "  " + basePayRes.getInfo());
			switch (basePayRes.getCode()) {
			case ReqTransFormatUtils.SUCCESS:
				response.setSuccessBaseResponse();
				break;
			case ReqTransFormatUtils.Failed:
				response.setCode(ResponseCode.ACCOUNT_NOT_MATCH_THE_CODE.getCode());
				response.setMsg(ResponseCode.ACCOUNT_NOT_MATCH_THE_CODE.getDesc());
				break;
			case ReqTransFormatUtils.VERIFY_CODE_ERROR:
				response.setCode(ResponseCode.ACCOUNT_VERIFY_CODE_FAILURE.getCode());
				response.setMsg(ResponseCode.ACCOUNT_VERIFY_CODE_FAILURE.getDesc());
				break;
			default:
				response.setCode(ResponseCode.SERVER_UNKOWN_ERROR.getCode());
				response.setMsg(ResponseCode.SERVER_UNKOWN_ERROR.getDesc());
				break;
			}
		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 验证激活码
	 */
	@Override
	public CheckActiveCodeRes checkActiveCode(CheckActiveCodeReq req) {
		CheckActiveCodeRes response = new CheckActiveCodeRes();
		try {
			// 查询该会员卡号是否存在
			AdUser user = adUserService.get(null, req.getCardNumber());
			if (user == null) {
				response.setCode(ResponseCode.ACCOUNT_NOT_EXSIT_FAILURE.getCode());
				response.setMsg(ResponseCode.ACCOUNT_NOT_EXSIT_FAILURE.getDesc());
				return response;
			}
			// 若会员卡号存在,判定激活码是否有效
			switch (adActiveCodeService.isActiveCodeValid(req.getCardNumber(), req.getActiveCode())) {
			case AdActiveCodeServiceI.ACTIVE_CODE_NOT_EXIST:
				response.setCode(ResponseCode.ACCOUNT_ACTIVE_CODE_NOT_EXSIT_FAILURE.getCode());
				response.setMsg(ResponseCode.ACCOUNT_ACTIVE_CODE_NOT_EXSIT_FAILURE.getDesc());
				break;
			case AdActiveCodeServiceI.ACTIVE_CODE_NOT_USED:
				response.setSuccessBaseResponse();
				break;
			case AdActiveCodeServiceI.ACTIVE_CODE_USED:
				response.setCode(ResponseCode.ACCOUNT_ACTIVE_CODE_IS_USED.getCode());
				response.setMsg(ResponseCode.ACCOUNT_ACTIVE_CODE_IS_USED.getDesc());
				break;
			default:
				response.setServerUnkownErrorResponse();
				break;
			}
		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 修改手机号
	 */
	@Override
	public ModifyMobileRes modifyMobile(ModifyMobileReq req) {
		ModifyMobileRes response = new ModifyMobileRes();
		try {
			response = wsService.modifyUserMobile(req);
		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 修改密码
	 */
	@Override
	public ModifyPwdRes modifyPassword(ModifyPwdReq req) {
		ModifyPwdRes response = new ModifyPwdRes();
		// 验证手机格式
		if (!AccountUtil.isMobile(req.getMobile())) {
			response.setCode(ResponseCode.ACCOUNT_MOBILE_FORMAT_ERROR_FAILURE.getCode());
			response.setMsg(ResponseCode.ACCOUNT_MOBILE_FORMAT_ERROR_FAILURE.getDesc());
			return response;
		}

		try {
			response = wsService.modifyUserInfo(req);
		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 用户激活
	 */
	@Override
	public UserActiveRes manageUserActive(UserActiveReq req) {
		UserActiveRes response = new UserActiveRes();
		int isSuccess = 0;
		// 修改会员手机号与密码
		try {
			// 1:修改会员手机号和密码
			response = wsService.modifyUserInfo(req);
			if (response.getCode().equals(ResponseCode.SUCCESS.getCode())) {
				// 2:修改该会员对应的激活码的使用状态
				isSuccess = adActiveCodeService.useActiveCode(req.getCardNumber());
				if (isSuccess != 0) {
					// 3:修改该会员的状态为"激活"
					isSuccess = adUserService.userActivation(req.getCardNumber());
				}
			}
		} catch (Exception e) {
			response.setServerInternalErrorResponse();
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 会员卡验证登录
	 * 
	 * @param req
	 * @return
	 */
	private LoginRes checkAccountLogin(LoginReq req) throws Exception {
		LoginRes res = new LoginRes();
		// 验证会员卡号是否合法
		if (!adGroupService.isCardNumberValid(req.getAccount())) {
			res.setCode(ResponseCode.ACCOUNT_NOT_EXSIT_FAILURE.getCode());
			res.setMsg(ResponseCode.ACCOUNT_NOT_EXSIT_FAILURE.getCode());
			return res;
		}
		// 验证会员是否存在
		res = wsService.checkUserIdentity(req);
		if (res.getCode().equals(ResponseCode.SUCCESS.getCode())) {
			res = authenticated(req, res);
		}
		return res;
	}

	/**
	 * 手机验证登录
	 * 
	 * @param req
	 * @return
	 */
	private LoginRes checkMobileLogin(LoginReq req) throws Exception {
		LoginRes response = new LoginRes();
		// 判断手机号格式是否正确
		if (!AccountUtil.isMobile(req.getAccount())) {
			response.setCode(ResponseCode.ACCOUNT_MOBILE_FORMAT_ERROR_FAILURE.getCode());
			response.setMsg(ResponseCode.ACCOUNT_MOBILE_FORMAT_ERROR_FAILURE.getDesc());
			return response;
		}
		BasePayRes basePayRes = wsService.CheckVerifyCode("", req.getAccount(), req.getPassword());
		switch (basePayRes.getCode()) {
		case ReqTransFormatUtils.SUCCESS:
			response.setSuccessBaseResponse();
			authenticated(req, response);
			break;
		case ReqTransFormatUtils.Failed:
			response.setCode(ResponseCode.ACCOUNT_NOT_MATCH_THE_CODE.getCode());
			response.setMsg(ResponseCode.ACCOUNT_NOT_MATCH_THE_CODE.getDesc());
			break;
		case ReqTransFormatUtils.VERIFY_CODE_ERROR:
			response.setCode(ResponseCode.ACCOUNT_VERIFY_CODE_FAILURE.getCode());
			response.setMsg(ResponseCode.ACCOUNT_VERIFY_CODE_FAILURE.getDesc());
			break;
		default:
			response.setCode(ResponseCode.SERVER_UNKOWN_ERROR.getCode());
			response.setMsg(ResponseCode.SERVER_UNKOWN_ERROR.getDesc());
			break;
		}
		System.out.println("验证码验证信息: " + basePayRes.getCode() + "  " + basePayRes.getInfo());
		return response;
	}

	/**
	 * 会员认证后进行信息处理
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	private LoginRes authenticated(LoginReq req, LoginRes res) throws Exception {
		String tokenStr = null;
		int appClientID = -1;
		// 查找会员
		AdUser user = adUserService.get(req.getAccount(), req.getAccount());
		System.out.println(user.getId());
		// 插入或更新会员设备信息
		AppClient client = req.getClient();
		client.setUserId(user.getId());
		clientService.addOrUpdate(client);
		appClientID = clientService.getID(user.getId(), req.getClient().getDeviceNumber());
		// 新增或更新会员token(app)
		if (req.getClient().getClientType() == AppClientType.Android.getValue()
				|| req.getClient().getClientType() == AppClientType.IOS.getValue()) {
			// 校验该账号是否在其它手机登录,若无则直接绑定,若有则解绑后绑定
			AppToken token = new AppToken();
			token.setCard(res.getCardNumber());
			token.setMobile(res.getMobile());
			token.setAppClientId(appClientID);
			if (tokenService.isBind(req.getAccount())) {
				tokenService.unBind(token);
				tokenStr = tokenService.renewBind(token);
				res.setCode(ResponseCode.DUPLICATE_LOGIN.getCode());
				res.setMsg(ResponseCode.DUPLICATE_LOGIN.getDesc());
			} else {
				tokenStr = tokenService.newBind(token);
			}
			res.setToken(tokenStr);
		}
		res.setMemberType((int) user.getType());
		if (user.getType() == MemberType.Employee_Relation.getValue()) {
			res.setReferrer(user.getSourceCardNumber());
		}
		res.setCompany(adGroupService.getGroupName(user.getGroupNum()));
		res.setCardNumber(user.getCardNumber());
		res.setMobile(user.getTelephone());
		return res;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false)
	public BaseRes managerUserActiveNew(UserActiveNewReq req) {
		BaseRes response = new BaseRes();
		String mobile = req.getMobile();
		String newPwd = req.getNewPwd();
		if (mobile == null || mobile.length() == 0) {
			response.setCode(ResponseCode.SERVER_INTERNAL_ERROR.getCode());
			return response;
		}

		try {
			AdUser user = adUserService.get(mobile, null);

			if (user == null) {
				throw new IllegalArgumentException("mobile is not exist");
			}

			user.setDataSyn(AdUser.DATA_SYN_ON);// 设置该会员数据能被同步

			Date now = new Date();
			user.setActiveDate(now);
			user.setUpdateDate(now);// 设置更新(操作激活)时间
			user.setIsActive(AdUser.USER_ACTIVATION_ON);// 设置激活标识为"激活"
			adUserDao.updateActiveStatus(user);

			if (newPwd != null && newPwd.length() > 0) {
				adUserDao.updatePwd(mobile, newPwd);
			}

			String cardNumber = user.getCardNumber();

			lifeMemberService.memberActivation(cardNumber);
			response.setSuccessBaseResponse();
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode(ResponseCode.SERVER_INTERNAL_ERROR.getCode());
		}

		return response;
	}

	/**
	 * 用户登录 缓存token,设备号mobileNum+userId
	 */
	@Override
	public JSONObject userLogin(JSONObject paramJson) throws Exception {
		logger.info("====【userLogin】,用户登录操作参数：" + paramJson.toJSONString());
		// 返回数据
		JSONObject dataJson = new JSONObject();
		try {
			// adUser 主键
			String userId = paramJson.getString("userId");
			// 终端渠道
			String channel = paramJson.getString(Constants.CHANNEL);

			// 用户当前使用token
			String token = "";
			// 验证是否已存在token,如果存在则刷新
			String userToken = redisTemplate.opsForValue().get(String.format(channel + ":" + TOKEN_KEY, userId));
			logger.info("====【userLogin】用户已存在的token-userToken：" + userToken);
			if (StringUtils.isNotBlank(userToken)) {
				// 删除原token用户ID
				redisTemplate.delete(userToken);
				// 刷新token
				token = TokenUtil.refreshUserToken(channel, userId);
			} else {
				token = TokenUtil.getUserToken(channel, userId);
			}
			logger.info("====【userLogin】oldToken=" + userToken + ",newToken=" + token);
			// 获取用户个人信息
			HashMap<String, Object> userInfomMap = myAccountService.getAccountListById(userId);
			if (userInfomMap != null) {
				userInfomMap.put(TOKEN_NAME, token);
				dataJson.put("userInfo", new Gson().toJson(userInfomMap));
				dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.SUCCESS.getCode());
				dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.SUCCESS.getMsg());
			} else {
				dataJson.put("userInfo", "");
				dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_NOT_EXIST.getCode());
				dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_NOT_EXIST.getMsg());
			}
			// dataJson.put(AppConstants.TOKEN, token);
		} catch (Exception e) {
			dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.FAIL.getCode());
			dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.FAIL.getMsg());
			logger.info("====【userLogin】系统错误========");
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【userLogin】返回数据：" + dataJson.toJSONString());
		return dataJson;
	}

	/**
	 * APP用户登录验证, 缓存token
	 */
	@Override
	public JSONObject userValidateLogin(JSONObject paramJson) throws Exception {
		logger.info("====【userValidateLogin】,用户登录操作参数：" + paramJson.toJSONString());

		// 返回数据
		JSONObject dataJson = new JSONObject();
		try {
			dataJson = adUserService.validateUserInfo(paramJson);
			if (dataJson != null && (ConstantsLogin.Login.SUCCESS.getCode().equals(dataJson.getString("code"))
					|| ConstantsLogin.Login.USER_NOT_ACTIVED.getCode().equals(dataJson.getString("code")))) {
				// adUser 主键
				String userId = dataJson.getString("userId");
				// 终端渠道
				String channel = paramJson.getString(Constants.CHANNEL);

				// 用户当前使用token
				String token = "";
				// 验证是否已存在token,如果存在则刷新
				String userToken = redisTemplate.opsForValue().get(channel + ":" + String.format(TOKEN_KEY, userId));
				logger.info("====【userValidateLogin】用户已存在的token-userToken：" + userToken);
				if (StringUtils.isNotBlank(userToken)) {
					// 删除原token用户ID
					redisTemplate.delete(userToken);
					// 刷新token
					token = TokenUtil.refreshUserToken(channel, userId);
				} else {
					token = TokenUtil.getUserToken(channel, userId);
				}
				// 获取用户个人信息
				HashMap<String, Object> userInfomMap = myAccountService.getAccountListById(userId);
				if (userInfomMap != null) {
					userInfomMap.put(TOKEN_NAME, token);
					dataJson.put("userInfo", new Gson().toJson(userInfomMap));
					dataJson.put(ConstantsLogin.CODE, dataJson.getString("code"));
					dataJson.put(ConstantsLogin.MESS, dataJson.getString("mess"));
				} else {
					dataJson.put("userInfo", "");
					dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.USER_NOT_EXIST.getCode());
					dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.USER_NOT_EXIST.getMsg());
				}
			}
		} catch (Exception e) {
			dataJson.put(ConstantsLogin.CODE, ConstantsLogin.Login.FAIL.getCode());
			dataJson.put(ConstantsLogin.MESS, ConstantsLogin.Login.FAIL.getMsg());
			logger.info("====【userValidateLogin】系统错误========");
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【userValidateLogin】返回数据：" + dataJson.toJSONString());
		return dataJson;
	}

	@Override
	public MessageDataBean userLogout(JSONObject paramJson, HttpServletRequest request) throws Exception {
		// 返回数据
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 用户认证token-rediskey
			String token = paramJson.getString(ConstantsLogin.TOKEN);
			String userId = paramJson.getString("userId");
			String channel = paramJson.getString(ConstantsLogin.CHANNEL);
			logger.info("====【userLogout】-传入参数：" + paramJson.toJSONString());
			if (StringUtils.isBlank(token) || StringUtils.isBlank(userId) || StringUtils.isBlank(channel)) {
				// 用户认证token-rediskey
				token =  request.getHeader(ConstantsLogin.TOKEN);
				userId = redisTemplate.opsForValue().get(token);
				channel = request.getHeader(ConstantsLogin.CHANNEL);
				logger.info("====>>【userLogout】header-token：" + token + ",==channel："	+ channel + ",==userId：" + userId);
			}
			// 删除用户缓存key
			redisTemplate.delete(String.format(channel + ":" + TOKEN_KEY, userId));
			redisTemplate.delete(token);
			messageDataBean.setCode(ConstantsLogin.Login.SUCCESS.getCode());
			messageDataBean.setMess(ConstantsLogin.Login.SUCCESS.getMsg());
		} catch (Exception e) {
			messageDataBean.setCode(ConstantsLogin.Login.FAIL.getCode());
			messageDataBean.setMess(ConstantsLogin.Login.FAIL.getMsg());
			logger.info("====【userLogout】系统错误==========");
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【userLogout】-返回数据：" + messageDataBean.toJsonString());
		return messageDataBean;
	}

	@Override
	public BaseRes<JSONObject> getWechatUserInitInfo(JSONObject paramJson) throws Exception {
		logger.info("====【getWechatUserInitInfo】-传入参数：" + paramJson.toJSONString());
		// 返回数据
		BaseRes<JSONObject> baseRes = new BaseRes<JSONObject>();
		try {
			// 卡号-username
			String cardNumber = paramJson.getString("username");
			String channel = paramJson.getString(Constants.CHANNEL);
			// 查询用户信息userId
			AdUser adUser = new AdUser();
			adUser.setCardNumber(cardNumber);
			HashMap<String, Object> userInfoMap = adUserDao.getWechatUserInitInfo(adUser);
			if (userInfoMap != null && StringUtils.isNotBlank(userInfoMap.get("userId").toString())) {
				String token = TokenUtil.getUserToken(channel, userInfoMap.get("userId").toString());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(TOKEN_NAME, token);
				jsonObject.put("userId", userInfoMap.get("userId").toString());
				jsonObject.put("userName", userInfoMap.get("userName").toString());
				jsonObject.put("mobile", userInfoMap.get("mobile").toString());
				jsonObject.put("groupShortName", userInfoMap.get("groupShortName").toString());
				baseRes = new BaseRes<JSONObject>(ConstantsLogin.Login.SUCCESS.getCode(),
						ConstantsLogin.Login.SUCCESS.getMsg(), jsonObject);
			} else {
				baseRes = new BaseRes<JSONObject>(ConstantsLogin.Login.USER_NOT_EXIST.getCode(),
						ConstantsLogin.Login.USER_NOT_EXIST.getMsg());
			}

		} catch (Exception e) {
			baseRes = new BaseRes<JSONObject>(ConstantsLogin.Login.FAIL.getCode(), ConstantsLogin.Login.FAIL.getMsg());
			logger.info("====【getWechatUserInitInfo】系统错误==========");
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【getWechatUserInitInfo】-返回数据：" + baseRes.toJsonString());
		return baseRes;
	}

	/**
	 * 
	 */
	@Override
	public AdUser addUser(AdUser user, String groupName) {
		AdUser u = new AdUser();
		String mobile = user.getTelephone();
		u.setTelephone(mobile);
		// 激活标识
		String isActive = "2";
		u.setIsActive(isActive);
		AdUser userDetail = adUserDao.get(u);
		if (userDetail == null) {
			AdGroup group = adGroupDao.findGroupByGroupName(groupName);
			u.setGroupNum(group.getId());
			String cardNumber = group.getGroupNum() + mobile.substring(4);
			u.setCardNumber(cardNumber);
			Date date = new Date();
			u.setName(user.getName());
			u.setActiveDate(date);
			u.setUpdateDate(date);
			u.setIsActive(isActive);
			u.setDataSyn("1");// 设置同步给商家
			u.setDelFlag("0");
			int userCount = adUserDao.addActiveUser(u);
			// userCount=0:会员卡号重复，需要重新生成会员卡号
			if (userCount == 0) {
				logger.warn(String.format("新增并激活会员失败，groupName=%s,mobile=%s", groupName, mobile));
				// 循环3次，重新生成会员卡号
				for (int i = 0; i < 3; i++) {
					cardNumber = group.getGroupNum() + mobile.substring(4) + RandomUtils.nextInt(1, 9);
					u.setCardNumber(cardNumber);
					userCount = adUserDao.addActiveUser(u);
					if (userCount > 0)
						break;
				}
			}
			LifeMember member = new LifeMember();
			member.setMobile(mobile);
			member.setUsername(u.getCardNumber());
			member.setGroupId(u.getGroupNum());
			member.setIsEnabled(2);// 直接激活
			member.setMemberType(0);
			member.setName(user.getName());
			member.setAdId(String.valueOf(u.getId()));
			memberDao.addMember(member);
		} else {
			logger.warn(String.format("手机号=%s 已经是兜礼会员", mobile));
		}
		return u;
	}

	/**
	 * 快捷登录 1.参数验证 2.企业合法性验证 3.签名验证 4.会员白名单验证 5.（可选）添加为兜礼会员并添加到黑名单标记
	 */
	public BaseRes<String> checkQuickLogin(String groupId, String mobile, String name, String groupName,
			Long actionTime, String sign) {
		// 1.校验参数是否为空
		if (StringUtils.isBlank(groupId) || StringUtils.isBlank(mobile) || StringUtils.isBlank(name)
				|| actionTime == null || StringUtils.isBlank(sign)) {
			return new BaseRes<String>(ResponseEnum.ERROR_PARAM_BLANK.getCode(),
					ResponseEnum.ERROR_PARAM_BLANK.getMsg());
		}
		// 2.企业标识groupId验证
		AdBlocGroupLogin groupLoginInfo = blocGroupLoginDao.selectByBlocChannel(groupId);
		if (groupLoginInfo == null) {
			return new BaseRes<String>(ResponseEnum.ERROR_GROUP_NOT_EXIST.getCode(),
					ResponseEnum.ERROR_GROUP_NOT_EXIST.getMsg());
		}
		// 3.actionTime快捷登录开始时间（秒）
		Integer actionPeriod = groupLoginInfo.getActionPeriod();
		// 快捷登录时间已超过规定时间（与当前时间比较）
		if ((System.currentTimeMillis() / 1000 - actionTime) > actionPeriod) {
			return new BaseRes<String>(ResponseEnum.ERROR_ACTION_EXPIRED.getCode(),
					ResponseEnum.ERROR_ACTION_EXPIRED.getMsg());
		}
		// 4.签名验证
		String secretKey = groupLoginInfo.getSecretKey();
		StringBuffer orignStr = new StringBuffer("groupId=");
		orignStr.append(groupId).append("&groupKey=").append(secretKey).append("&mobile=").append(mobile)
				.append("&name=").append(name).append("&groupName=").append(groupName).append("&actionTime=")
				.append(actionTime);
		if (!AccountUtil.checkMD5Sign(orignStr.toString(), sign)) {
			// 4.1验证失败
			logger.error(String.format("快捷登录签名验证失败，原始信息=%s，原始签名信息=%s", orignStr, sign));
			return new BaseRes<String>(ResponseEnum.ERROR_SIGN.getCode(), ResponseEnum.ERROR_SIGN.getMsg());
		}
		/**
		 * 4.验证白名单（手机号+姓名）是否存在兜礼会员库. 不存在则： a.升级为兜礼会员 b.同时添加到黑名单库
		 */
		// 4.1 验证是否存在会员库
		AdUser user = adUserDao.findByMobile(mobile);
		// 4.1.1 会员手机号不存在
		try {
			name = URLDecoder.decode(name, "UTF-8");
			groupName = URLDecoder.decode(groupName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("快捷登陆url转码异常:" + e);
			// return new BaseRes<String>(ResponseEnum.ERROR.getCode(),
			// ResponseEnum.ERROR.getMsg());
		}
		if (user == null) {
			logger.error(String.format("快捷登录参数验证失败，兜礼会员库不存在，mobile=%s，name=%s,groupName=%s", mobile, name, groupName));
			// 5.1 添加为兜礼会员
			user = new AdUser();
			user.setTelephone(mobile);
			user.setName(name);
			user = addUser(user, groupLoginInfo.getBlackGroupName());
			// 5.2 添加到黑名单
			AdBlocBlackUser blackUser = new AdBlocBlackUser(groupLoginInfo.getBlocId(), user.getId(), mobile, name,
					groupName);
			int blackNum = blocBlackUserDao.insert(blackUser);
			if (blackNum > 0) {
				logger.info(String.format("快捷登录验证失败，已将该用户临时激活为兜礼会员并添加到黑名单,mobile=%s, name=%s, groupName=%s", mobile,
						name, groupName));
			}
			// return new
			// BaseRes<String>(ResponseEnum.ERROR_MOBILE_NOT_EXIST.getCode(),
			// ResponseEnum.ERROR_MOBILE_NOT_EXIST.getMsg());
		} else if (AdUser.USER_ACTIVATION_OFF.equals(user.getIsActive())) {
			// 若手机号签名认证通过且已存在但未激活，则进行激活操作
			Date activeDate = new Date();
			user.setActiveDate(activeDate);
			user.setUpdateDate(activeDate);
			user.setDataSyn(AdUser.DATA_SYN_ON);
			user.setIsActive(AdUser.USER_ACTIVATION_ON);
			adUserDao.updateActiveStatus(user);
			LifeMember lifeMember = new LifeMember();
			lifeMember.setUsername(user.getCardNumber());
			lifeMember.setMobile(user.getTelephone());
			lifeMember.setModifyDate(activeDate);
			lifeMember.setIsEnabled(Integer.valueOf(AdUser.USER_ACTIVATION_ON));
			memberDao.updateMemberActiveStatus(lifeMember);
			logger.info(String.format("快捷登录签名认证通过且已存在但未激活，已激活成功。mobile=%s, name=%s", mobile, name));
		}
		// 4.1.2 会员姓名不匹配
		if (!name.equals(user.getName())) {
			logger.error(String.format("快捷登录参数验证失败，兜礼会员库不存在，mobile=%s，name=%s,groupName=%s", mobile, name, groupName));
			return new BaseRes<String>(ResponseEnum.ERROR_NAME_NOT_EXIST.getCode(),
					ResponseEnum.ERROR_NAME_NOT_EXIST.getMsg());
		}

		// 快捷登录验证成功
		return new BaseRes<String>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
	}

	/**
	 * 企业批量更新会员信息记录
	 * 
	 * @author hutao
	 * @date 创建时间：2017年10月20日 下午2:32:04
	 * @version 1.0
	 * @parameter
	 * @since
	 * @return
	 */
	public BaseRes<JSONObject> batchUpdateUser(JSONObject reqJson) {
		// 1.参数有效性验证
		if (reqJson == null) {
			return new BaseRes<JSONObject>(ResponseEnum.ERROR_PARAM_BLANK.getCode(),
					ResponseEnum.ERROR_PARAM_BLANK.getMsg());
		}
		String groupId = reqJson.getString("groupId");
		JSONArray employees = reqJson.getJSONArray("employees");
		if (StringUtils.isBlank(groupId) || employees == null) {
			return new BaseRes<JSONObject>(ResponseEnum.ERROR_PARAM_BLANK.getCode(),
					ResponseEnum.ERROR_PARAM_BLANK.getMsg());
		}
		// 2.企业标识groupId验证
		AdBlocGroupLogin groupLoginInfo = blocGroupLoginDao.selectByBlocChannel(groupId);
		if (groupLoginInfo == null) {
			return new BaseRes<JSONObject>(ResponseEnum.ERROR_GROUP_NOT_EXIST.getCode(),
					ResponseEnum.ERROR_GROUP_NOT_EXIST.getMsg());
		}

		// 3.批量记录用户信息,若不是白名单（即兜礼会员），则需要添加到黑名单记录表
		int total = 0;// 记录总条数
		for (int index = 0; index < employees.size(); index++) {
			JSONObject employee = employees.getJSONObject(index);
			String mobile = employee.getString("newMobile");
			AdUser user = adUserDao.findByMobile(mobile);
			if (user == null) {
				String oldMobile = employee.getString("oldMobile");
				String name = employee.getString("name");
				String groupName = employee.getString("groupName");
				try {
					AdBlocBlackUser blackUser = new AdBlocBlackUser(groupLoginInfo.getBlocId(), 0L, mobile, oldMobile,
							name, groupName);
					int num = blocBlackUserDao.insert(blackUser);
					total += num;
					logger.info(String.format("集团-企业员工信息批量更新成功一条：name=%s,mobile=%s,oldMobile=%s,groupName=%s", name,
							mobile, oldMobile, groupName));
				} catch (Exception e) {
					logger.info(String.format("集团-企业员工信息批量更新失败一条：name=%s,mobile=%s,oldMobile=%s,groupName=%s", name,
							mobile, oldMobile, groupName), e);
				}
			}
		}
		BaseRes<JSONObject> result = new BaseRes<JSONObject>(ResponseEnum.SUCCESS.getCode(),
				ResponseEnum.SUCCESS.getMsg());
		JSONObject data = new JSONObject();
		data.put("successNum", total);
		data.put("state", 1);
		result.setData(data);
		return result;
	}

	@Override
	public BaseRes<JSONObject> saveTelephoneChange(JSONObject reqJson) {
		BaseRes<JSONObject> baseRes = new BaseRes<>();
		try {
			AdUser adUser = new AdUser();
			adUser.setTelephone(reqJson.getString("newTelephone"));
			adUser.setOldTelephone(reqJson.getString("oldTelephone"));
			adUser.setId(Long.valueOf(reqJson.getString("userId")));

			int count = 0;
			if (adUserDao.getTelephoneChange(adUser) > 0) {
				count = adUserDao.updateTelephoneChange(adUser);
			} else {
				count = adUserDao.saveTelephoneChange(adUser);
			}
			if (count > 0) {
				baseRes = new BaseRes<>("1000", "操作成功");
			} else {
				baseRes = new BaseRes<>("1001", "操作失败");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			baseRes = new BaseRes<>("1001", "系统错误");
		}

		return baseRes;
	}

	// @Override
	// public void scheduleUpdateUser() {
	// String url
	// ="http://dunion-test.think-tec.com/index.php?s=/addon/WeiSite/Donghang/getMemberInfo.html&end_time=ENDTIME&start_time=STARTTIME&phone=&timestamp=TIMESTAMP&sign=SIGN";
	// Calendar calendar = Calendar.getInstance();
	// Long end_time =calendar.getTimeInMillis()/1000;
	// calendar.setTime(new Date());
	// calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
	// Long start_time = calendar.getTimeInMillis()/1000;
	// Long timestamp = Calendar.getInstance().getTimeInMillis()/1000;
	// String orign
	// ="end_time="+end_time+"&password=hongbao&start_time="+start_time+"&timestamp="+timestamp;
	// String sign = MD5Util.MD5Encode(orign, "utf-8");
	// System.out.println(sign);
	// url = url.replace("SIGN", sign);
	// url = url.replace("ENDTIME", String.valueOf(end_time));
	// url = url.replace("STARTTIME", String.valueOf(start_time));
	// url = url.replace("TIMESTAMP", String.valueOf(timestamp));
	// System.out.println(url);
	// String httpsGet = HttpClientUtil.httpsGet(url);
	// JSONObject parseObject = JSONObject.parseObject(httpsGet);
	// if (parseObject.getBooleanValue("success")) {
	// List<UserCompareReq> users =
	// JSON.parseArray(parseObject.getString("data"), UserCompareReq.class);
	// for (UserCompareReq user : users) {
	// //如果手机号不在我们库中,添加到ad_bloc_user
	// AdUser findByMobile = adUserDao.findByMobile(user.getPhone());
	// if (findByMobile ==null) {
	// try {
	// AdBlocBlackUser blackUser = new AdBlocBlackUser(88, 0L, user.getPhone(),
	// user.getPhone(),
	// user.getUsername(), user.getSub_org());
	// blocBlackUserDao.insert(blackUser);
	// logger.info(String.format("定时对比员工数据加入成功一条：name=%s,mobile=%s,groupName=%s",
	// user.getUsername(),
	// user.getPhone(),user.getSub_org()));
	// } catch (Exception e) {
	// logger.info(String.format("定时对比员工数据加入失败一条：name=%s,mobile=%s,groupName=%s",
	// user.getUsername(),
	// user.getPhone(),user.getSub_org()));
	// }
	// }
	// }
	// }else {
	// logger.error("定时对比员工数据出错:"+parseObject.getString("error"));
	// }
	// }
	//
	// @Override
	// public void initializeScheduleUpdateUser() {
	// //最初的数据日期
	//// Calendar earliest = new GregorianCalendar(2017,4,1);
	//// Calendar now = Calendar.getInstance();
	// Date d1 = new Date();
	// Date d2 = new Date();
	// try {
	// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// d1 = sdf.parse("2017-04-01 00:00:00");
	// //d2 = sdf.parse("2017-05-30 16:20:00");
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(d1);
	// long time1 = cal.getTimeInMillis();
	// cal.setTime(d2);
	// long time2 = cal.getTimeInMillis();
	// Long between_days=(time2-time1)/(1000*3600*24);
	//// Long
	// between_days=(now.getTimeInMillis()-earliest.getTimeInMillis())/(1000*3600*24);
	// int count =7;
	// int maxLoop = (int) (between_days / count);
	// int minLoop = (int) (between_days % count);
	//
	// if (minLoop != 0) {
	// maxLoop++;
	// }
	// int time =0;
	// try {
	// for (int i = 0; i < maxLoop; i++) {
	// time++;
	// String url
	// ="http://dunion-test.think-tec.com/index.php?s=/addon/WeiSite/Donghang/getMemberInfo.html&end_time=ENDTIME&start_time=STARTTIME&phone=&timestamp=TIMESTAMP&sign=SIGN";
	// Calendar calstart = Calendar.getInstance();
	// Long timestamp =calstart.getTimeInMillis()/1000;
	// calstart.setTime(d1);
	// calstart.set(Calendar.DATE, calstart.get(Calendar.DATE) + (count*i));
	// Long start_time = calstart.getTimeInMillis()/1000;
	// calstart.setTime(d1);
	// calstart.set(Calendar.DATE, calstart.get(Calendar.DATE) + (count*(i+1)));
	// Long end_time = calstart.getTimeInMillis()/1000;
	// if (start_time < timestamp) {
	// String orign
	// ="end_time="+end_time+"&password=hongbao&start_time="+start_time+"&timestamp="+timestamp;
	// String sign = MD5Util.MD5Encode(orign, "utf-8");
	// System.out.println(sign);
	// url = url.replace("SIGN", sign);
	// url = url.replace("ENDTIME", String.valueOf(end_time));
	// url = url.replace("STARTTIME", String.valueOf(start_time));
	// url = url.replace("TIMESTAMP", String.valueOf(timestamp));
	// System.out.println(url);
	// String httpsGet = HttpClientUtil.httpsGet(url);
	// JSONObject parseObject = JSONObject.parseObject(httpsGet);
	// if (parseObject.getBooleanValue("success")) {
	// List<UserCompareReq> users =
	// JSON.parseArray(parseObject.getString("data"), UserCompareReq.class);
	// for (UserCompareReq user : users) {
	// //如果手机号不在我们库中,添加到ad_bloc_user
	// AdUser findByMobile = adUserDao.findByMobile(user.getPhone());
	// if (findByMobile ==null) {
	// try {
	// AdBlocBlackUser blackUser = new AdBlocBlackUser(88, 0L, user.getPhone(),
	// user.getPhone(),
	// user.getUsername(), user.getSub_org());
	// blocBlackUserDao.insert(blackUser);
	// logger.info(String.format("定时对比员工数据加入成功一条：name=%s,mobile=%s,groupName=%s",
	// user.getUsername(),
	// user.getPhone(),user.getSub_org()));
	// } catch (Exception e) {
	// logger.info(String.format("定时对比员工数据加入失败一条：name=%s,mobile=%s,groupName=%s",
	// user.getUsername(),
	// user.getPhone(),user.getSub_org()));
	// }
	// }
	// }
	// }else {
	// logger.error("定时对比员工数据出错:"+parseObject.getString("error"));
	// }
	// }else{break;}
	// }
	// } catch (Exception e) {
	// try {
	// throw new Exception("批量插入记录错误!", e);
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// }
	// System.out.println(time);
	// }
	//
}
