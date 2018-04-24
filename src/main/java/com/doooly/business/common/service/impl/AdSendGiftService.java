package com.doooly.business.common.service.impl;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdActivityGiftServiceI;
import com.doooly.business.common.service.AdBusinessActivityOrderServiceI;
import com.doooly.business.common.service.AdCouponCodeServiceI;
import com.doooly.business.common.service.AdSendGiftServiceI;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.business.common.service.LifeProductServiceI;
import com.doooly.common.constants.ThirdPartySMSConstatns;
import com.doooly.dto.activity.ActivityOrderRes;
import com.doooly.entity.reachad.AdActivityGift;
import com.doooly.entity.reachad.AdBusinessActivityOrder;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachlife.LifeMember;
import com.doooly.entity.reachlife.LifeProduct;

/**
 * 
 * @author lxl
 */
@Service
public class AdSendGiftService implements AdSendGiftServiceI {

	/** 日志 */
	private Log logger = LogFactory.getLog(this.getClass());

	/** 优惠码Service */
	@Autowired
	private AdCouponCodeServiceI adCouponCodeServiceI;

	/** 活动礼品Service */
	@Autowired
	private AdActivityGiftServiceI adActivityGiftServiceI;

	/** 商品Service */
	@Autowired
	private LifeProductServiceI lifeProductServiceI;

	/** 商家活动订单Service */
	@Autowired
	private AdBusinessActivityOrderServiceI adBusinessActivityOrderServiceI;

	/** 商家活动订单Service */
	@Autowired
	private AdUserServiceI adUserServiceI;

	@Override
	@Transactional
	public ActivityOrderRes sendGift(AdBusinessActivityOrder adBusinessActivityOrder) {
		try {
			logger.info("=======参数解析：" + JSONObject.toJSONString(adBusinessActivityOrder));

			// ========礼品类数据(参数：商家编号)========
			AdActivityGift adActivityGift = new AdActivityGift();
			adActivityGift.setBusinessId(adBusinessActivityOrder.getBusinessId());
			adActivityGift = adActivityGiftServiceI.getBusinessActivityGift(adActivityGift);
			logger.info("=======商家活动礼品数据：" + JSONObject.toJSONString(adActivityGift));
			// 商家活动礼品验证
			if (adActivityGift == null) {
				return new ActivityOrderRes("1009", "未配置商家活动礼品,adActivityGift:" + adActivityGift);
			}
			// ========商品类数据(参数：商品ID)========
			LifeProduct lifeProduct = lifeProductServiceI.get(adActivityGift.getProductId().toString());

			// ========会员类信息(参数：卡号)==========
			LifeMember lifeMember = new LifeMember();
			lifeMember.setId(adBusinessActivityOrder.getMemberId());
			lifeMember.setAdId(adBusinessActivityOrder.getUserId().toString());
			lifeMember.setMobile(adBusinessActivityOrder.getTelephone());
			logger.info("======商品信息-lifeProduct：" + JSONObject.toJSONString(lifeProduct) + ",会员信息-lifeMember："
					+ JSONObject.toJSONString(lifeMember));

			if (adActivityGift.getNumber() > 0) {
				// ==============================发放指定数量兑换码start============================
				for (int i = 0; i < adActivityGift.getNumber(); i++) {
					// =================发放优惠码start=================
					HashMap<String, Object> couponCodeMap = adCouponCodeServiceI.giveCouponCode(lifeProduct,
							lifeMember);
					// =================发放优惠码end===================

					// =================存储订单记录start===============
					if (couponCodeMap.get("code").equals("1000")) {
						// 优惠码插入
						adBusinessActivityOrder.setCouponCode(couponCodeMap.get("couponCode").toString());
						// 执行保存
						adBusinessActivityOrderServiceI.insert(adBusinessActivityOrder);
					} else {
						logger.info("==========发放优惠码失败,couponCodeMap：" + couponCodeMap);
						return new ActivityOrderRes(couponCodeMap.get("code").toString(),
								couponCodeMap.get("msg").toString());
					}
					// =================存储订单记录end=================
				}
				// ==============================发放指定数量兑换码end==============================

				// =================发放短信start==================
				String mobiles = lifeMember.getMobile();// 手机号
				Boolean alidayuFlag = true;// ThirdPartySMSConstatns.SMSInfoConfig.luosen_alidayuFlag;//
											// 切换开关 true：使用阿里大鱼平台 false:嘉怡短信平台
				AdUser user = new AdUser(); // 获取用户手机号
				String alidayuSmsCode = "SMS_35060032";// ThirdPartySMSConstatns.SMSInfoConfig.luosen_alidayuSmsCode;
														// // 罗森活动ID
				JSONObject paramSMSJSON = new JSONObject();// 内容参数
				String smsContent = "【兜礼】亲，您的蔬菜礼盒兑换码已到账！请尽快进入兜礼微信号，至“我的福利”领取，先到“鲜”得哟！";// ThirdPartySMSConstatns.SMSInfoConfig.luosen_smsContent;
																						// //
																						// 短信模板
				adUserServiceI.batchSendSms(user, paramSMSJSON, mobiles, alidayuSmsCode, smsContent, alidayuFlag);
				// =================发送短信end====================
			} else {
				return new ActivityOrderRes("1009", "未找到商家对应的礼品number:" + adActivityGift.getNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ActivityOrderRes("5000", "系统错误");
		}
		logger.info("=====返回信息：操作成功=========");
		return new ActivityOrderRes("1000", "操作成功");
	}
}
