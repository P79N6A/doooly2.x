package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBusinessServicePJ;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ad_business_service商户服务表DAO
 * 
 * @author yangwenwei
 *
 */
@Repository
public interface AdBusinessServicePJDao extends BaseDaoI<AdBusinessServicePJ> {

//	List<AdBusinessServicePJ> getDataByUserId(@Param("userId") Long userId, @Param("serviceType") String serviceType);

	List<AdBusinessServicePJ> getDataByUserId(@Param("userId") Long userId, @Param("serviceType") String serviceType, @Param("address")String address);

}
