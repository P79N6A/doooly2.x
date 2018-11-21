package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdGuideCategory;

import java.util.List;

/**
 * 导购类目
 */
public interface AdGuideCategoryDao {

    List<AdGuideCategory> findList();

}