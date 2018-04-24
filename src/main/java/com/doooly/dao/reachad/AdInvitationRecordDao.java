package com.doooly.dao.reachad;

import com.doooly.common.persistence.CrudDao;
import com.doooly.entity.reachad.AdInvitationRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 家属邀请记录DAO
 * 
 * @author yuelou.zhang
 * @date 2016年10月31日
 * @version 1.0
 */

public interface AdInvitationRecordDao extends CrudDao<AdInvitationRecord> {

	/**
	 * 根据会员id和商品编号获取兑换码
	 * 
	 * @param userId
	 *            会员id
	 * @param productSN
	 *            商品编号
	 * @return 兑换码
	 */
	String findCodeByUserIdAndSn(@Param("userId") Integer userId,@Param("productSN") String productSN);

	/**
	 * 获取邀请人的邀请记录(被邀请人已核销&&邀请人未加5积分)
	 * 
	 * @param userId
	 *            邀请人会员id
	 * @return
	 */
	List<AdInvitationRecord> findRecordListByInviterId(Integer userId);
	
	/**
	 * 获取邀请人的邀请记录(该邀请人的全部邀请记录)
	 * 
	 * @param userId
	 *            邀请人会员id
	 * @return
	 */
	List<AdInvitationRecord> findAllRecordListByInviterId(@Param("userId") Integer userId, @Param("flag") String flag);

	List<AdInvitationRecord> getFamilys(@Param("userId")String userId);

	AdInvitationRecord findRecodByInviteeId(@Param("userId")String id);

	void updateType(@Param("userId")String id);
}
