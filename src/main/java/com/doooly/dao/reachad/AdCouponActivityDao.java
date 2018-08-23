package com.doooly.dao.reachad;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdCouponActivity;

public interface AdCouponActivityDao extends BaseDaoI<AdCouponActivity> {
	// 热门活动
	List<Map<String, String>> getHotActivity(@Param("client") int client, @Param("groupId") int groupId,
	                                         @Param("startIndex") int currentPage, @Param("pageSize") int pageSize,
	                                         @Param("categoryId") String categoryId, @Param("isRecommendation") String isRecommendation);

	int getHotActivityCnt(@Param("client") int client, @Param("groupId") int groupId,
	                                        @Param("categoryId") String categoryId, @Param("isRecommendation") String isRecommendation);

	// 关闭活动
	void closeActivity(@Param("id") int id);

	AdCouponActivity getActivityDetail(Integer activityId);

	void updateBrowserCount(Integer activityId);

	void updateVoteCount(Integer activityId);

	Integer getActivityUserCount(Integer activityId);

	List<AdCouponActivity> getBenefitsActivityList(Integer userId);

	List<Map<String, Object>> getNowActivityCoupon();

	AdCouponActivity getActivityIdByIdFlag(String idFlag);
	/**
	 * 验证活动是否在有效期
	 * 
	* @author  hutao 
	* @date 创建时间：2018年8月23日 下午2:41:40 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	int checkActivityValid(Integer id);

}