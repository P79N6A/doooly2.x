package com.doooly.business.mall.service.Impl;

import com.doooly.business.lightenBusiness.AdLightenBusinessServiceI;
import com.doooly.business.mall.service.MallBusinessServiceI;
import com.doooly.business.utils.Pagelab;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.ComputedRangeUtil;
import com.doooly.dao.reachad.AdBrandDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdBusinessGroupDao;
import com.doooly.dao.reachad.AdBusinessStoreDao;
import com.doooly.dao.reachad.AdCouponActivityConnDao;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdProductCategoryDao;
import com.doooly.dao.reachad.AdProductDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdadDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAd;
import com.doooly.entity.reachad.AdBrand;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdBusinessGroup;
import com.doooly.entity.reachad.AdBusinessStore;
import com.doooly.entity.reachad.AdCouponActivityConn;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdProduct;
import com.doooly.entity.reachad.AdProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MallBusinessService implements MallBusinessServiceI {

	protected Logger log = LoggerFactory.getLogger(MallBusinessService.class);


	@Autowired
	private AdProductCategoryDao adProductCategoryDao;
	@Autowired
	private AdProductDao adProductDao;
	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private AdBrandDao adBrandDao;
	@Autowired
	private AdadDao adAdDao;
	@Autowired
	private AdCouponActivityConnDao adCouponActivityConnDao;
	@Autowired
	private AdBusinessStoreDao adBusinessStoreDao;
	@Autowired
	private AdBusinessGroupDao adBusinessGroupDao;
	@Autowired
	private AdLightenBusinessServiceI adLightenBusinessServiceI;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdGroupDao adGroupDao;
	private int HOTBUSINESS = 1;
	private String radiusMap = PropertiesConstants.dooolyBundle.getString("map_radius");
	@Override
	public List<AdProductCategory> getCategoryList() {
		List<AdProductCategory> list = adProductCategoryDao.findFirstCategory();
		return list;
	}

	@Override
	public HashMap<String, Object> getHotProductDatas(Integer catagoryId, Integer type, Integer adId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取热门推荐里面的二级标签信息
		List<AdProductCategory> categories = adProductCategoryDao.findSecondCategory(catagoryId, type);
		if (!categories.isEmpty()) {
			map.put("secendCategorieList", categories);
		}
		// 获取热门推荐里面的券码信息
		List<AdCouponActivityConn> coupons = adCouponActivityConnDao.findCouponByCategory(catagoryId, type, adId,
				new Date());
		if (!coupons.isEmpty()) {
			map.put("coupons", coupons);
		}
		// 获取热门推荐里面的商品信息
		List<AdProduct> adProducts = adProductDao.getProductsByHotRecommend(new Date());
		if (!adProducts.isEmpty()) {
			map.put("adProductList", adProducts);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getHotMerchantDatas(Integer catagoryId, Integer type, Integer adId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取热门商家对应卡券
		List<AdCouponActivityConn> coupons = adCouponActivityConnDao.findCouponByCategory(catagoryId, type, adId,
				new Date());
		if (!coupons.isEmpty()) {
			map.put("coupons", coupons);
		}
		// 获取热门商家HOTBUSINESS
		List<AdBusiness> merchants = adBusinessDao.findHotMerchant(HOTBUSINESS);
		if (!merchants.isEmpty()) {
			map.put("hotMerchantList", merchants);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getBrandDatas(Integer id, Integer type, Integer adId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取品牌馆对应广告
		List<AdAd> ads = adAdDao.findAll();
		if (!ads.isEmpty()) {
			map.put("ads", ads);
		}
		// 获取品牌馆（全部商家）
		List<AdBrand> merchants = adBrandDao.findAll();
		if (!merchants.isEmpty()) {
			map.put("brandList", merchants);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getCatagoryProductDatas(Integer catagoryId, Integer type, Integer adId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 获取各个分类里面的二级标签信息
		List<AdProductCategory> categories = adProductCategoryDao.findSecondCategory(catagoryId, type);
		if (!categories.isEmpty()) {
			map.put("secendCategorieList", categories);
		}
		// 获取各个分类对应卡券
		List<AdCouponActivityConn> coupons = adCouponActivityConnDao.findCouponByCategory(catagoryId, type, adId,
				new Date());
		if (!coupons.isEmpty()) {
			map.put("coupons", coupons);
		}
		// 获取各个分类的下属商品
		List<AdProduct> adProducts = adProductDao.findDataByCatagory(catagoryId);
		if (!adProducts.isEmpty()) {
			map.put("adProductlist", adProducts);
		}
		return map;
	}

	@Override
	public List<AdProduct> getProductsByCatagorySecondId(Integer secondId, Integer byType, Integer firstTag) {

		return adProductDao.findDataByCatagorySecondId(secondId, byType, firstTag);
	}

	@Override
	public MessageDataBean getRebate(Integer productId, BigDecimal shouldPrice, BigDecimal factPrice) {
		MessageDataBean messageDataBean = new MessageDataBean();
		BigDecimal chu = new BigDecimal("10000");
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdProduct adProduct = adProductDao.get(productId + "");
		if (adProduct != null) {
			AdBusiness adBusiness = adBusinessDao.get(adProduct.getBusinessId());
			if (adBusiness.getBussinessRebateType().equals("3") || adBusiness.getBussinessRebateType().equals("4")) {
				// 该商品有categoryId需要使用商家品类的返佣返利进行计算返利
				adBusiness = adBusinessDao.getByBusinessIdAndCategoryId(adProduct.getBusinessId(),
						adProduct.getCategoryId());
			}
			if (adBusiness != null && adBusiness.getBussinessRebate() != null && adBusiness.getUserRebate() != null
					&& adBusiness.getBussinessRebateType() != null) {
				Double rebate = 0d;
				BigDecimal businessRebate = new BigDecimal(adBusiness.getBussinessRebate());
				BigDecimal userRebate = new BigDecimal(adBusiness.getUserRebate());
				switch (adBusiness.getBussinessRebateType()) {
				// 未设置,返利为0
				case "0":
					map.put("rebate", 0);
					messageDataBean.setData(map);
					break;
				// 按应付计算
				case "1":
					rebate = shouldPrice.multiply(businessRebate).multiply(userRebate)
							.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("rebate", rebate);
					messageDataBean.setData(map);
					break;
				// 按实付计算
				case "2":
					rebate = factPrice.multiply(businessRebate).multiply(userRebate)
							.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("rebate", rebate);
					messageDataBean.setData(map);
					break;
				// 按品类应付
				case "3":
					rebate = shouldPrice.multiply(businessRebate).multiply(userRebate)
							.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("rebate", rebate);
					messageDataBean.setData(map);
					break;
				// 按品类实付
				case "4":
					rebate = factPrice.multiply(businessRebate).multiply(userRebate)
							.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					map.put("rebate", rebate);
					messageDataBean.setData(map);
					break;
				default:
					map.put("rebate", 0);
					messageDataBean.setData(map);
					break;
				}
				messageDataBean.setCode(MessageDataBean.success_code);
			} else {
				messageDataBean.setCode(MessageDataBean.failure_code);
			}
		} else {
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean getBusinessInfo(Long userId,Long adBusinessId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdBusiness adBusiness = adBusinessDao.get(adBusinessId + "");
		// TODO 该商家是否被点亮 userId
		AdGroup logo = adGroupDao.getGroupLogoByUserId(userId.intValue());
		if (logo != null) {
			map.put("logo", logo.getLogoUrl());
			map.put("groupShortName", logo.getGroupShortName());
		}else {
			map.put("logo", null);
			map.put("groupShortName", null);
		}
		if (adBusiness != null) {
			Integer lightenBusinessType = adLightenBusinessServiceI.lightenBusinessType(userId.toString(), adBusinessId.toString());
			adBusiness.setLightenType(lightenBusinessType+"");
			map.put("adBusiness", adBusiness);
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean getBrandInfo(Long adBrandId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdBrand adBrand = adBrandDao.get(adBrandId + "");
		if (adBrand != null) {
			map.put("adBrand", adBrand);
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;
	}
	/**
	 * lat 纬度
	 * lng 经度
	 * 
	 * */
	@Override
	public MessageDataBean getBusinessStoreList(Integer businessId,Double lat,Double lng, Integer currentPage, Integer pageSize) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Pagelab pagelab = new Pagelab(currentPage,pageSize);
		List<AdBusinessStore> result = new ArrayList<AdBusinessStore>();
		List<AdBusinessStore> resultPage = new ArrayList<AdBusinessStore>();
		List<AdBusinessStore> list = adBusinessStoreDao.findAddressList(businessId);
		if (!list.isEmpty()) {
			pagelab.setTotalNum(list.size());
			for (AdBusinessStore a : list) {
				if (a.getLatitude()!=null&&a.getLatitude()!=""&&a.getLongitude()!=null&&a.getLongitude()!="") {
					double distance = ComputedRangeUtil.GetDistance(lat, lng, Double.valueOf(a.getLatitude()), Double.valueOf(a.getLongitude()));
					a.setDistance(distance);
					result.add(a);
				}
			}
			Collections.sort(result,new Comparator<AdBusinessStore>() {
				@Override
				public int compare(AdBusinessStore o1, AdBusinessStore o2) {
					// TODO Auto-generated method stub
					return o1.getDistance().compareTo(o2.getDistance());
				}
			});
			if (pagelab.getCountPage() >1) {
				for (int i = (currentPage-1)*pageSize; i < currentPage*pageSize; i++) {
					if ((i+1) <= result.size()) {
						resultPage.add(result.get(i));
					}
				}
			}else {
				resultPage.addAll(result);
			}
			map.put("list", resultPage);
			map.put("countPage", pagelab.getCountPage());
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		}else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
			messageDataBean.setMess("更多门店正在开发中，请耐心等待~~~");
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean getGuideDetail(Integer userId,Integer productId,Integer businessId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		AdProduct adProduct = adProductDao.getGuideDetail(productId);
		// TODO 判断该商家是否被该用户点亮
		if (adProduct != null) {
			//计算企业总人数的三分之一
			Integer count = adUserDao.getMemberCount(userId);
			if ((adProduct.getHitsCount()+3) <= (count/3)) {
				adProduct.setHitsCount(adProduct.getHitsCount()+3);
				adProductDao.updateHitsCount(adProduct);
			}else {
				adProduct.setHitsCount(count/3);
			}
			Integer lightenBusinessType = adLightenBusinessServiceI.lightenBusinessType(userId.toString(), businessId.toString());
			adProduct.setLightenType(lightenBusinessType+"");
			map.put("product", adProduct);
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean gethotBusiness(Integer userId,String address) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdBusiness> merchants = adBusinessDao.findHotMerchantWithLighten(userId,address);
		for (AdBusiness adBusiness : merchants) {
			Integer lightenBusinessType = adLightenBusinessServiceI.lightenBusinessType(userId.toString(), adBusiness.getId()+"");
			adBusiness.setLightenType(lightenBusinessType.toString());
		}
		AdGroup logo = adGroupDao.getGroupLogoByUserId(userId);
		if (logo != null) {
			map.put("logo", logo.getLogoUrl());
			map.put("groupShortName", logo.getGroupShortName());
		}else {
			map.put("logo", null);
			map.put("groupShortName", null);
		}
		if (!merchants.isEmpty()) {
			map.put("hotMerchantList", merchants);
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;

	}
@Override
	public MessageDataBean gethotBusiness(String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdBusiness> merchants = adBusinessDao.findHotMerchant(HOTBUSINESS);
		//过滤用户所在企业是否有权限查看热门商户
		List<AdBusiness> hasPermissionMht = new ArrayList<AdBusiness>();
		String gid = adUserDao.selectByPrimaryKey(Long.valueOf(userId)).getGroupNum().toString();
		log.info("gid = {},merchants.size = {}",gid,merchants.size());
		for (int i = 0; i < merchants.size(); i++) {
			AdBusiness obj = merchants.get(i);
			List<AdBusinessGroup> ls = selectByGid(gid);
			for (AdBusinessGroup adBusinessGroup : ls) {
				if(Long.valueOf(adBusinessGroup.getBusinessId()) == obj.getId().longValue()){
					hasPermissionMht.add(obj);
					break;
				}
			}
		}
		if (!hasPermissionMht.isEmpty()) {
			map.put("hotMerchantList", hasPermissionMht);
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;

	}
	
	@Override
	public List<AdBusinessGroup> selectByGid(String gid) {
		return  adBusinessGroupDao.selectByGid(gid);
	}


	@Override
	public Double getProductRebate(AdProduct adProduct) {
	 	BigDecimal chu = new BigDecimal("10000");
		BigDecimal shouldPrice = adProduct.getMarketPrice();
		BigDecimal factPrice = shouldPrice.multiply(new BigDecimal(adProduct.getDiscount())).divide(new BigDecimal(10));
		AdBusiness adBusiness = adBusinessDao.get(adProduct.getBusinessId());
		if (adBusiness.getBussinessRebateType().equals("3") || adBusiness.getBussinessRebateType().equals("4")) {
			// 该商品有categoryId需要使用商家品类的返佣返利进行计算返利
			adBusiness = adBusinessDao.getByBusinessIdForProductRebate(adProduct.getBusinessId(),
					adProduct.getCategoryId());
		}
		if (adBusiness != null && adBusiness.getBussinessRebate() != null && adBusiness.getUserRebate() != null
				&& adBusiness.getBussinessRebateType() != null) {
			Double rebate = 0d;
			BigDecimal businessRebate = new BigDecimal(adBusiness.getBussinessRebate());
			BigDecimal userRebate = new BigDecimal(adBusiness.getUserRebate());
			switch (adBusiness.getBussinessRebateType()) {
			// 未设置,返利为0
			case "0":
				return 0d;
			// 按应付计算
			case "1":
				rebate = shouldPrice.multiply(businessRebate).multiply(userRebate)
						.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				break;
			// 按实付计算
			case "2":
				rebate = factPrice.multiply(businessRebate).multiply(userRebate)
						.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				break;
			// 按品类应付
			case "3":
				rebate = shouldPrice.multiply(businessRebate).multiply(userRebate)
						.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				break;
			// 按品类实付
			case "4":
				rebate = factPrice.multiply(businessRebate).multiply(userRebate)
						.divide(chu, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				break;
			default:
				break;
			}
			return rebate;
		}
		return 0d;
 	}
	
	/***
	 * 武钢的热门商家
	 * 
	 * @param userId
	 * @param address
	 * @return
	 */
	@Override
	public MessageDataBean gethotBusinessForWuSteel(Integer userId,String address) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdBusiness> merchants = adBusinessDao.findHotMerchantForWuSteel(userId,address);
		for (AdBusiness adBusiness : merchants) {
			Integer lightenBusinessType = adLightenBusinessServiceI.lightenBusinessType(userId.toString(), adBusiness.getId()+"");
			adBusiness.setLightenType(lightenBusinessType.toString());
		}
		AdGroup logo = adGroupDao.getGroupLogoByUserId(userId);
		if (logo != null) {
			map.put("logo", logo.getLogoUrl());
			map.put("groupShortName", logo.getGroupShortName());
		}else {
			map.put("logo", null);
			map.put("groupShortName", null);
		}
		if (!merchants.isEmpty()) {
			map.put("hotMerchantList", merchants);
			messageDataBean.setData(map);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		return messageDataBean;
	}

	@Override
	public AdBusiness getById(String id) {
		return adBusinessDao.getById(id);
	}
}
