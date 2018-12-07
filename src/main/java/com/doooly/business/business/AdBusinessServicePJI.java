package com.doooly.business.business;

import com.doooly.entity.reachad.AdBusinessServicePJ;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ad_business_service商户服务表服务接口
 * @Author: Mr.Wu
 * @Date: 2018/12/6
 */
public interface AdBusinessServicePJI {

    List<AdBusinessServicePJ> getDataByUserId(@Param("userId") Long userId, @Param("serviceType") String serviceType);
}
