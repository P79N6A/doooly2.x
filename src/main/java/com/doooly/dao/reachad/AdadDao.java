package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdAd;

public interface AdadDao extends BaseDaoI<AdAd> {

	List<AdAd> findAll();

	List<AdAd> findAllByType(@Param("type") int type,@Param("state") Integer state,@Param("userId") Integer userId);

	AdAd findByTypeAndGroup(@Param("groupId") int groupId,@Param("title") String title);
}
