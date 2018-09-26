package com.doooly.dao.reachad;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdReturnPoints;

/**
 * @Description: 待返积分
 * @author: qing.zhang
 * @date: 2017-05-19
 */
public interface AdReturnPointsDao {

	int getTotalNum(@Param("income") String income, @Param("userId") String userId);

	List<AdReturnPoints> getAdReturnPoints(@Param("income") String income, @Param("userId") String userId,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	AdReturnPoints getAvailablePointDetail(String availablePointsId);

	List<AdReturnPoints> getByUserIds(@Param("familyIdsList") List<String> familyIdsList,
			@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

	Integer getCountByUserIds(@Param("familyIdsList") List<String> familyIdsList);
	/**
	 * 冻结用户预计返利积分
	 * 
	* @author  hutao 
	* @date 创建时间：2018年9月26日 上午9:35:30 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	Integer cancelRebateByUserId(@Param("userId") Long userId);
}
