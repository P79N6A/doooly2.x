package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.ReMerchantCps;

/**
 * re_merchant_cps商家营销表DAO
 * 
 * @author tangzhiyuan
 *
 */
public interface ReMerchantCpsDao extends BaseDaoI<ReMerchantCps> {
	/**
	 * 获取商家营销规则
	 * 
	 * @param adBusinessId
	 * @return
	 */
	ReMerchantCps getCpsByBusinessId(Long adBusinessId);
}
