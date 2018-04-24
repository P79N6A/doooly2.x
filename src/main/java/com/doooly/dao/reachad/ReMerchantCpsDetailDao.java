package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.ReMerchantCpsDetail;

/**
 * re_merchant_cps_detail商家营销规则详情表DAO
 * 
 * @author tangzhiyuan
 *
 */
public interface ReMerchantCpsDetailDao extends BaseDaoI<ReMerchantCpsDetail> {
	/**
	 * 查询配置项详情
	 * 
	 * @param merchantCpsId
	 * @param orderNumber
	 *            订单序号
	 * @return
	 */
	List<ReMerchantCpsDetail> getDetailByOrderNumber(@Param("merchantCpsId") Long merchantCpsId,
			@Param("orderNumber") Integer orderNumber);

	/**
	 * 获取商家设置营销总订单数
	 * 
	 * @param merchantCpsId
	 * @return
	 */
	Integer getOrderCount(Long merchantCpsId);
}
