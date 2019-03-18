package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdBusiness;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ad_business商户信息表DAO
 * 
 * @author tangzhiyuan
 *
 */
public interface AdBusinessDao extends BaseDaoI<AdBusiness> {
	/**
	 * 获取主键id
	 * 
	 * @param businessId
	 * @return
	 */
	public AdBusiness getById(String id);
	public Long getAdBusinessId(String businessId);

	// 获取热门商家HOTBUSINESS
	public List<AdBusiness> findHotMerchant(int isHot);

	public List<AdBusiness> findAll();

	// 根据返佣规则表里面按品类获取返佣,获取商家对象
	public AdBusiness getByBusinessIdAndCategoryId(@Param("businessId") String businessId,
			@Param("categoryId") String categoryId);
	public AdBusiness getByBusinessIdForProductRebate(@Param("businessId") String businessId,
			@Param("categoryId") String categoryId);

	// 根据商家编码获取商家对象
	public AdBusiness getByBusinessId(@Param("businessId") String businessId);
	public List<AdBusiness> findAllBusinessLogo(@Param("address")String address);
	public List<AdBusiness> findHotMerchantWithLighten(@Param("userId") Integer userId, @Param("address") String address);
	
	//武钢的热门活动
	public List<AdBusiness> findHotMerchantForWuSteel(@Param("userId") Integer userId, @Param("address") String address);


	public List<AdBusiness> findScanList();
	
	//判断用户名及密码是否合法
    Boolean findByUsernamePassword(@Param("businessId") String businessId, @Param("username") String username, @Param("password") String password);

	public List<AdBusiness> findAllBusinessAllLogo();

	public List<AdBusiness> findAllBusinessByUserId(@Param("userId")String userId);
	/**
	 * 获取扫码优惠商家
	 * @param userId
	 * @return
	 */
	public List<AdBusiness> findBusinessList(String userId);

	public int getHotTotalNum(@Param("userId") Integer userId, @Param("address") String address,@Param("type") String type, @Param("shopType")Integer shopType);

	public List<AdBusiness> findHotMerchantByPage(@Param("userId") Integer userId, @Param("address") String address, @Param("type") String type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("shopType")Integer shopType);
	// 根据商家id获取可商家商品id
	Integer getMarketableProductId(Long businessId);
	public List<AdBusiness> findDongHangMerchantByPage(@Param("userId") Integer userId, @Param("type") String type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
	public List<AdBusiness> findHotMerchantsByDealType(@Param("userId") Integer userId, @Param("type") String type, @Param("address") String address, @Param("dealTypes") List<Integer> dealTypes);

    AdBusiness getBusiness(AdBusiness adBusiness);

    List<AdBusiness> getListByBusinessIds(@Param("businessIds") List<String> businessIds);
}
