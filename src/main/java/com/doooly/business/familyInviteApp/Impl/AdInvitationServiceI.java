package com.doooly.business.familyInviteApp.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.impl.AdUserService;
import com.doooly.business.familyInviteApp.AdInvitationService;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdInvitationDao;
import com.doooly.dao.reachad.AdInvitationRecordDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdUserPersonalInfoDao;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdInvitation;
import com.doooly.entity.reachad.AdInvitationRecord;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserPersonalInfo;
@Service
public class AdInvitationServiceI implements AdInvitationService {

	private static final String FAMILY_FLAG = "1";
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdGroupDao adGroupDao;

	@Autowired
	private AdUserService adUserService;
	@Autowired
	private AdInvitationRecordDao adInvitationRecordDao;
	@Autowired
	private AdInvitationDao adInvitationDao;
	@Autowired
	private AdUserPersonalInfoDao adUserPersonalInfoDao;
	
//	//家属邀请赠送卡券的活动Id
//	private static final String ACTIVITY_ID = ResourceBundle.getBundle("familyInvitation").getString("activity_id");
//	//家属邀请赠送卡券的id
//	private static final String COUPON_ID = ResourceBundle.getBundle("familyInvitation").getString("coupon_id");

	/**
	 * 获取邀请人详细信息
	 * 
	 * @param jsonReq
	 * @return
	 */
	ReentrantLock lock = new ReentrantLock();

