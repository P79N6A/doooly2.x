package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBusinessServicePJ;

/**
 * ad_business_service商户服务表DAO
 * 
 * @author yangwenwei
 *
 */
public interface AdBusinessServicePJDao extends BaseDaoI<AdBusinessServicePJ> {

	List<AdBusinessServicePJ> getDataByUserId(@Param("userId")Long userId);

}
