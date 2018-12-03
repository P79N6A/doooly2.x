package com.doooly.business.mall.service;

import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface MallBusinessServiceI {
	
	public MessageDataBean gethotBusinessForWuSteel(Integer userId,String address) ;
	
	public List<AdProductCategory> getCategoryList();

	public HashMap<String, Object> getHotProductDatas(Integer catagoryId, Integer type, Integer adId);

	public HashMap<String, Object> getHotMerchantDatas(Integer catagoryId, Integer type, Integer adId);

	public HashMap<String, Object> getBrandDatas(Integer catagoryId, Integer type, Integer adId);

	public HashMap<String, Object> getCatagoryProductDatas(Integer catagoryId, Integer type, Integer adId);

	public List<AdProduct> getProductsByCatagorySecondId(Integer secondId, Integer byType, Integer firstTag);

	public MessageDataBean getRebate(Integer productId, BigDecimal shouldPrice, BigDecimal factPrice);

	public MessageDataBean getBusinessInfo(Long adBusinessId, Long adBusinessId2);

	public MessageDataBean getBusinessStoreList(Integer businessId,Double lat,Double lng, Integer currentPage, Integer pageSize);

	public MessageDataBean getGuideDetail(Integer userId,Integer productId,Integer businessId);

	public MessageDataBean getBrandInfo(Long adBrandId);

	public MessageDataBean gethotBusiness(String userId);
	
	public List<AdBusinessGroup> selectByGid(String gid);
	public MessageDataBean gethotBusiness(Integer userId, String address);
	
	public Double getProductRebate(AdProduct adProduct);

	public AdBusiness getById(String id);

	public AdAd findByTypeAndGroup(int groupId ,String title);

	public String getActivityByTypeAndGroup(Integer type, Integer groupId);
}
