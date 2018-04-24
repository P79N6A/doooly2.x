package com.doooly.dao.doooly;


import com.doooly.entity.doooly.AdAppVersionUrl;

/**
 * @Description: app版本控制Dao
 * @author: qing.zhang
 * @date: 2017-07-31
 */
public interface AdAppVersionUrlDao {
    AdAppVersionUrl getVersionInfo(String type);//根据唯一表示查询app信息

    void upDateAppVersion(AdAppVersionUrl versionInfo);

    void insert(AdAppVersionUrl versionInfo);

}