	@Transactional(readOnly = false)
	public JSONObject getInvitationDetail(JSONObject jsonReq) {
		JSONObject res = new JSONObject();
		try {
			Integer userId = jsonReq.getInteger("userId");
			// 获取邀请人会员卡号、企业编号
			AdUser adUser = adUserDao.getById(String.valueOf(userId));
			Integer invitationType = jsonReq.getInteger("invitationType");
			// 生成邀请码（使用会员ID，长度为6位，不足在左边以0填充）
			AdInvitation adInvitation = new AdInvitation();
			try {
				lock.lock();
				adInvitation = this.getByUserIdInvitationType(Integer.valueOf(userId), invitationType);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			logger.info("===adInvitation:" + adInvitation + "获取邀请人详细信息,userId = " + userId + ",invitationType="
					+ invitationType);
			// 若邀请信息不存在，需要重新生成邀请信息
			if (adInvitation == null) {
				String invitationCode = invitationType == AdInvitation.MEM_INVITATION_TYPE ? adUser.getCardNumber()
						: StringUtils.leftPad(invitationType + String.valueOf(userId), 6, "0");
				Integer invitationNum = jsonReq.getInteger("invitationMaxNum");
				String giftList = jsonReq.getString("giftList");
				Integer giftNum = jsonReq.getInteger("giftNum");
				// Date validationTime = jsonReq.getDate("validationTime");
				Integer invitationMinNum = jsonReq.getInteger("invitationMinNum");
				String flag = jsonReq.getString("flag");
				adInvitation = new AdInvitation(userId, invitationCode, invitationType, invitationNum, invitationNum,
						invitationMinNum, giftNum, giftList,flag);
				// 保存邀请人详细信息
				adInvitationDao.insert(adInvitation);
			} else {
				// List<String> telephoneList =
				// this.getInvitationListByUserId(userId,invitationType,adInvitation.getCreateDate());
				// res.put("InvitationFamilyList", telephoneList);
				// if(StringUtils.isNotBlank(adInvitation.getGiftLists()))
				// res.put("giftList", adInvitation.getGiftLists());
				// 获取邀请人的邀请记录 && 更新邀请记录中的积分(不充值积分)
				String flag = jsonReq.getString("flag");
				List<AdInvitationRecord> recordList = this.getRecordsAndUpdatePoint(userId, flag);
				logger.info("已存在邀请信息：" + recordList);
				res.put("InvitationFamilyList", recordList);
			}
			// 组装邀请信息并返回json格式数据
			// res.put("groupId",adUser.getGroupNum());
			// res.put("cardNo", adUser.getCardNumber());
			res.put("InvitationCode", adInvitation.getInvitationCode());
			res.put("InvitationMaxNum", adInvitation.getInvitationMaxNum());
			res.put("InvitationAvailNum", adInvitation.getInvitationAvail());
			// res.put("giftNum", adInvitation.getGiftNum());
			res.put("giftState", adInvitation.getGiftState());
			res.put("invitationMinNum", adInvitation.getInvitationMinNum());
			res.put("invitationType", invitationType);
			// 计算剩余邀请 时间（毫秒->秒）
//			if (adInvitation.getValidationTime() != null)
//				res.put("validationTime",
//						(adInvitation.getValidationTime().getTime() - System.currentTimeMillis()) / 1000);
			res.put("userId", userId);
			res.put("full", adInvitation.getInvitationAvail() > 0 ? false : true);
			logger.info("邀请码：" + adInvitation.getInvitationCode() + ",剩余邀请人数：" + adInvitation.getInvitationAvail()
					);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	/**
	 * 根据会员ID查询邀请码相关信息
	 * 
	 * @param userId
	 * @return
	 */
	public AdInvitation getByUserIdInvitationType(Integer userId, Integer invitationType) {
		return adInvitationDao.getByUserIdInvitationType(userId, invitationType);
	}
	


	/**
	 * 获取邀请人的邀请记录 && 更新邀请记录中的积分(不充值积分)
	 * 
	 * @param userId
	 * 
	 */
	public List<AdInvitationRecord> getRecordsAndUpdatePoint(Integer userId, String flag) {
		// 获取邀请人的邀请记录(该邀请人的全部邀请记录)
		List<AdInvitationRecord> allRecordList = new ArrayList<AdInvitationRecord>();
		try {
			allRecordList = adInvitationRecordDao.findAllRecordListByInviterId(userId, flag);
			logger.info("邀请记录：" + allRecordList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allRecordList;
	}
	
	/**
	 * 通过一个用户的Id查询到关联的家属邀请的所有用户的手机和id
	 * @param data
	 * @return
	 */
	public JSONObject getFamilys(JSONObject data) {
		JSONObject res = new JSONObject();
		try {
			List<String> userList = new ArrayList<String>();
			List<String> mobiles = new ArrayList<String>();
			String userId = data.getString("userId");
			AdInvitation invitation = adInvitationDao.getByUserIdInvitationType(Integer.valueOf(userId), 1);
//			List<AdInvitationRecord> adInvitationRecordList = adInvitationRecordDao.getFamilys(userId);
//			if(adInvitationRecordList.size()==0){
//				userList.add(userId);
//			}else{
//				AdInvitationRecord adInvitationRecord2 = adInvitationRecordList.get(0);
//				int inviterId = adInvitationRecord2.getInviterId();
//				userList.add(String.valueOf(inviterId));
//				List<AdInvitationRecord> adInvitationRecordList2 = adInvitationRecordDao.getFamilys(inviterId+"");
//				for (AdInvitationRecord adInvitationRecord : adInvitationRecordList2) {
//					userList.add(adInvitationRecord.getInviteeId() + "");
//				}
//			}
//			List<AdUser> adUserList = adUserDao.getFamilysByids(userList);
//			if(adUserList!=null){
//				List<String> mobiles = new ArrayList<String>();
//				for (AdUser adUser : adUserList) {
//					mobiles.add(adUser.getTelephone());
//				}
			
			List<AdInvitationRecord> familys = adInvitationRecordDao.getFamilys(userId);
			if(familys.size()==0){
				userList.add(userId);
				List<AdUser> adUserList = adUserDao.getFamilysByids(userList);
				for (AdUser adUser : adUserList) {
					mobiles.add(adUser.getTelephone());
				}
				logger.info("==================无邀请记录mobiles:" +mobiles+"===========");
				
			}
			else{
				for (AdInvitationRecord adInvitationRecord : familys) {
					mobiles.add(adInvitationRecord.getTelephone());
					userList.add(adInvitationRecord.getUserId());
				}
				logger.info("==================有邀请记录mobiles:" +mobiles+"===========");
			}
			res.put("mobiles", mobiles);
			res.put("familyIds", userList);
			res.put("invitation", invitation);
			res.put("code", "0");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res.put("msg", "服务器异常");
			res.put("code", "1");
			return res;
		}
	}
	
	
	public JSONObject getGroupInfo(String userId) {
		JSONObject resJson = new JSONObject();
		try {
			AdGroup adGroup = adGroupDao.findGroupByUserId(userId);
			if(adGroup!=null){
				resJson.put("code", "1000");
				resJson.put("adGroup", adGroup);
				return resJson;
			}else{
				resJson.put("code", "1001");
				resJson.put("desc", "用户没有查询到关联企业");
				return resJson;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resJson.put("code", "4000");
			resJson.put("desc", "服务器异常");
		}
		return resJson;
	}


	public JSONObject saveUserNotActive(String famMobile,
			String invitationCode, Integer invitationType, String famPassword,
			String name,String channel) {
		JSONObject resJson = new JSONObject();
		try {
			// 激活家属前获取invitation，判断是否有礼品
			AdInvitation invitation = new AdInvitation();
			invitation.setInvitationCode(invitationCode);
			invitation.setInvitationType(invitationType);
			invitation = adInvitationDao.getByInvitationCodeAndType(invitation);
			logger.info("激活家属前 获取 invitation = " + invitation);
			//if (invitation == null || StringUtils.isBlank(invitation.getGiftLists())) {
			if (invitation == null) {
				resJson.put("code", 8);
				resJson.put("desc", "很遗憾，你错过了好礼!");
				return resJson;
			}

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("famMobile", famMobile);
			paramMap.put("invitationCode", invitationCode);
			paramMap.put("invitationType", String.valueOf(invitation.getInvitationType()));
			paramMap.put("famPassword", famPassword);
			paramMap.put("name", name);
			
			// 调用函数注册家属会员并激活
			Integer code = adInvitationDao.saveUserNotActive(paramMap);
			if (code == 1) {
				resJson.put("code", code);
				resJson.put("desc", "抱歉，邀请人数已达上线，您来晚了");
				return resJson;
			} else if (code == 2) {
				resJson.put("code", code);
				resJson.put("desc", "手机号已经被激活");
				return resJson;
			} else if (code == 3) {
				resJson.put("code", code);
				resJson.put("desc", "邀请码错误，请重新填写正确的邀请码");
				return resJson;
			} else if (code == 5) {
				resJson.put("code", code);
				resJson.put("desc", "新分配的会员卡号与已有会员卡号重复");
				return resJson;
			} else if (code == 7) {
				resJson.put("code", code);
				resJson.put("desc", "家属会员注册失败");
				return resJson;
			} else if (code == -1) {
				resJson.put("code", code);
				resJson.put("desc", "家属激活异常，请再试");
				return resJson;
			} else if (code == -2) {
				resJson.put("code", code);
				resJson.put("desc", "注册家属会员异常");
				return resJson;
			}
			else if(code == -3){
				resJson.put("code", code);
				resJson.put("desc", "手机号已经存在，无法激活");
				return resJson;
			}
			AdUser famUser = new AdUser();
			famUser.setTelephone(famMobile);
			// 获取家属会员信息
			famUser = adUserDao.getUser(famUser);
			// 同步家属会员信息到A系统
			try {
				boolean syncUser = adUserService.syncUserASystem(famUser);
				if (!syncUser) {
					String tip = "会员同步失败";
					logger.error("famMobile=" + famMobile + ", " + tip);
					resJson.put("code", code);
					resJson.put("desc", "会员邀请家属激活异常，请再试");
					return resJson;
				}
			} catch (Exception e) {
				logger.error("会员同步失败, famMobile=" + famMobile + ", error:", e);
				e.printStackTrace();
				resJson.put("code", code);
				resJson.put("desc", "会员邀请家属激活异常，请再试");
				return resJson;
			}
			//新增会员附属表数据
			AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
			adUserPersonalInfo.setId(famUser.getId());
			adUserPersonalInfo.setAuthFlag("0");;
			adUserPersonalInfoDao.insert(adUserPersonalInfo);
			//邀请记录表新增记录
			int result = this.sendCodeAndSaveRecord(famUser, invitation, channel);
			if(result==1){
				resJson.put("code", 9);
				resJson.put("desc", "账号激活异常");
			}else{
				resJson.put("code", 1000);
				resJson.put("desc", "恭喜，您的账户激活成功");
			}
			
		} catch (Exception e) {
			resJson.put("code", 8);
			resJson.put("desc", "很遗憾，你错过了好礼!");
			e.printStackTrace();
		}
		return resJson;
	}
	
	
	/**
	 * 给家属发放固定卡券 && 邀请记录表新增记录
	 * @param code
	 * @param famUser
	 * @param invitation
	 */
	private int sendCodeAndSaveRecord(AdUser famUser,
			AdInvitation invitation,String channel) {
		Integer userId = invitation.getUserId();
		//获取家属邀请的最新的活动卡券
//		AdCouponActivityConn adCouponActivityConn= adCouponActivityConnService.findNewFamilyActivity();
//		String couponId = adCouponActivityConn.getCouponId()+"";
//		String activityId = adCouponActivityConn.getActivityId()+"";
//		//先去卡券池中获取一张卡券
//		String businessId = adCouponService.getBussinessById(couponId).getBusinessId();
//		JSONObject data = new JSONObject();
//		data.put("codeKey", businessId + "+" + couponId + "+" + activityId);
//		data.put("codeCount", 1);
//		// 把企业卡券的剩余卡券查询出来
//		String result = HttpUtils.httpPost(BASE_URL + "redis/popDataFromRedis",
//				data.toJSONString());
//		JSONObject resultJson = JSONObject.parseObject(result);
//		String str = (String) resultJson.get("codeList");
//		if (str.equals("null")) {
//			return 1;
//		}
//		List<String> codelist = JSONObject.parseArray(str, String.class);
//		String code = codelist.get(0);
//		//把券码放入到被邀请人的卡券中
//		adCouponCodeDao.updateUserByActivity(famUser.getId(), code, activityId);
		
		//邀请记录表新增记录
		AdInvitationRecord adInvitationRecord = new AdInvitationRecord();
		adInvitationRecord.setCode(null);
		adInvitationRecord.setInviteeId(famUser.getId().intValue());
		adInvitationRecord.setInviterId(userId);
		adInvitationRecord.setCreateDate(new Date());// 创建时间
		adInvitationRecord.setUpdateDate(new Date());// 更新时间
		adInvitationRecord.setIsNewRecord(true);
		adInvitationRecord.setFlag(invitation.getFlag());
		adInvitationRecord.setChannel(channel);
		adInvitationRecordDao.insert(adInvitationRecord);
		return 0;
		
	}


	public JSONObject getMyFamilyInfo(String userId) {
		JSONObject resJson = new JSONObject();
		try {
			List<Map> adUserList = adUserDao.getMyFamilyInfo(userId);
			if(adUserList!=null){
				resJson.put("code", "1000");
				resJson.put("adUserList", adUserList);
				return resJson;
			}else{
				resJson.put("code", "1001");
				resJson.put("desc", "用户没有查询到家属信息");
				return resJson;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resJson.put("code", "4000");
			resJson.put("desc", "服务器异常");
		}
		return resJson;
	}

}
