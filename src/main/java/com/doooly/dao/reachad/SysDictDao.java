package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.SysDict;

public interface SysDictDao extends BaseDaoI<SysDict> {

	List<SysDict> findAll();

	List<SysDict> findAllByUserIdAndAddress(@Param("userId") Integer userId, @Param("address") String address);

	/**
	 * 根据类型查询企业口令问题
	 */
	List<SysDict> getSysDict(@Param("type") String type);
}
