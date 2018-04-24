package com.doooly.dao.reachad;

import java.util.List;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdProductCategory;

public interface AdProductCategoryDao extends BaseDaoI<AdProductCategory> {
	// 获取商城一级标签(如热门推荐等)
	List<AdProductCategory> findFirstCategory();

	// 获取对应一级标签里面的二级标签信息
	List<AdProductCategory> findSecondCategory(Integer catagoryId, Integer type);

}
