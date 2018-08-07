package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

/**
 * 集享积分bindId和用户绑定
 */
public interface AdNexusBindDao {

    int insert(@Param("userId") String userId, @Param("bindId") String bindId);

    String getBindId(@Param("userId") long userId);

    String getByBindId(@Param("bindId") String bindId);

}
