package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdDoubleElevenRecord;

public interface AdDoubleElevenRecordDao extends BaseDaoI<AdDoubleElevenRecord> {
	AdDoubleElevenRecord findTotalDataBySuperId(Integer userId);

	List<AdDoubleElevenRecord> findDataBySuperId(String userId);

	List<AdDoubleElevenRecord> findDataByUserId(String userId);


	AdDoubleElevenRecord findDataByUserIdForTypeZero(Integer valueOf);

}
