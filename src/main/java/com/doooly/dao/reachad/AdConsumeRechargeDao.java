package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdConsumeRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 活动类目，类型
 * 
 */
public interface AdConsumeRechargeDao extends BaseDaoI<AdConsumeRecharge> {

	List<AdConsumeRecharge> getConsumeRecharges(@Param("templateId") int templateId, @Param("floorId") int floorId);
	/**
	 * 
	* @author  hutao 
	* @date 创建时间：2018年11月1日 下午6:33:23 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return
	 */
	List<AdConsumeRecharge> getConsumeRechargesByFloors(@Param("templateId") int templateId,
			@Param("floorIdList") List<Integer> floorIdList);

}
