package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdAd;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdadDao extends BaseDaoI<AdAd> {

	List<AdAd> findAll();

	List<AdAd> findAllByType(@Param("type") int type,@Param("state") Integer state,@Param("userId") Integer userId);

	AdAd findByTypeAndGroup(@Param("groupId") int groupId,@Param("title") String title);

	AdAd getActivityByTypeAndGroup(@Param("type") Integer type, @Param("groupId") Integer groupId);

	List<AdAd> getByTypeAndGroup(@Param("type") int type, @Param("groupId") String groupId);
}
