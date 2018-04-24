package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdSupport;

/**
 * @Description: 点赞接口
 * @author: qing.zhang
 * @date: 2017-04-26
 */
public interface AdSupportDao {

    Boolean findAdSupport(AdSupport adSupport);//根据实体查询记录

    void insertSupport(AdSupport adSupport);


}
