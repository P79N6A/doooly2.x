package com.doooly.dao.reachad;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdProduct;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AdProductDao extends BaseDaoI<AdProduct> {
	// 获取热门推荐里面的商品信息
	List<AdProduct> getProductsByHotRecommend(Date date);

	// 获取各个一级分类的下属商品
	List<AdProduct> findDataByCatagory(Integer catagoryId);

	// 获取各个二级分类的下属商品
	List<AdProduct> findDataByCatagorySecondId(@Param("catagoryId") Integer catagoryId, @Param("byType") Integer byType,
			@Param("firstTag") Integer firstTag);

	AdProduct getGuideDetail(Integer productId);

	List<AdProduct> getBenefitsProductList(String address);

	void updateHitsCount(AdProduct adProduct);

    int getTotalNum();//查询上架商品总数

    int getTotalNumv2(@Param("guideCategoryId") String guideCategoryId,@Param("recommendHomepage") String recommendHomepage);//查询上架商品总数

    List<AdProduct> getGuideProductList(@Param("orderType") String orderType, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    void updateSellCount(AdProduct adProduct);

    List<AdProduct> getGuideProductListv2(@Param("guideCategoryId") String guideCategoryId, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,@Param("recommendHomepage") String recommendHomepage);

}
