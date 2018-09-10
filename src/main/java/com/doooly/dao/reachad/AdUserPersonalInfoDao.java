package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdUserPersonalInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户信息
 * @author: wenwei.yang
 * @date: 2017-10-09
 */
public interface AdUserPersonalInfoDao extends BaseDaoI<AdUserPersonalInfo> {


    Integer getIsSetPayPassword(String userId);

    Integer updatePayPassword(@Param("userId") String userId, @Param("payPassword") String payPassword,@Param("isPayPassword") String isPayPassword);
}
