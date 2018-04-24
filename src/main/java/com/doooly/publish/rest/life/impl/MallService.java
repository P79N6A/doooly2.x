package com.doooly.publish.rest.life.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.mall.service.MallBusinessServiceI;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdProduct;
import com.doooly.entity.reachad.AdProductCategory;
import com.doooly.publish.rest.life.MallServiceI;

@Component
@Path("/mall")
public class MallService implements MallServiceI {
	@Autowired
	private MallBusinessServiceI mallBusinessServiceI;
	private static final Logger logger = LoggerFactory.getLogger(MallService.class);

	/** 获取商城一级菜单 */
	@POST
	@Path(value = "/getCategoryList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getCategoryList() {
		MessageDataBean messageDataBean = new MessageDataBean();
		List<AdProductCategory> list = mallBusinessServiceI.getCategoryList();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (list != null && !list.isEmpty()) {
			map.put("firstCategoryList", list);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} else {
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	/** 获取二级菜单 商品信息 以及商品分类信息 */
	@POST
	@Path(value = "/getPageData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getPageData(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Integer catagoryId = json.getInteger("id");
			Integer type = json.getInteger("type");
			Integer adId = json.getInteger("AdId");
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (adId == null) {
				adId = 0;
			}
			switch (type) {
			// 热门推荐
			case 0:
				map = mallBusinessServiceI.getHotProductDatas(catagoryId, type, adId);
				break;
			// 热门商家
			case 1:
				map = mallBusinessServiceI.getHotMerchantDatas(catagoryId, type, adId);
				break;
			// 品牌馆
			case 2:
				map = mallBusinessServiceI.getBrandDatas(catagoryId, type, adId);
				break;
			// 详细分类
			case 3:
				map = mallBusinessServiceI.getCatagoryProductDatas(catagoryId, type, adId);
				break;
			default:
				break;
			}
			if (!map.isEmpty()) {
				messageDataBean.setCode(MessageDataBean.success_code);
				messageDataBean.setData(map);
			} else {
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		logger.info(messageDataBean.toJsonString());
		return messageDataBean.toJsonString();
	}

	/** 获取二级菜单 商品信息 以及商品分类信息 */
	@POST
	@Path(value = "/toSecondMall")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String toSecondMall(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		// 详细分类下的二级标签id
		try {
			Integer secondId = json.getInteger("secondId");
			// 二级页面规定的三个分类(0按最新,1按销量,2按折扣度)
			Integer byType = json.getInteger("byType");
			// 热门推荐下的二级标签id
			Integer firstTag = json.getInteger("firstTag");
			if (byType == null) {
				byType = 0;
			}
			if (secondId == null) {
				secondId = 0;
			}
			if (firstTag == null) {
				firstTag = 0;
			}
			List<AdProduct> adProducts = mallBusinessServiceI.getProductsByCatagorySecondId(secondId, byType, firstTag);
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (adProducts != null && !adProducts.isEmpty()) {
				map.put("adProductList", adProducts);
				messageDataBean.setCode(MessageDataBean.success_code);
				messageDataBean.setData(map);
			} else {
				messageDataBean.setCode(MessageDataBean.no_data_code);
			}
			logger.info(messageDataBean.toJsonString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	// 获取商品返利
	@POST
	@Path(value = "/getRebate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getRebate(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 商品id
			Integer productId = json.getInteger("productId");
			// 商品应付款
			BigDecimal shouldPrice = json.getBigDecimal("shouldPrice");
			// 商品实付款
			BigDecimal factPrice = json.getBigDecimal("factPrice");
			messageDataBean = mallBusinessServiceI.getRebate(productId, shouldPrice, factPrice);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	// 获取品牌馆详情
	@POST
	@Path(value = "/businessInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getBusinessInfo(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Long adBusinessId = json.getLong("adBusinessId");
			Long userId = json.getLong("userId");
			messageDataBean = mallBusinessServiceI.getBusinessInfo(userId,adBusinessId);
			logger.info(messageDataBean.toJsonString());
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	// 获取品牌馆详情
	@POST
	@Path(value = "/brandInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getBrandInfo(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Long adBrandId = json.getLong("id");
			messageDataBean = mallBusinessServiceI.getBrandInfo(adBrandId);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/getStoreList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getBusinessStoreList(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Integer businessId = json.getInteger("businessId");
			Double lat = json.getDouble("lat");
			Double lng = json.getDouble("lng");
			Integer currentPage = json.getInteger("currentPage");
			Integer pageSize = json.getInteger("pageSize");
			messageDataBean = mallBusinessServiceI.getBusinessStoreList(businessId,lat,lng,currentPage,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/getGuideDetail")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getGuideDetail(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Integer productId = json.getInteger("productId");
			Integer userId = json.getInteger("userId");
			Integer businessId = json.getInteger("businessId");
			messageDataBean = mallBusinessServiceI.getGuideDetail(userId,productId,businessId);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

	@POST
	@Path(value = "/hotBusiness")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String gethotBusiness(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Integer userId = json.getInteger("userId");
			String address = json.getString("address");
			logger.info("热门商家的地域信息为============="+address);
			messageDataBean = mallBusinessServiceI.gethotBusiness(userId,address);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}
	
	@POST
	@Path(value = "/hotBusinessForWuSteel")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String gethotBusinessForWuSteel(JSONObject json) {
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			Integer userId = json.getInteger("userId");
			String address = json.getString("address");
			logger.info("热门商家的地域信息为============="+address);
			messageDataBean = mallBusinessServiceI.gethotBusinessForWuSteel(userId,address);
		} catch (Exception e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean.toJsonString();
	}

}
