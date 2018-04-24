package com.doooly.business.product.service;

import com.doooly.entity.reachad.AdSelfProductCoupon;

public interface ProductCouponService {

    AdSelfProductCoupon getProductCoupon(String orderId,Long userId,String productSkuId);
}
