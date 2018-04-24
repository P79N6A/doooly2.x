package com.doooly.business.product.service.impl;

import com.doooly.business.product.service.ProductCouponService;
import com.doooly.dao.reachad.AdSelfProductCouponDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.entity.reachad.AdSelfProductCoupon;
import com.doooly.entity.reachad.AdUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductCouponServiceImpl implements ProductCouponService {
    @Autowired
    private AdSelfProductCouponDao adSelfProductCouponDao;
    @Autowired
    private AdUserDao adUserDao;

    /**
     * 获取卡号卡密，同时修改自营卡券状态
     * @param orderId 订单id
     * @param userId 用户id
     * @param productSkuId 商品skuid
     * @return
     */
    @Override
    public AdSelfProductCoupon getProductCoupon(String orderId, Long userId, String productSkuId) {
        AdSelfProductCoupon productCoupon = adSelfProductCouponDao.getProductCoupon(productSkuId);
        if(productCoupon!=null){
            AdUser adUser = adUserDao.selectByPrimaryKey(userId);
            productCoupon.setOrderId(orderId);
            productCoupon.setPurchaseStatus(2);
            productCoupon.setTelephone(adUser.getTelephone());
            productCoupon.setUserCardNumber(adUser.getCardNumber());
            adSelfProductCouponDao.update(productCoupon);
        }
        return productCoupon;
    }
}
