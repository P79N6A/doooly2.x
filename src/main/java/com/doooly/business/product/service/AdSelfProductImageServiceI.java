package com.doooly.business.product.service;

import com.doooly.business.product.entity.AdSelfProductImage;

import java.util.Map;

/**
 * @Description: ad_self_product_image表service
 * @author: qing.zhang
 * @date: 2018-12-26
 */
public interface AdSelfProductImageServiceI {

    //根据商品id获取相关的商品主图&&详情图
    AdSelfProductImage getImageByProductId(Map<String, Object> paramMap);
}
