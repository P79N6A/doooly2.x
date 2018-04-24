package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdIntegralActivityConn;

/**
 * 用户领取积分活动明细表DAO
 * 
 * @author yangwenwei
 * @date 2018年2月27日
 * @version 1.0
 */
public interface AdIntegralActivityConnDao {

	AdIntegralActivityConn checkIsHadActivity(Long userId);

}