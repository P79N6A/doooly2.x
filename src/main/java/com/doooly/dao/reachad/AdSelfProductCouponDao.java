/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdSelfProductCoupon;

/**
 * 兜礼自营电子卡券DAO接口
 * @author qing.zhang
 * @version 2018-04-08
 */
public interface AdSelfProductCouponDao {

    AdSelfProductCoupon getProductCoupon(String productSkuId);

    void update(AdSelfProductCoupon productCoupon);
}