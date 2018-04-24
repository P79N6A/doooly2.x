/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.business.reachLife;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doooly.dao.reachlife.LifeGroupDao;
import com.doooly.entity.reachlife.LifeGroup;

/**
 * A系统企业表Service
 * @author xiatianlong
 * @version 2015-12-17
 */
@Service
@Transactional(readOnly = true)
public class LifeGroupService{
	@Autowired
	private LifeGroupDao lifeGroupDao;

	/**
	 * 通过groupId查询Id
	 * @param GroupIds
	 * @return
	 */
	public LifeGroup getGroupByGroupId(String GroupIds){
		LifeGroup lifeGroup = new LifeGroup();
		lifeGroup.setAdId(GroupIds);
		lifeGroup.setDelFlg("0");
		return lifeGroupDao.getGroupByGroupId(lifeGroup);
	}

	
}