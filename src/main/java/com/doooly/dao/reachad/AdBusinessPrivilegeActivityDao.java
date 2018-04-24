package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdBusinessPrivilegeActivity;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 商户特权开通
 * @author: qing.zhang
 * @date: 2017-09-12
 */
public interface AdBusinessPrivilegeActivityDao {

    AdBusinessPrivilegeActivity get(@Param("businessId") Integer businessId,@Param("userId")  Integer userId);

    void insert(AdBusinessPrivilegeActivity adBusinessPrivilegeActivity);

    int setUserPrivilege(AdBusinessPrivilegeActivity adBusinessPrivilegeActivity);

    AdBusinessPrivilegeActivity getUserPrivilege(AdBusinessPrivilegeActivity adBusinessPrivilegeActivity);
}
