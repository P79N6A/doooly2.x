package com.doooly.publish.rest.life.impl;

import java.util.Date;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.freeCoupon.service.FreeCouponBusinessServiceI;
import com.doooly.business.freeCoupon.service.MyCouponsBusinessServiceI;
import com.doooly.business.touristCard.datacontract.entity.SctcdAccount;
import com.doooly.common.constants.ConstantsV2;
import com.doooly.common.dto.BaseReq;
import com.doooly.dao.reachad.AdBusinessPrivilegeActivityDao;
import com.doooly.dao.reachad.TouristCardDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdBusinessPrivilegeActivity;
import com.doooly.entity.reachad.AdUserBusinessExpansion;
import com.doooly.publish.rest.life.FreeCouponServiceI;

@Component
@Path("/freeCoupon")
public class FreeCouponService implements FreeCouponServiceI {
	@Autowired
	private FreeCouponBusinessServiceI freeCouponBusinessServiceI;
	@Autowired
	private MyCouponsBusinessServiceI myCouponsBusinessServiceI;
	@Autowired
	private TouristCardDao touristCardDao;
	@Autowired
	private AdBusinessPrivilegeActivityDao adBusinessPrivilegeActivityDao;
	@Autowired
	protected StringRedisTemplate redisTemplate;

	private static final Logger logger = LoggerFactory.getLogger(FreeCouponService.class);

