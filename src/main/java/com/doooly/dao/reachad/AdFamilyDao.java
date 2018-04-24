package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdFamily;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 家庭表
 * @author: qing.zhang
 * @date: 2017-07-25
 */
public interface AdFamilyDao {
    int insert(AdFamily adFamily);

    void updateFamily(@Param("userId") Integer userId,@Param("pointShareSwitch") Integer pointShareSwitch);

    AdFamily getMyFamily(Integer userId);
}
