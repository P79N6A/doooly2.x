/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;


import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdGroupEquityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业权益关系结构DAO接口
 * @author sfc
 * @version 2019-03-12
 */
public interface AdGroupEquityRelationDao extends BaseDaoI<AdGroupEquityRelation> {
    //批量新增企业权益关系
    Integer saveList(List<AdGroupEquityRelation> adGroupEquityRelationList);

    //查询企业添加权益ID集合
    List<String> findGroupEquityRelationIdList(@Param("groupId") String groupId, @Param("relationStatus") String relationStatus);

    //企业保存后重置新增企业记录
    Integer updateGroupEquityRelation(@Param("groupId") String groupId, @Param("relationStatus") String relationStatus);
}