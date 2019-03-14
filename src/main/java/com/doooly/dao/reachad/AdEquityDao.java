/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;


import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdEquity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权益管理DAO接口
 * @author sfc
 * @version 2019-03-07
 */
public interface AdEquityDao extends BaseDaoI<AdEquity> {
    //查询权益最大ID
	Integer findMaxEquityId();

	//只用来更新权益中的开启关闭状态
    Integer updateEquityStatusById(Map map);

    //查询正常类型的权益ID
    List<String> findEquityIdList(@Param("equityLevel") String equityLevel, @Param("equityStatus") String equityStatus);
}