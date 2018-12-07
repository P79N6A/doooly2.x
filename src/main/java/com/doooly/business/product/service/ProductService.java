package com.doooly.business.product.service;

import java.util.HashMap;
import java.util.List;

import com.doooly.business.product.entity.ActivityInfo;
import com.doooly.business.product.entity.AdGroupSelfProductPrice;
import com.doooly.business.product.entity.AdSelfProduct;

public interface ProductService {
	//活动库存
	public int decStock(int number, int skuId) ;

	public int incStock(int number, int skuId);

	//商品库存
	public int decInventory(int skuId) ;

	public int incInventory(int skuId);


	public AdSelfProduct getProduct(int productId);

	public AdSelfProduct getProductSku(Integer merchantId, Integer productId, Integer skuId);

	/**
	 * 获取自营商品列表
	 * 
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @param userId
	 *            会员id(自营商品查看权限)
	 */
	HashMap<String, Object> getSelfProductList(int currentPage, int pageSize,String userId);

	/**
	 * 获取卡券商品详情页信息
	 * 
	 * @param productId
	 *            商品id
	 * @param userId
	 *            用户id
	 */
	HashMap<String, Object> getSelfProductDetail(String productId,String userId);


	public ActivityInfo getActivityInfo(String groupId, int skuId);
	
	AdGroupSelfProductPrice getSelfProductSkuListByName(String activityName);

	List<AdGroupSelfProductPrice> getSelfProductAirport(String activityName,String selfProductId);

}
