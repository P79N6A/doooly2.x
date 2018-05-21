package com.doooly.business.activity.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.activity.ActivityServiceI;
import com.doooly.business.activity.ShareRecordServiceI;
import com.doooly.business.common.service.AdSendGiftServiceI;
import com.doooly.business.common.service.impl.AdUserService;
import com.doooly.business.freeCoupon.service.Impl.FreeCouponBusinessService;
import com.doooly.business.utils.Pagelab;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.constants.ConstantsV2.ActivityCode;
import com.doooly.common.constants.ConstantsV2.MemberCode;
import com.doooly.common.constants.ConstantsV2.OrderCode;
import com.doooly.common.constants.ConstantsV2.SystemCode;
import com.doooly.dao.reachad.AdActivityCategoryDao;
import com.doooly.dao.reachad.AdBasicTypeDao;
import com.doooly.dao.reachad.AdBusinessActivityOrderDao;
import com.doooly.dao.reachad.AdBusinessActivityRuleDao;
import com.doooly.dao.reachad.AdCouponActivityDao;
import com.doooly.dao.reachad.AdVoteRecordDao;
import com.doooly.dao.reachlife.LifeMemberDao;
import com.doooly.dto.activity.ActivityOrderReq;
import com.doooly.dto.activity.ActivityOrderRes;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdActivityCategory;
import com.doooly.entity.reachad.AdBusinessActivityInfo;
import com.doooly.entity.reachad.AdBusinessActivityOrder;
import com.doooly.entity.reachad.AdBusinessActivityRule;
import com.doooly.entity.reachad.AdBusinessActivityRule.RuleType;
import com.doooly.entity.reachad.AdCouponActivity;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdVoteRecord;
import com.doooly.entity.reachlife.LifeMember;

@Service
public class ShareRecordService implements ShareRecordServiceI {
	
	private static Logger logger = Logger.getLogger(ShareRecordService.class);
	private static final String ACTIVITY_ID1  = PropertiesConstants.dooolyBundle.getString("bring_coolness_activity_id1");
	private static final String ACTIVITY_ID2  = PropertiesConstants.dooolyBundle.getString("bring_coolness_activity_id2");
	private static final String ACTIVITY_ID3  = PropertiesConstants.dooolyBundle.getString("bring_coolness_activity_id3");
	private static final String ACTIVITY_COUPON_ID  = PropertiesConstants.dooolyBundle.getString("bring_coolness_coupon_id");
	@Autowired
	private AdVoteRecordDao adVoteRecordDao;
	@Autowired
	private AdUserService adUserService;
	@Autowired
	private FreeCouponBusinessService freeCouponBusinessService;
	
	@Override
	@Transactional
	public MessageDataBean isSetShareRecord(String userId, String telephone) {
		// TODO Auto-generated method stub
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			//判断该telephone是否在数据库中
			AdUser adUser = adUserService.get(telephone, null);
			if (adUser!=null) {
				//ad_vote_record表中,state为0代表未被使用,1代表已被使用过,activity_id使用3
				//获取所有telephone的记录,遍历,判断是否有该userId,或者state为1的数据
				if (adUser.getId().toString().equals(userId)) {
					//输入的手机号是本人的手机号
					messageDataBean.setCode(ActivityCode.NOT_STARTED.getCode()+"");
					return messageDataBean;
				}
				int used = 0;
				int isUserSet = 0;
				List<AdVoteRecord> records = adVoteRecordDao.findByTelephoneAndActivityId(telephone,"3");
				if (!records.isEmpty()) {
					for (AdVoteRecord adVoteRecord : records) {
						if (StringUtils.isNotBlank(adVoteRecord.getUserWechatOpenId())&&userId.equals(adVoteRecord.getUserWechatOpenId())) {
							//判断该userId是否已经邀请过该telephone
							isUserSet=1;
							break;
						}else if (adVoteRecord.getState()!='\0'&&adVoteRecord.getState()=='1') {
							//判断ad_vote_record中telephone,判断该号码是否被使用
							used=1;
							break;
						}
					}
					if (used == 1) {
						messageDataBean.setCode(ActivityCode.HAD_ALREADY.getCode()+"");
					}else if (isUserSet ==1) {
						messageDataBean.setCode(ActivityCode.HAD_ALREADY.getCode()+"");
					}else {
						//添加记录
						addActivityShareRecord(userId, telephone);
						messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
					}
				}else {
					//添加记录
					addActivityShareRecord(userId, telephone);
					messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
				}
			}else {
				//此号码不在数据库中
				messageDataBean.setCode(MemberCode.NOT_EXIST.getCode()+"");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
		}
		return messageDataBean;
	}

