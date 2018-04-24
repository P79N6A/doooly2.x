package com.doooly.dao.reachad;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdFamilyUser;

/**
 * @Description: 家属关系表
 * @author: qing.zhang
 * @date: 2017-07-25
 */
public interface AdFamilyUserDao {

	AdFamilyUser getMyFamily(Integer userId);

	void insert(AdFamilyUser adFamilyUser);

	List<Map> getMyFamilyList(@Param("userId") Integer userId);

	void updateFamilyUser(@Param("userId") Integer userId, @Param("pointShareSwitch") Integer pointShareSwitch);

	/**
	 * 更新家属关系(该家属与员工的家庭关系)
	 * 
	 * @param userId
	 *            用户id
	 * @param familyRelation
	 *            '与员工家庭关系 1：夫妻 2：子女 3：父母 4：亲戚 5：朋友 6：其他'
	 */
	void updateFamilyRelation(@Param("userId") String userId, @Param("familyRelation") String familyRelation);

}
