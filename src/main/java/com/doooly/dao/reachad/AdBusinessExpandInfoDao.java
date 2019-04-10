package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdBusinessExpandInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 1号通接口
 * @author: qing.zhang
 * @date: 2017-08-29
 */
public interface AdBusinessExpandInfoDao {

    AdBusinessExpandInfo getByBusinessId(String businessId);

    AdBusinessExpandInfo getBusinessExpandInfo(AdBusinessExpandInfo adBusinessExpandInfo);

    AdBusinessExpandInfo getBusinessAndExpandInfo(@Param("businessId")String businessId,
                                                  @Param("clientSecret")String clientSecret);
}
