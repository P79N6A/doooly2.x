package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdActiveCode;

/**
 * 激活码表DAO
 * 
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
public interface AdActiveCodeDao {

	/**
	 * 获取未失效的验证码 del_flag = 0 唯一字段 id(激活码ID), ad_user_id(用户ID) , code(激活码),
	 * card_number(会员卡号)
	 * 
	 * @param code
	 * @return
	 */
	public List<AdActiveCode> get(AdActiveCode code);

	/**
	 * 修改激活码使用状态
	 * 
	 * @param code
	 * @return
	 */
	public int updateUseStatus(AdActiveCode code);

	int deleteByPrimaryKey(Long id);

	int insert(AdActiveCode record);

	int insertSelective(AdActiveCode record);

	AdActiveCode selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(AdActiveCode record);

	int updateByPrimaryKey(AdActiveCode record);

	/**
	 * 获取激活码用户信息
	 */
	public AdActiveCode findCodeStatusByCode(String code);
	
	/**
	 * 更新激活码信息
	 * */
	void updateCodeIsUsed(String adActiveCodeId);

	/**
	 * 查询单个
	 * @param adActiveCode
	 * @return
	 */
	AdActiveCode getByCondition(AdActiveCode adActiveCode);
	
	/**
	 * 更新用户ID
	 * */
	void updateUserId(@Param("oldId") String oldId, @Param("newId") String newId);
}