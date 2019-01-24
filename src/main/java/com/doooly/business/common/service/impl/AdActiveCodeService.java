package com.doooly.business.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdActiveCodeServiceI;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.reachLife.LifeGroupService;
import com.doooly.business.user.service.UserServiceI;
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
	public JSONObject validateFordUser(String code, String mobile, String staffNum, String email, String groupId) throws Exception {
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
									}

									adActiveCode.setIsUsed("1");
									adActiveCode.setAdUserId(adUser.getId());
									adActiveCode.setUsedDate(new Date());
									adActiveCodeDao.updateByPrimaryKey(adActiveCode);
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
        logger.info("更新类型为专属码激活:{}",adUser.getTelephone());
        userServiceI.updatePersonInfoDataSources(adUser.getTelephone(),4);

		resultData.put(ConstantsLogin.CODE, ConstantsLogin.CodeActive.SUCCESS.getCode());
		resultData.put(ConstantsLogin.MSG, ConstantsLogin.CodeActive.SUCCESS.getMsg());
		resultData.put("userId",adUser.getId());
		return resultData;
	}

}
