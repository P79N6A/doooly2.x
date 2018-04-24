package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBrand;

/**
 * 品牌表DAO
 * 
 * @author 杨汶蔚
 * @date 2016年7月15日
 * @version 1.0
 */
public interface AdBrandDao extends BaseDaoI<AdBrand> {

	List<AdBrand> findAll();
}