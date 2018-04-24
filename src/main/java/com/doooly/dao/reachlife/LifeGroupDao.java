/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachlife;

import com.doooly.common.persistence.CrudDao;
import com.doooly.common.persistence.annotation.MyBatisDao;
import com.doooly.entity.reachlife.LifeGroup;

/**
 * A系统企业表DAO接口
 * 
 * @author xiatianlong
 * @version 2015-12-17
 */
@MyBatisDao
public interface LifeGroupDao extends CrudDao<LifeGroup> {

	LifeGroup getGroupByGroupId(LifeGroup lifeGroup);

}