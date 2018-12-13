/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.business.product.entity.AdGroupSelfProductPrice;
import com.doooly.business.product.entity.AdSelfProduct;
import com.doooly.business.product.entity.AdSelfProductSku;
import com.doooly.business.product.entity.AdSelfProductType;

/**
 * 商品管理DAO接口
 * 
 * @author wenwei.yang
 * @version 2017-09-20
 */
public interface AdSelfProductDao{

	int decStock(@Param("skuId") int skuId, @Param("number") int number);
	int incStock(@Param("skuId") int skuId, @Param("number") int number);

	int decInventory(@Param("skuId") int skuId);
	int incInventory(@Param("skuId") int skuId);

	ActivityInfo getActivityInfo(@Param("groupId") String groupId, @Param("skuId") int skuId);
	
	AdSelfProduct getProduct(@Param("merchantId") Integer merchantId, @Param("productId") Integer productId,
			@Param("skuId") Integer skuId);
	

	/** 查询总数(已上架的自营商品) */
	int getSelfProductTotalNum();

	/** 获取分页的自营商品 */
	List<AdSelfProduct> getSelfProductList(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize, @Param("groupId") Long groupId);

	/** 根据商品id获取销售价最低的SKU(除去被屏蔽的sku) */
	AdSelfProductSku getLowestSellingPriceSku(@Param("productId")String productId,@Param("limitSkuArray")String [] limitSkuArray);
	
	/** 获取最低特惠价|最低折扣(排除商品屏蔽的sku) */
	AdSelfProduct getLowestSpecialPriceAndDiscount(@Param("productId")String productId,@Param("limitSkuArray")String [] limitSkuArray,@Param("groupId")Long groupId);

	/** 根据商品id获取商品详情 */
	AdSelfProduct getSelfProductDetailById(String productId);

	/** 根据商品id获取sku集合 */
	List<AdSelfProductSku> getSelfProductSkuList(@Param("productId")String productId,@Param("limitSkuIdList")List<String> limitSkuIdList,@Param("groupId")Long groupId,@Param("activityName") String activityName);

	/** 根据商品id获取卡券类型集合 */
	List<AdSelfProductType> getSelfProductTypeList(String productId);
	
	/** 查询某商品被屏蔽skuIdList */
	List<String> getLimitSkuIdListByProductId(@Param("productId")String productId,@Param("groupId")Long groupId);
	
	/** 根据活动名字查询sku */
	AdGroupSelfProductPrice getSelfProductSkuListByName(@Param("activityName") String activityName);

	/**机场活动**/
	List<AdGroupSelfProductPrice> getSelfProductAirport(@Param("activityName") String activityName,@Param("selfProductId") String selfProductId);
}