	/** 商城直接领取卡券 */
	@POST
	@Path(value = "/receiveCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String receiveCoupon(JSONObject json) {
		// ad_user表主键
		Integer adId = json.getInteger("adId");
		// ad_coupon主键
		Integer couponId = json.getInteger("couponId");
		// ad_coupon_activity主键
		Integer activityId = json.getInteger("activityId");
		// 商品编号
		String productSn = json.getString("productSn");
		MessageDataBean messageDataBean = freeCouponBusinessServiceI.receiveCoupon(adId, couponId, activityId,
				productSn);
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}
	/** 武钢商城直接领取卡券 */
	@POST
	@Path(value = "/forWuGangCouponSend")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String forWuGangCouponSend(JSONObject json) {
		// ad_user表主键
		Integer adId = json.getInteger("adId");
		// ad_coupon主键
		Integer couponId = json.getInteger("couponId");
		// ad_coupon_activity主键
		Integer activityId = json.getInteger("activityId");
		// 商品编号
		String productSn = json.getString("productSn");
		MessageDataBean messageDataBean = freeCouponBusinessServiceI.forWuGangCouponSend(adId, couponId, activityId,
				productSn);
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	/** 获取“0元抢券”首页数据 */
	@POST
	@Path(value = "/grabCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String grabCoupons(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取会员id
			String userId = obj.getString("userId");
			// 获取有效的卡券活动关联
			HashMap<String, Object> map = freeCouponBusinessServiceI.findAvailableCoupons(userId);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取“0元抢券”首页数据异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	/** “0元抢券”保存报名记录 and 获取报名人数 */
	@POST
	@Path(value = "/saveRegisterRecord")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveRegisterRecord(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 保存报名记录
			HashMap<String, Object> map = freeCouponBusinessServiceI.saveRegisterRecord(obj);
			logger.info("保存报名记录 and 获取报名人数返回map:" + map);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("保存报名记录,获取报名人数异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	/** “0元抢券”获取卡券详情 */
	@POST
	@Path(value = "/coupondetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String couponDetail(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 获取卡券详情
			String actConnId = obj.getString("actConnId");
			String userId = obj.getString("userId");
			HashMap<String, Object> map = freeCouponBusinessServiceI.getCouponDetailsByConnId(actConnId,userId);
			logger.info("获取卡券详情返回map:" + map);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取卡券详情异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}


	/**
	 * 回收卡券
	 */
	@POST
	@Path(value = "/recyclingCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String recyclingCoupon(JSONObject obj) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			freeCouponBusinessServiceI.recyclingCoupon();
		} catch (Exception e) {
			logger.error("回收异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/integralActivity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String integralActivity(BaseReq<JSONObject> json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
            logger.info(JSON.toJSONString(json));
			JSONObject jsonObject  = json.getParams();
			Long userId = jsonObject.getLong("userId");
			messageDataBean = freeCouponBusinessServiceI.getIntegralActivityData(userId);
			logger.info(messageDataBean.toJsonString());
			logger.info("新手礼查询时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(ConstantsV2.SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(ConstantsV2.SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}
	@POST
	@Path(value = "/sendIntegralActivity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String sendIntegralActivity(BaseReq<JSONObject> json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			long start = System.currentTimeMillis();
			logger.info(JSON.toJSONString(json));
			JSONObject jsonObject  = json.getParams();
			Long userId = jsonObject.getLong("userId");
			messageDataBean = freeCouponBusinessServiceI.sendIntegralActivity(userId);
			logger.info(messageDataBean.toJsonString());
			logger.info("新手礼领取时间"+ (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(ConstantsV2.SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(ConstantsV2.SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}


	/***
	 *  开启前折后返权限
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/setUserPrivilege")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String setUserPrivilege(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean("1000","成功");
		try {
			logger.info(JSON.toJSONString(json));
			Integer userId = json.getInteger("userId");
			Integer privilegeType = json.getInteger("privilegeType");
			Integer businessType = json.getInteger("businessType");
			if(1 == privilegeType || 2 == privilegeType ) {
				//开启前折后返
				AdBusinessPrivilegeActivity privilege = new AdBusinessPrivilegeActivity();
				privilege.setBusinessId(0);
				privilege.setIsOpenPrivilege(1);
				privilege.setPrivilegeType(privilegeType);
				privilege.setUserId(userId);
				AdBusinessPrivilegeActivity privilegeActivity = adBusinessPrivilegeActivityDao.getUserPrivilege(privilege);
				if(privilegeActivity == null) {
					int rows = adBusinessPrivilegeActivityDao.setUserPrivilege(privilege);
					if (rows == 0) {
						messageDataBean.setCode("1001");
						messageDataBean.setMess("失败");
					}
				}
			}else if(3 == privilegeType && businessType != null){
				//开启特权
				AdUserBusinessExpansion expansion = new AdUserBusinessExpansion();
				expansion.setUserId(String.valueOf(userId));
				JSONObject privilege = null;
				JSONObject jsonObject = null;
				SctcdAccount sctcdAccount = touristCardDao.findByUidBid(expansion);
				String f15 =  null;
				if(sctcdAccount != null && (f15 = sctcdAccount.getF15()) != null){
					jsonObject = JSON.parseObject(f15);
					privilege = jsonObject.getJSONObject("app-index-privilege");
				}else{
					jsonObject = new JSONObject();
					privilege = new JSONObject();
					privilege.put("business",0);
					privilege.put("friends",0);
					privilege.put("integral",0);
					privilege.put("coupon",0);
					privilege.put("birthday",0);
					jsonObject.put("app-index-privilege",privilege);
				}
				//设置权限
				switch (businessType) {
					case 1:
						privilege.put("business",1);
						break;
					case 2:
						privilege.put("friends",1);
						break;
					case 3:
						privilege.put("integral",1);
						break;
					case 4:
						privilege.put("coupon",1);
						break;
					case 5:
						privilege.put("birthday",1);
						break;
					default:
						break;
				}
				int rows = 0;
				if(sctcdAccount != null){
					rows = touristCardDao.updatePrivilege(sctcdAccount.getId(),jsonObject.toJSONString());
				}else{
					AdUserBusinessExpansion bean = new AdUserBusinessExpansion();
					bean.setUserId(String.valueOf(userId));
					bean.setBusinessType("app-guide");
					bean.setDelFlag("0");
					bean.setCreateDate(new Date());
					bean.setF15(jsonObject.toJSONString());
					rows = touristCardDao.addSctcdAccount(bean);
				}
				if (rows == 0) {
					messageDataBean.setCode("1001");
					messageDataBean.setMess("失败");
				}
			}else{
				messageDataBean.setCode("1008");
				messageDataBean.setMess("无效的privilegeType");
			}

		} catch (Exception e) {
			logger.error("setUserPrivilege e = {}",e);
			messageDataBean.setCode(ConstantsV2.SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(ConstantsV2.SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}

	/***
	 *  开启前折后返权限
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/getUserPrivilege")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUserPrivilege(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean("1000","成功");
		try {
			logger.info("params = {}",json.toJSONString());
			Integer userId = json.getInteger("userId");
			Integer privilegeType = json.getInteger("privilegeType");
			AdBusinessPrivilegeActivity privilege = new AdBusinessPrivilegeActivity();
			privilege.setPrivilegeType(privilegeType);
			privilege.setUserId(userId);
			AdBusinessPrivilegeActivity adBusinessPrivilegeActivity = adBusinessPrivilegeActivityDao.getUserPrivilege(privilege);
			if(adBusinessPrivilegeActivity == null){
				messageDataBean.setCode("1001");
				messageDataBean.setMess("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(ConstantsV2.SystemCode.SYSTEM_ERROR.getCode()+"");
			messageDataBean.setMess(ConstantsV2.SystemCode.SYSTEM_ERROR.getMsg());
		}
		return messageDataBean.toJsonString();
	}
	
	/***
	 *  获取活动卡券
	 * @param json
	 * @return
	 */
	@POST
	@Path(value = "/getActivityCoupon")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getActivityCouponS(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			logger.info("params = {}",json.toJSONString());
			String activityId = json.getString("activityId");
			HashMap<String, Object> map = myCouponsBusinessServiceI.getActivityCouponS(activityId);
			logger.info("获取活动卡券返回的map:" + map);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (Exception e) {
			logger.error("获取活动卡券异常！");
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

}
