/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachlife;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachlife.LifeWechatBinding;

/**
 * A系统wechatBindingDAO接口
 * 
 * @author yangwenwei
 * @version 2018-4-23
 */
public interface LifeWechatBindingDao{
	LifeWechatBinding getDataByOpenId(@Param("openId") String openId);

}