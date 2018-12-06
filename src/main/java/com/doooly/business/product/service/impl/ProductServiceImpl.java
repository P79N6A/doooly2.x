package com.doooly.business.product.service.impl;

import com.doooly.business.product.entity.*;
import com.doooly.business.product.service.ProductService;
import com.doooly.business.utils.Pagelab;
import com.doooly.dao.reachad.*;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	protected Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private AdSelfProductDao adSelfProductDao;
	@Autowired
	private AdSelfProductImageDao adSelfProductImageDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdGroupDao adGroupDao;
	@Autowired
	private AdOrderReportDao adOrderReportDao;

	@Override
	public int decStock(int number, int skuId) {
		return adSelfProductDao.decStock(skuId,number);
	}

	@Override
	public int incStock(int number, int skuId) {
		return adSelfProductDao.incStock(skuId,number);
	}

	@Override
	public int decInventory(int skuId) {
		return adSelfProductDao.decInventory(skuId);
	}

	@Override
	public int incInventory(int skuId) {
		return adSelfProductDao.incInventory(skuId);
	}

	@Override
	public AdSelfProduct getProduct(int productId) {
		return adSelfProductDao.getProduct(null, productId, null);
	}

	/**
	 * 获得活动信息
	 * @param groupId
	 * @param skuId
	 * @return
	 */
	public ActivityInfo getActivityInfo(String groupId, int skuId) {
		logger.info("getActivityInfo() groupId = {},skuId = {}",groupId,skuId);
		if (StringUtils.isNotBlank(groupId) && skuId > 0) {
			ActivityInfo activityInfo = adSelfProductDao.getActivityInfo(groupId,skuId);
			logger.info("getActivityInfo() activityInfo = {}",activityInfo);
			return activityInfo;
		}
		return null;
	}

	@Override
	public AdSelfProduct getProductSku(Integer merchantId, Integer productId, Integer skuId) {
		return adSelfProductDao.getProduct(merchantId, productId, skuId);
	}

	@Override
	public HashMap<String, Object> getSelfProductList(int currentPage, int pageSize, String userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 1.查询自营商品总数(已上架)
			int totalNum = adSelfProductDao.getSelfProductTotalNum();
			if (totalNum > 0) {
				Pagelab pagelab = new Pagelab(currentPage, pageSize);
				pagelab.setTotalNum(totalNum);
				// 2.获取分页的自营商品(排除sku全部被屏蔽的商品)
				AdGroup group = adGroupDao.findGroupByUserId(userId);// 查询用户所属企业
				List<AdSelfProduct> selfProductList = adSelfProductDao.getSelfProductList(pagelab.getStartIndex(),
						pagelab.getPageSize(), group.getId());
				if (CollectionUtils.isNotEmpty(selfProductList)) {
					for (AdSelfProduct adSelfProduct : selfProductList) {
						// 3.根据商品id获取最低销售价及其市场价
						String limitSkuListStr = adSelfProduct.getLimitSkuListStr();
						logger.info(String.format("productId = %s 的自营商品，limitSkuListStr=%s", adSelfProduct.getId(),
								limitSkuListStr));
						String[] limitSkuArray = StringUtils.split(limitSkuListStr, ",");
						AdSelfProductSku sku = adSelfProductDao.getLowestSellingPriceSku(adSelfProduct.getId(),
								limitSkuArray);
						// 获取最低特惠价|最低折扣(排除商品屏蔽的sku)
						AdSelfProduct product = adSelfProductDao.getLowestSpecialPriceAndDiscount(adSelfProduct.getId(),
								limitSkuArray, group.getId());
						String sellPrice = sku.getSellPrice();
						if (product != null) {
							String specialPrice = product.getSpecialPrice();
							logger.info(String.format(
									"productId = %s 的自营商品，最低销售价sellPrice=%s | 最低特惠价specialPrice =%s | 市场价marketPrice=%s | "
											+ "最低折扣discount=%s |总库存inventory=%s",
									adSelfProduct.getId(), sellPrice, specialPrice, sku.getMarketPrice(),
									product.getDiscount(), product.getInventory()));
							sellPrice = this.getLowerPrice(specialPrice, sellPrice);
							adSelfProduct.setDiscount(
									StringUtils.isNotBlank(product.getDiscount()) ? product.getDiscount() : "");
							adSelfProduct.setInventory(
									StringUtils.isNotBlank(product.getInventory()) ? product.getInventory() : "");
							adSelfProduct.setTagName(product.getTagName());
							logger.info(String.format("productId = %s 的自营商品，活动开始时间StartDate=%s | 活动结束时间EndDate =%s",
									adSelfProduct.getId(),product.getActivityStartDate(),product.getActivityEndDate()));
							adSelfProduct.setActivityStartDate(product.getActivityStartDate());
							adSelfProduct.setActivityEndDate(product.getActivityEndDate());
							//计算倒计时时间差 [单位毫秒]
							if(product.getActivityStartDate()!=null){
								adSelfProduct.setCountdownTime(product.getActivityStartDate().getTime()-System.currentTimeMillis());
							}
							adSelfProduct.setCurrentTime(new Date());
						} else {
							logger.info(String.format("productId = %s 的自营商品，最低销售价sellPrice=%s | 市场价marketPrice=%s",
									adSelfProduct.getId(), sellPrice, sku.getMarketPrice()));
							adSelfProduct.setDiscount("");
							adSelfProduct.setInventory("");
							adSelfProduct.setTagName("");
						}
						adSelfProduct.setSellPrice(sellPrice);
						adSelfProduct.setMarketPrice(sku.getMarketPrice());
					}
				}
				map.put("selfProductList", selfProductList);
				map.put("countPage", pagelab.getCountPage());// 总页码
			}
		} catch (Exception e) {
			logger.error("获取自营商品异常！！！", e);
		}
		return map;
	}

	@Override
	public HashMap<String, Object> getSelfProductDetail(String productId, String userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			// 0.查询某商品被屏蔽skuId
			AdGroup group = adGroupDao.findGroupByUserId(userId);// 查询用户所属企业
			logger.info(String.format("productId=%s 的自营商品,| 用户id=%s,| 企业id=%s 查询被屏蔽的skuIdList", productId, userId,
					group.getId()));
			List<String> limitSkuIdList = adSelfProductDao.getLimitSkuIdListByProductId(productId, group.getId());

			// 1.判断活动是否开始
			String[] limitSkuArray = CollectionUtils.isNotEmpty(limitSkuIdList)?limitSkuIdList.toArray(new String[limitSkuIdList.size()]):null;
			AdSelfProduct product = adSelfProductDao.getLowestSpecialPriceAndDiscount(productId,limitSkuArray, group.getId());
			if(product != null){
				Date activityStartDate = product.getActivityStartDate();
				Date activityEndDate = product.getActivityEndDate();
				Date currentTime = new Date();
				logger.info(String.format("productId=%s 的自营商品,| 活动开始时间=%s,| 结束时间=%s |当前时间=%s",
						productId,activityStartDate,activityEndDate,currentTime));
				if(activityStartDate!=null && activityStartDate.after(currentTime)){
					logger.info("活动尚未开始！！");
					map.put("code", MessageDataBean.time_out_code);
					return map;
				}
			}

			// 2.获取图片 关联image表
			List<String> imagesList = new ArrayList<String>();
			AdSelfProductImage selfProductImage = adSelfProductImageDao.getImageByProductId(productId);
			if (selfProductImage != null) {
				// 商品主图
				imagesList.add(0, selfProductImage.getImage());
				// 商品详情图
				String detailImage = selfProductImage.getDetailImage();
				List<String> result = Arrays.asList(StringUtils.split(detailImage, ","));
				imagesList.addAll(result);
			}

			// 3.获取商品 id|商品名称|商品详情|商户名称|品牌介绍
			AdSelfProduct adSelfProduct = adSelfProductDao.getSelfProductDetailById(productId);

			// 4.获取商品规格|最低销售价|市场价(除去被屏蔽的sku)
			List<AdSelfProductSku> skuList = adSelfProductDao.getSelfProductSkuList(productId, limitSkuIdList,
					group.getId());
			if (CollectionUtils.isNotEmpty(skuList)) {
				AdSelfProductSku lowestPriceSku = skuList.get(0);
				// 比较商品特惠价跟销售价,哪个小显示哪个
				for (AdSelfProductSku productSku : skuList) {
					productSku.setSellPrice(this.getLowerPrice(productSku.getSpecialPrice(), productSku.getSellPrice()));
					//判断是否超出限购条件
					productSku.setOverBuyLimit(this.overBuyNumberLimit(productSku, userId));
				}
				adSelfProduct.setSellPrice(this.getLowerPrice(lowestPriceSku.getSpecialPrice(), lowestPriceSku.getSellPrice()));// 最低销售价
				adSelfProduct.setMarketPrice(lowestPriceSku.getMarketPrice());// 最低销售价对应的市场价
			}

			// 5.获取卡券类型
			List<AdSelfProductType> productTypeList = adSelfProductDao.getSelfProductTypeList(productId);

			// 6.获取可用积分
			BigDecimal availablePoint = adUserDao.getAvailablePoint(userId);

			map.put("code", MessageDataBean.success_code);
			map.put("selfProduct", adSelfProduct);
			map.put("skuList", skuList);
			map.put("productTypeList", productTypeList);
			map.put("availablePoint", availablePoint);
			map.put("imagesList", imagesList);

		} catch (Exception e) {
			logger.error("获取卡券商品详情页信息异常！！！", e);
		}
		return map;
	}

	//比较两个价格  返回较小的价格
	private String getLowerPrice(String specialPrice, String sellPrice) {
		String price = "";
		if (StringUtils.isNotBlank(specialPrice) && StringUtils.isNotBlank(sellPrice) && Double.valueOf(specialPrice) < Double.valueOf(sellPrice)) {
			price = specialPrice;
		} else {
			price = sellPrice;
		}
		return price;
	}

	//判断是否超出限购
	private boolean overBuyNumberLimit(AdSelfProductSku productSku, String userId) {
		//该用户该SKU限购数量
		Integer buyNumberLimit = productSku.getBuyNumberLimit();
		if(buyNumberLimit!=null){
			//查询用户所属企业id
			AdUser adUser = adUserDao.getById(Integer.valueOf(userId));
			String groupId = adUser!=null?adUser.getGroupNum().toString():"";
			logger.info("判断是否超出限购  groupId = " + groupId);
			//根据企业id跟skuId查询活动信息
			ActivityInfo activityInfo = adSelfProductDao.getActivityInfo(groupId,Integer.valueOf(productSku.getId()));
			String activityName = activityInfo!=null?activityInfo.getActivityName():"";
			logger.info("判断是否超出限购 activityName = " + activityName);
			//该用户该SKU已购数量
			int alreadyBuyNum = adOrderReportDao.getByNum(Long.valueOf(userId),"-" + productSku.getId(),activityName);
			logger.info(String.format("userId=%s用户,skuId=%s的商品，限购[%s]件 | 已购[%s]件", userId,productSku.getId(),buyNumberLimit,alreadyBuyNum));
			if(buyNumberLimit < 1 + alreadyBuyNum){
				return true;
			}
		}
		return false;
	}

	@Override
	public AdGroupSelfProductPrice getSelfProductSkuListByName(String activityName, Integer groupId) {
		return adSelfProductDao.getSelfProductSkuListByName(activityName, groupId);
	}

	@Override
	public List<AdGroupSelfProductPrice> getSelfProductAirport(String activityName) {
		return adSelfProductDao.getSelfProductAirport(activityName);
	}
}
