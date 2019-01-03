/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdRechargeConf;

public interface AdRechargeConfDao {

    AdRechargeConf getRechargeConf(String groupId);//配置数据
}