package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.entity.reachad.AdBusinessStore;

/**
 * 分店表DAO
 * 
 * @author 杨汶蔚
 * @date 2017年4月10日
 * @version 1.0
 */
public interface AdBusinessStoreDao {

	List<AdBusinessStore> findList(Integer businessId);

	List<AdBusinessStore> findAddressList(Integer businessId);

	List<AdBusinessStore> findStoreList(@Param("businessId") String businessId, @Param("province") String province,
			@Param("city") String city, @Param("area") String area);

}