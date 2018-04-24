package com.doooly.dao.reachad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.persistence.CrudDao;
import com.doooly.common.persistence.annotation.MyBatisDao;
import com.doooly.entity.reachad.AdInvitation;

public interface AdInvitationDao extends CrudDao<AdInvitation> {
	/**
	 * 根据User更新可邀请人数，基于当前可用人数-1
	 * 
	 * @param userId
	 * @return
	 */
	int updateByUserId(Integer userId);

	/**
	 * 根据会员ID查询邀请码相关信息
	 * 
	 * @param userId
	 * @return
	 */
	AdInvitation getByUserIdInvitationType(@Param("userId") Integer userId,
			@Param("invitationType") Integer invitationType);

	/**
	 * 根据会员ID获取已邀请人数手机号列表
	 * 
	 * @param userId
	 * @return
	 */
	List<String> getInvitationListByUserId(@Param("userId") Integer userId,
			@Param("invitationType") Integer invitationType, @Param("userType") Integer userType,
			@Param("startDate") Date startDate);

	/**
	 * 保存并激活家属会员
	 * 
	 * @param paramMap
	 * @return
	 */
	Integer saveAndActivationUser(Map<String, String> paramMap);

	/**
	 * 保存邀请员工已选定的赠送礼品
	 * 
	 * @param invitation
	 * @return
	 */
	int saveInvitationSelectedGifts(AdInvitation invitation);

	/**
	 * 根据唯一邀请码和邀请类型查询邀请相关详细信息
	 * 
	 * @param invitation
	 * @return
	 */
	AdInvitation getByInvitationCodeAndType(AdInvitation invitation);

	/**
	 * 更新会员礼品状态
	 * 
	 * @param invitation
	 * @return
	 */
	int updateInvitationGiftsState(AdInvitation invitation);

	/**
	 * 查询是否已经在邀请表中存在相同的邀请类型信息
	 * 
	 * @param invitation
	 * 
	 * @return
	 */
	AdInvitation findByTypeAndTelephone(AdInvitation adInvitation);
	
	/**
	 * 通过邀请码查询出会员信息
	 * @param invitationCode
	 * @return
	 */
	AdInvitation findInvitationByCode(@Param("invitationCode")String invitationCode);

	Integer saveUserNotActive(Map<String, String> paramMap);
}
