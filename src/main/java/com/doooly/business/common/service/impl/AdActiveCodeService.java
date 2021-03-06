package com.doooly.business.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdActiveCodeServiceI;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.reachLife.LifeGroupService;
import com.doooly.business.user.service.UserServiceI;
import com.doooly.common.constants.Constants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdActiveCodeDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdUserPersonalInfoDao;
import com.doooly.dao.reachlife.LifeMemberDao;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.entity.reachad.AdActiveCode;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserPersonalInfo;
import com.doooly.entity.reachlife.LifeGroup;
import com.doooly.entity.reachlife.LifeMember;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 激活码服务类
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
@Service
public class AdActiveCodeService implements AdActiveCodeServiceI {

	private static final Logger logger = LoggerFactory.getLogger(AdActiveCodeService.class);

	/** B库用户DAO */
	@Autowired
	private AdUserDao adUserDao;

	/** B库激活码DAO */
	@Autowired
	private AdActiveCodeDao adActiveCodeDao;

	/** A库会员DAO */
	@Autowired
	private LifeMemberDao lifeMemberDao;

	/** A库企业Service */
	@Autowired
	private LifeGroupService lifeGroupService;


	/** B库用户信息DAO */
	@Autowired
	private AdUserPersonalInfoDao adUserPersonalInfoDao;

	@Autowired
	private AdUserServiceI adUserServiceI;

	@Autowired
	private UserServiceI userServiceI;

	
	@Override
	public int isActiveCodeValid(String cardNumber, String code) throws Exception{
		AdActiveCode activeCode = new AdActiveCode();
		activeCode.setCardNumber(cardNumber);
		activeCode.setCode(code);
		List<AdActiveCode> list = adActiveCodeDao.get(activeCode);
		if (list.size() != 0) {
			if (list.get(0).getIsUsed().equals("0")) {
				return ACTIVE_CODE_NOT_USED;
			}
			return ACTIVE_CODE_USED;
		}
		return ACTIVE_CODE_NOT_EXIST;
	}

	@Override
	public int useActiveCode(String cardNumber) throws Exception {
		AdActiveCode code = new AdActiveCode();
		code.setCardNumber(cardNumber);
		
		code.setIsUsed(AdActiveCode.ACTIVE_CODE_USED);//设置激活码使用状态
		code.setUsedDate(new Date());//设置使用时间
		
		return adActiveCodeDao.updateUseStatus(code);
	}


	public boolean checkVerificationCode(String mobile,String code) {
		// 1.验证短信验证码是否有效
		JSONObject verificationReq = new JSONObject();
		verificationReq.put("businessId", WebService.BUSINESSID);
		verificationReq.put("storesId", WebService.STOREID);
		verificationReq.put("verificationCode", code);
		verificationReq.put("cardNumber", mobile);
		logger.info("checkVerificationCode请求：{}",verificationReq);
		String result = HTTPSClientUtils.sendPost(verificationReq, Constants.MerchantApiConstants.CHECK_VERIFICATION_CODE_URL);
		logger.info("checkVerificationCode返回：{}",result);
		return JSONObject.parseObject(result).getInteger("code") == 0;
	}



