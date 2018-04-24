/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachlife;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachlife.LifeActivity;

/**
 * A系统福利DAO接口
 * 
 * @author lxl
 * @version 2016-12-15
 */
public interface LifeActivityDao{
	LifeActivity getUserRecord(@Param("telephone") String telephone);

	void insert(LifeActivity a);
}