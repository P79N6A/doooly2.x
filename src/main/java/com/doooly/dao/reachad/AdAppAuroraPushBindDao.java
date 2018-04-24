package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdAppAuroraPushBind;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 极光绑定信息Dao
 * @author: qing.zhang
 * @date: 2017-07-31
 */
public interface AdAppAuroraPushBindDao {

    AdAppAuroraPushBind findByUserId(@Param("userId") String userId, @Param("registrationId") String registrationId);

    void saveAuroraPushBind(AdAppAuroraPushBind adAppAuroraPushBind);

}
