package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdAppVersion;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: app版本控制Dao
 * @author: qing.zhang
 * @date: 2017-07-31
 */
public interface AdAppVersionDao {
    AdAppVersion getVersionInfo(@Param("uniqueIdentification") String uniqueIdentification,@Param("type") String type);//根据唯一表示查询app信息

    void insert(AdAppVersion adAppVersion);

    void update(AdAppVersion adAppVersion);
}
