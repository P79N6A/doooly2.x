package com.doooly.business.recharge;

import com.doooly.entity.reachad.AdRechargeConf;

import java.util.Map;

/**
 * @Description: 企业充值配置service
 * @author: qing.zhang
 * @date: 2018-12-26
 */
public interface AdRechargeConfServiceI {

    AdRechargeConf getRechargeConf(Map<String, String> paramMap);//配置数据
}
