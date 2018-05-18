package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdVoteRecord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 投票记录
 * @author: qing.zhang
 * @date: 2017-05-31
 */
public interface AdVoteRecordDao extends BaseDaoI<AdVoteRecord> {

    AdVoteRecord findByOptionIdAndOpenId(@Param("optionId")Integer optionId,@Param("wechatOpenId")String wechatOpenId);//根据用户id和选项id查询投票记录

    int findVoteCountByOpenId(String wechatOpenId);//查询用户总投票数

    int insert(AdVoteRecord adVoteRecord);//查询用户总投票数

	String getByUserId(@Param("userId")String userId,@Param("activityId")String activityId);

	void updateState(@Param("state")char state, @Param("userId")String userId ,@Param("activityId")String activityId);

	List<AdVoteRecord> findByTelephoneAndActivityId(@Param("telephone")String telephone,@Param("activityId")String activityId);

	List<AdVoteRecord> findUsedRecordByTelephoneAndActivityId(@Param("telephone")String telephone,@Param("activityId")String activityId, @Param("state")String state);

	int getByUserIdAndActivityId(@Param("userId")String userId, @Param("activityId")String activityId,@Param("state")String state);

	void updateShareRecord(@Param("userId")String userId, @Param("telephone")String telephone);

 }
