package com.doooly.dao.reachad;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdGroupActivityConn;

/**
 * 活动企业权限DAO接口
 * 
 * @author qing.zhang
 * @version 2017-04-25
 */
public interface AdGroupActivityConnDao extends BaseDaoI<AdGroupActivityConn> {

	List<AdGroupActivityConn> findGroup(Integer activityId);

	List<AdGroupActivityConn> findSecondGroup(@Param("ids") String ids);

	List<Map> findDepartment(@Param("ids") String ids);
	/** 
	 * 验证会员是否有资格参与活动
	 * 
	* @author  hutao 
	* @date 创建时间：2018年8月23日 下午2:26:11 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return 大于0：有资格 0：无资格
	 */
	int checkUserEligibleActivities(@Param("userId") Integer userId);

	AdGroupActivityConn getRealGIft(@Param("activityId") Integer activityId, @Param("groupNum") Long groupNum);
}