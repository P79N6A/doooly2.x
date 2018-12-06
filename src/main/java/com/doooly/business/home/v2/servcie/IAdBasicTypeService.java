package com.doooly.business.home.v2.servcie;

import com.doooly.entity.reachad.AdBasicType;

import java.util.List;

/**
 * 模版常量配置服务类
 */
public interface IAdBasicTypeService {

    List<AdBasicType> getFloors(String userId, Integer type, Integer templateType);
}
