package com.doooly.dao.reachad;

import com.doooly.entity.home.AdBusinessScene;

import java.util.List;

public interface AdBusinessSceneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdBusinessScene record);

    int insertSelective(AdBusinessScene record);

    AdBusinessScene selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdBusinessScene record);

    int updateByPrimaryKey(AdBusinessScene record);

    List<AdBusinessScene> getListByCondition(AdBusinessScene adBusinessScene);
}