/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad	;

import com.doooly.business.product.entity.AdSelfProductImage;
import com.doooly.common.persistence.CrudDao;
import com.doooly.common.persistence.annotation.MyBatisDao;

/**
 * 商品图片管理DAO接口
 * @author wenwei.yang
 * @version 2017-09-20
 */
@MyBatisDao
public interface AdSelfProductImageDao extends CrudDao<AdSelfProductImage> {
	
	//根据商品id获取相关的商品主图&&详情图
	AdSelfProductImage getImageByProductId(String productId);
	
}