	/**
	 *
	 * @param code
	 * @param mobile
	 * @param staffNum
	 * @param email
	 * @param groupId
	 * @return
	 * @throws Exception
	 * TODO(需要重构)
	 */
	public JSONObject validateFordUser(String code, String mobile, String staffNum, String email, String groupId,String verificationCode) throws Exception {
		JSONObject resultData = new JSONObject();
		resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
		resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.SUCCESS.getMsg());
		//手机号是否被绑定
		AdUser adUser = new AdUser();
		adUser.setTelephone(mobile);
		adUser = adUserDao.get(adUser);
		if (adUser != null) {
			logger.info("用户{}已经存在手机号{}，进入二次匹配流程", adUser.getId(),mobile);
			//第二次进入页面进行匹配

			//已经在福特
			if (groupId.equals(String.valueOf(adUser.getGroupNum()))) {
				AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
				adUserPersonalInfo.setId(adUser.getId());
				adUserPersonalInfo = adUserPersonalInfoDao.select(adUserPersonalInfo);
				if (adUserPersonalInfo != null) {
					//工号和手机号是否匹配
					if (staffNum.equals(adUserPersonalInfo.getWorkNumber())) {
						//邮箱是否匹配
						if (email.equals(adUser.getMailbox())) {
							AdActiveCode adActiveCode = new AdActiveCode();
							adActiveCode.setAdUserId(adUser.getId());
							adActiveCode.setCode(code);
							//adActiveCode.setIsUsed("1");//已使用
							adActiveCode = adActiveCodeDao.getByCondition(adActiveCode);
							if (adActiveCode != null) {
								//短信验证码校验
								if (!checkVerificationCode(mobile,verificationCode)) {
									resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
									resultData.put(ConstantsLogin.MSG, "短信验证码不正确");
									return resultData;
								}
								resultData.put("userId",adUser.getId());
								return resultData;
							} else {
								resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
								resultData.put(ConstantsLogin.MSG, "员工激活码不正确");
								return resultData;
							}
						} else {
							resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
							resultData.put(ConstantsLogin.MSG, "员工邮箱不正确");
							return resultData;
						}
					} else {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
						resultData.put(ConstantsLogin.MSG, "员工工号不正确");
						return resultData;
					}
				}
			} else {
				//原来不在福特
				AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
				adUserPersonalInfo.setWorkNumber(staffNum);
				adUserPersonalInfo.setGroupId(Long.parseLong(groupId));
				adUserPersonalInfo = adUserPersonalInfoDao.selectPersonByWorknumAndGroup(adUserPersonalInfo);
				if (adUserPersonalInfo != null) {
					AdUser adUserNew = new AdUser();
					adUserNew.setId(adUserPersonalInfo.getId());
					adUserNew = adUserDao.getById(Integer.parseInt(adUserPersonalInfo.getId() +""));
					if (adUserNew != null) {
						if (email.equals(adUserNew.getMailbox())) {
							AdActiveCode adActiveCode = new AdActiveCode();
							adActiveCode.setAdUserId(adUserNew.getId());
							adActiveCode.setIsUsed("0");
							adActiveCode = adActiveCodeDao.getByCondition(adActiveCode);
							if (adActiveCode != null) {

								//短信验证码校验
								if (!checkVerificationCode(mobile,verificationCode)) {
									resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
									resultData.put(ConstantsLogin.MSG, "短信验证码不正确");
									return resultData;
								}

								//更新原来的user
								adUser.setIsActive("2");
								if (StringUtils.isNotBlank(groupId)) {
									adUser.setGroupNum(Long.parseLong(groupId));
								}
								adUser.setMailbox(email);
								adUser.setUpdateDate(new Date());
								adUser.setActiveDate(new Date());
								adUser.setDataSyn(AdUser.DATA_SYN_ON);
								int i = adUserDao.updateByPrimaryKeySelective(adUser);
								if (i == 0) {
									resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
									resultData.put(ConstantsLogin.MSG, "切换单位失败");
									return resultData;
								} else {
									LifeMember lifeMember = lifeMemberDao.findMemberByUsername(adUser.getCardNumber());
									if (lifeMember == null) {
										lifeMember = lifeMemberDao.findMemberByMobile(mobile);
									}
									// A库企业编号
									String groupNum = "";
									if (StringUtils.isNotBlank(groupId) && lifeMember != null) {
										LifeGroup lifeGroup = lifeGroupService.getGroupByGroupId(groupId);
										groupNum = lifeGroup.getId();
										lifeMember.setGroupId(Long.valueOf(groupNum));
										lifeMember.setName(adUser.getName());
										lifeMember.setIsEnabled(2);
										lifeMember.setMobile(mobile);
										lifeMember.setLoginFailureCount(0);
										lifeMember.setModifyDate(new Date());
										lifeMember.setAdId(String.valueOf(adUser.getId()));
										lifeMemberDao.updateActiveStatus(lifeMember);
									}

									//更新老的
									AdUserPersonalInfo adUserPersonalInfoOld = new AdUserPersonalInfo();
									adUserPersonalInfoOld.setId(adUser.getId());
									adUserPersonalInfoOld = adUserPersonalInfoDao.select(adUserPersonalInfoOld);
									if (adUserPersonalInfoOld != null) {
										adUserPersonalInfoOld.setWorkNumber(staffNum);
										adUserPersonalInfoDao.updateWorkNum(adUserPersonalInfoOld);
										//更新的AdUserPersonalInfo工号为空
										adUserPersonalInfo.setWorkNumber("");
										adUserPersonalInfoDao.updateWorkNum(adUserPersonalInfo);
										//对新的user做逻辑删除
										adUserDao.deleteAdUser(adUserNew);
									}

									adActiveCode.setIsUsed("1");
									adActiveCode.setAdUserId(adUser.getId());
									adActiveCode.setUsedDate(new Date());
									adActiveCodeDao.updateByPrimaryKey(adActiveCode);
                                    //更新类型为专属码激活
                                    //数据来源 0:平台导入(白名单) 2：企业口令激活，3：卡激活，4：专属码
                                    logger.info("更新类型为专属码激活:{}",adUser.getTelephone());
                                    userServiceI.updatePersonInfoDataSources(adUser.getTelephone(),4);
                                    resultData.put("userId",adUser.getId());
                                    return resultData;
								}
							} else {
								resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
								resultData.put(ConstantsLogin.MSG, "员工激活码不正确");
								return resultData;
							}
						} else {
							resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
							resultData.put(ConstantsLogin.MSG, "员工邮箱不正确");
							return resultData;
						}
					} else {
						resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
						resultData.put(ConstantsLogin.MSG, "员工工号不正确");
						return resultData;
					}
				} else {
					resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
					resultData.put(ConstantsLogin.MSG, "员工工号不正确");
					return resultData;
				}
			}

			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "该手机号已经被绑定");
			return resultData;
		}
		//员工号与邮箱是否匹配
		//通过员工号查询用户id
		AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
		adUserPersonalInfo.setWorkNumber(staffNum);
		Map<String,Object> adUserPersonalInfoMap = new HashMap<>();
		adUserPersonalInfoMap.put("workNumber",staffNum);
		adUserPersonalInfoMap.put("groupId",groupId);
		adUserPersonalInfo = adUserPersonalInfoDao.selectPersonByCondition(adUserPersonalInfoMap);
		if (adUserPersonalInfo == null) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "员工号不存在");
			return resultData;
		}

		adUser = adUserDao.getById(Integer.parseInt(adUserPersonalInfo.getId()+""));
		if (adUser == null) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "员工用户不存在");
			return resultData;
		}
		if (!email.equals(adUser.getMailbox())) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "员工号和邮箱不匹配");
			return resultData;
		}
		//员工工号是否已经绑定手机号
		if (StringUtils.isNotBlank(adUser.getTelephone())) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "该工号已激活，请输入正确的员工工号");
			return resultData;
		}
		//员工工号与激活码是否匹配
		//查询激活码
		AdActiveCode adActiveCode = new AdActiveCode();
		adActiveCode.setAdUserId(adUser.getId());
		adActiveCode.setIsUsed("0");//未使用
		adActiveCode.setCode(code);
		adActiveCode = adActiveCodeDao.getByCondition(adActiveCode);
		if (adActiveCode == null) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "请输入正确的激活码");
			return resultData;
		}

		//短信验证码校验
		if (!checkVerificationCode(mobile,verificationCode)) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "短信验证码不正确");
			return resultData;
		}

		//绑定手机号
		adUser.setTelephone(mobile);
		adUser.setIsActive("2");
		adUser.setActiveDate(new Date());
		adUser.setUpdateDate(new Date());
		adUser.setDataSyn(AdUser.DATA_SYN_ON);
		int i = adUserDao.updateByPrimaryKeySelective(adUser);
		if (i == 0) {
			resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
			resultData.put(ConstantsLogin.MSG, "用户绑定手机号失败");
			return resultData;
		} else {
			LifeMember lifeMember = lifeMemberDao.findMemberByUsername(adUser.getCardNumber());
			if (lifeMember == null) {
				lifeMember = lifeMemberDao.findMemberByMobile(mobile);
				if (lifeMember == null) {
					adUserServiceI.saveMember(adUser);
				}
			} else {
				if (StringUtils.isNotBlank(groupId)) {
					String groupNum = "";
					LifeGroup lifeGroup = lifeGroupService.getGroupByGroupId(groupId);
					groupNum = lifeGroup.getId();
					lifeMember.setGroupId(Long.valueOf(groupNum));
					lifeMember.setName(adUser.getName());
					lifeMember.setIsEnabled(2);
					lifeMember.setMobile(mobile);
					lifeMember.setLoginFailureCount(0);
					lifeMember.setModifyDate(new Date());
					lifeMember.setAdId(String.valueOf(adUser.getId()));
					lifeMemberDao.updateActiveStatus(lifeMember);
				}
			}
			adActiveCode.setIsUsed("1");
			adActiveCode.setUsedDate(new Date());
			adActiveCodeDao.updateByPrimaryKey(adActiveCode);
		}

        //更新类型为专属码激活
        //数据来源 0:平台导入(白名单) 2：企业口令激活，3：卡激活，4：专属码
        logger.info("更新类型为专属码激活:{}",adUser.getTelephone());
        userServiceI.updatePersonInfoDataSources(adUser.getTelephone(),4);

		resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
		resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.SUCCESS.getMsg());
		resultData.put("userId",adUser.getId());
		return resultData;
	}

    @Override
