package com.doooly.business.home.v2.servcie;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.base.BaseResponse;
import com.doooly.dto.coupon.FindExclusiveCouponResponse;
import com.doooly.dto.coupon.GetAdCouponActivityInfosResponse;
import com.doooly.dto.coupon.ReceiveExclusiveCouponRequest;
import com.doooly.dto.coupon.ReceiveExclusiveCouponResponse;
import com.doooly.dto.home.*;

/**
 * Created by 王晨宇 on 2018/2/12.
 */
public interface HomePageDataServcie {
	/**
	 * 获取兜礼APP首页数据
	 */
	GetHomePageDataV2Response getHomePageDataV2(GetHomePageDataV2Request request, GetHomePageDataV2Response response);
	
	/**
	 * 获取兜礼APP首页数据
	 */
	GetHomePageDataV2Response getHomePageDataV2_2(GetHomePageDataV2Request request, GetHomePageDataV2Response response);
	/**
	 * 获取个人中心数据
	 * 
	* @author  hutao 
	* @date 创建时间：2018年11月9日 下午2:08:37 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	GetHomePageDataV2Response getHomePageDataV2_2(GetHomePageDataV2Request request, GetHomePageDataV2Response response);

	/**
	 * 查询用户的新手引导完成进度
	 */
	UserGuideFlowResponse getUserAppGuideFlow(Integer userId, UserGuideFlowResponse response);

	/**
	 * 兜礼APP中，查询专属优惠券
	 */
	FindExclusiveCouponResponse findExclusiveCoupon(String userId, FindExclusiveCouponResponse response);

	/**
	 * 用户领取《专属优惠券》
	 */
	ReceiveExclusiveCouponResponse receiveExclusiveCoupon(ReceiveExclusiveCouponRequest request,
			ReceiveExclusiveCouponResponse response);

	/**
	 * 后端数据库中保存，用户《权益，或积分》新手引导完成
	 */
	BaseResponse insertOrUpdateUserGuideFinish(GuideFinishRequest request, BaseResponse response);

	/**
	 * 在redis中设置专属优惠券活动id
	 */
	BaseResponse setExclusiveCouponActivityId(JSONObject json, BaseResponse response);

	/**
	 * 拉取闪屏页，企业logo等信息
	 */
	GetSplashScreenResponse getSplashScreen(String groupId, GetSplashScreenResponse response);

	/**
	 * 获取优惠券活动表信息
	 */
	GetAdCouponActivityInfosResponse getAdCouponActivityInfos(String activityId,
			GetAdCouponActivityInfosResponse response);
}
