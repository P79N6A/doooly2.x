package com.doooly.dao.reachlife;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachlife.LifeMember;

public interface LifeMemberDao extends BaseDaoI<LifeMember> {
	/**
	 * 根据会员卡号修改会员信息
	 * 
	 * @param member
	 * @return
	 */
	public int updateMemberActiveStatus(LifeMember member);

	public LifeMember getByAdId(@Param("adId") Long adId);

	int deleteByPrimaryKey(Long id);

	int insert(LifeMember record);

	int insertSelective(LifeMember record);

	LifeMember selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(LifeMember record);

	int updateByPrimaryKeyWithBLOBs(LifeMember record);

	int updateByPrimaryKey(LifeMember record);

	int addMember(LifeMember member);

	/**
	 * 卡号获取用户信息
	 */
	LifeMember findMemberByUsername(String username);

	/**
	 * 更新激活状态
	 * 
	 * @param lifeMember
	 */
	void updateActiveStatus(LifeMember lifeMember);

	/**
	 * 根据手机号查询
	 * 
	 * @param mobile
	 * @return
	 */
	LifeMember findMemberByMobile(String mobile);

	/**
	 * 查询会员信息
	 */
	LifeMember findLifeMemberByAdId(LifeMember lifeMember);

	/**
	 * 删除会员信息
	 */
	void deleteMember(LifeMember lifeMember);

	public Long getLifeGroupId(Long groupNum);

	/**
	 * 保存会员信息-返回主键
	 */
	public int saveMember(LifeMember lifeMember);
}