//    @Transactional
    public JSONObject validateFord201904ShipAddrCollectorUser
            (String mobile, String staffNum, String email, String verificationCode)
            throws Exception{
        JSONObject resultData = new JSONObject();
        resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
        resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.SUCCESS.getMsg());
        
        //确保工号存在
        if(adUserPersonalInfoDao.countPersonsByWorkNumber(staffNum)==0){
            resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
            resultData.put(ConstantsLogin.MSG, "该工号不存在！");
            return resultData;
        }
        
        //确保邮箱与工号匹配
        List<AdUser> usersByMailbox = adUserDao.findByMailbox(email);
        AdUser userMatch = null;
        for (AdUser userByMail : usersByMailbox) {
            AdUserPersonalInfo paramSelectByUserId = new AdUserPersonalInfo();
            paramSelectByUserId.setId(userByMail.getId());
            AdUserPersonalInfo personalInfoByMail = adUserPersonalInfoDao.select(paramSelectByUserId);
            if (StringUtils.equals(personalInfoByMail.getWorkNumber(), staffNum)
                    && StringUtils.equalsIgnoreCase(userByMail.getMailbox(), email)) {
                userMatch = userByMail;
            }
        }
        if (userMatch == null) {
            resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
            resultData.put(ConstantsLogin.MSG, "请输入准确的工号和邮箱");
            return resultData;
        }
        AdUser user = userMatch;
        
        //如果该账户已经绑定手机号
        //    如果已经绑定相同手机号  直接登录
        if(user.getTelephone()!=null && StringUtils.equals(user.getTelephone(),mobile)){
            resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
            resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.SUCCESS.getMsg());
            resultData.put("userId",user.getId());
            return resultData;
        }
        //    否则：已经绑定其他手机号  提示输入正确手机号
        if(user.getTelephone()!=null && !StringUtils.equals(user.getTelephone(),mobile)){
            resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
            resultData.put(ConstantsLogin.MSG, "该工号已绑定手机号，请输入正确的手机号！");
            return resultData;
        }
        
        // from now on, the case is user.getTelephone() == null
        
        //确保手机号没有被绑定
        AdUser userByMobile = new AdUser();
        userByMobile.setTelephone(mobile);
        userByMobile = adUserDao.get(userByMobile);
        if (userByMobile != null) {
            resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
            resultData.put(ConstantsLogin.MSG, "该手机号已是兜礼用户，请输入其他手机号！");
            return resultData;
        }

        //确保短信验证码正确
        if(verificationCode==null||!checkVerificationCode(mobile, verificationCode)){
            resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
            resultData.put(ConstantsLogin.MSG, "请输入正确的验证码");
            return resultData;
        }

        //验证通过，开始更新数据库
        
        //绑定手机号
        user.setTelephone(mobile);
        user.setIsActive("2");
        user.setActiveDate(new Date());
        user.setUpdateDate(new Date());
        user.setDataSyn(AdUser.DATA_SYN_ON);
        if (0==adUserDao.updateByPrimaryKeySelective(user)) {
            resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
            resultData.put(ConstantsLogin.MSG, "用户绑定手机号失败");
            return resultData;
        }
        
        //确保为该用户创建了xx_member记录
        LifeMember lifeMember = lifeMemberDao.findMemberByUsername(user.getCardNumber());
        if (lifeMember == null){
            lifeMember = lifeMemberDao.findMemberByMobile(mobile);
        }
        if (lifeMember == null){
            LifeMember paramFindByAdId = new LifeMember();
            paramFindByAdId.setAdId(user.getId().toString());
            lifeMember = lifeMemberDao.findLifeMemberByAdId(paramFindByAdId);
        }
        //   如果已经创建了xx_member则更新
        if (lifeMember != null){
            lifeMember.setName(user.getName());
            lifeMember.setIsEnabled(2);
            lifeMember.setMobile(mobile);
            lifeMember.setLoginFailureCount(0);
            lifeMember.setModifyDate(new Date());
            lifeMemberDao.updateActiveStatus(lifeMember);
        }
        //   如果没有就新建
        if (lifeMember == null) {
            try {
                adUserServiceI.saveMember(user);
            } catch (Exception e) {
                resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.FAIL.getCode());
                logger.error(String.format("为ad_user{id=%s}创建xx_member失败", user.getId().toString()), e);
                return resultData;
            }
            lifeMember = lifeMemberDao.findMemberByUsername(user.getCardNumber());
        }

        //更新类型为 平台导入(白名单)
        //数据来源 0:平台导入(白名单) 2：企业口令激活，3：卡激活，4：专属码
        logger.info("更新类型为专属码激活:{}",user.getTelephone());
        userServiceI.updatePersonInfoDataSources(user.getTelephone(),0);

        resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
        resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.SUCCESS.getMsg());
        resultData.put("userId",user.getId());
        return resultData;
    }

}
