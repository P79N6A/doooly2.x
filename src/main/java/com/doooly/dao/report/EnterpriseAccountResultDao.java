/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.report;

import com.doooly.entity.report.EnterpriseAccountResult;

/**
 * report库DAO接口
 * 
 * @author linking
 * @version 2018-08-10
 */
public interface EnterpriseAccountResultDao {

	/**
	 * 根据手机号获取升级结果
	 */
	public EnterpriseAccountResult getResult(EnterpriseAccountResult enterpriseAccountResult);
}