/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdGroupEquityLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业权益信息DAO接口
 * @author sfc
 * @version 2019-03-09
 */
public interface AdGroupEquityLevelDao extends BaseDaoI<AdGroupEquityLevel> {
    List<AdGroupEquityLevel> getAllByGroupId(@Param("groupId") String groupId, @Param("limitCount") Integer limitCount);
}