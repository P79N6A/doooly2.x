/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachlife;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachlife.LifeWechatBinding;

import java.util.List;

/**
 * A系统wechatBindingDAO接口
 * 
 * @author yangwenwei
 * @version 2018-4-23
 */
public interface LifeWechatBindingDao{

	LifeWechatBinding getDataByOpenId(@Param("openId") String openId);
	List<LifeWechatBinding> getWechatBindingListByCardNum(@Param("memberCardNums")String memberCardNums, @Param("memberMobiles")String memberMobiles);

}