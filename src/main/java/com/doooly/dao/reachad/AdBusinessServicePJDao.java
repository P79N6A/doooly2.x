package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBusinessServicePJ;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ad_business_service商户服务表DAO
 * 
 * @author yangwenwei
 *
 */
public interface AdBusinessServicePJDao extends BaseDaoI<AdBusinessServicePJ> {

	List<AdBusinessServicePJ> getDataByUserId(@Param("userId") Long userId, @Param("serviceType") String serviceType);

}