	private void addActivityShareRecord(String userId, String telephone) {
		AdVoteRecord newRecord =  new AdVoteRecord();
		newRecord.setMobile(telephone);
		newRecord.setState('0');
		newRecord.setActivityId(3);
		newRecord.setCreateDate(new Date());
		newRecord.setUserWechatOpenId(userId);
		adVoteRecordDao.insert(newRecord);
	}

	@Override
	@Transactional
	public MessageDataBean sendByShareRecord(String userId, String telephone, String telephoneUserId) {
		// TODO Auto-generated method stub
		MessageDataBean messageDataBean= new MessageDataBean();
		try {
			AdUser adUser = adUserService.getById(telephoneUserId);
			if (adUser!=null) {
				//ad_vote_record表中,state为0代表未被使用,1代表已被使用过,activity_id使用3
				//获取所有telephone的记录,遍历,判断是否有该userId,或者state为1的数据
				if (StringUtils.isNotBlank(adUser.getTelephone())&&!adUser.getTelephone().equals(telephone)) {
					//当前进入用户并不是邀请输入的手机号
					logger.info("当前用户为:"+adUser.getTelephone()+",分享手机号为:"+telephone);
					messageDataBean.setCode(ActivityCode.NOT_STARTED.getCode()+"");
					return messageDataBean;
				}
			}
			//判断该telephone是否被使用
			List<AdVoteRecord> records = adVoteRecordDao.findUsedRecordByTelephoneAndActivityId(telephone,"3","1");
			if (records.isEmpty()) {
				//判断该userId是否已经获取三次
				int userRecordsSize = adVoteRecordDao.getByUserIdAndActivityId(userId, "3","1");
				if (userRecordsSize < 3) {
					//发券
					switch (userRecordsSize) {
					case 0:
						messageDataBean = freeCouponBusinessService.receiveCoupon(Integer.valueOf(userId), 
								Integer.valueOf(ACTIVITY_COUPON_ID), 
								Integer.valueOf(ACTIVITY_ID1), null);
						break;
					case 1:
						messageDataBean = freeCouponBusinessService.receiveCoupon(Integer.valueOf(userId), 
								Integer.valueOf(ACTIVITY_COUPON_ID), 
								Integer.valueOf(ACTIVITY_ID2), null);
						break;
					case 2:
						messageDataBean = freeCouponBusinessService.receiveCoupon(Integer.valueOf(userId), 
								Integer.valueOf(ACTIVITY_COUPON_ID), 
								Integer.valueOf(ACTIVITY_ID3), null);
						break;
					default:
						break;
					}
					if (messageDataBean.getCode().equals(MessageDataBean.success_code)) {
						//更新记录状态
						adVoteRecordDao.updateShareRecord(userId, telephone);
					}
//					messageDataBean.setCode(SystemCode.SUCCESS.getCode()+"");
				}else {
					messageDataBean.setCode(OrderCode.SELL_OUT.getCode()+"");
				}
			}else{
				messageDataBean.setCode(ActivityCode.HAD_ALREADY.getCode()+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(SystemCode.SYSTEM_ERROR.getCode()+"");
		}
		return messageDataBean;
	}

}
