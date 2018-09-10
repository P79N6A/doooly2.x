package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdBusinessExpandInfo;

/**
 * @Description: 1号通接口
 * @author: qing.zhang
 * @date: 2017-08-29
 */
public interface AdBusinessExpandInfoDao {

    AdBusinessExpandInfo getByBusinessId(String businessId);

}
