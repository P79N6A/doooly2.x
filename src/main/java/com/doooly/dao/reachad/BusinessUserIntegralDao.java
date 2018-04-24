package com.doooly.dao.reachad;

import com.doooly.entity.reachad.BusinessUserIntegral;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户商家分配积分表
 * @author: qing.zhang
 * @date: 2018-03-08
 */
public interface BusinessUserIntegralDao {

    BusinessUserIntegral getDiDiIntegral(@Param("businessId") Long businessId,@Param("userId")  Long userId);

    void insert(BusinessUserIntegral businessUserIntegral1);

    void insertRecord(BusinessUserIntegral businessUserIntegral1);

    void update(BusinessUserIntegral businessUserIntegral);

    void delete(BusinessUserIntegral businessUserIntegral);

}