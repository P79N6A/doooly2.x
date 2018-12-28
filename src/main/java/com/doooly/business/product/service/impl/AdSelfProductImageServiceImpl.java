package com.doooly.business.product.service.impl;

import com.doooly.business.product.entity.AdSelfProductImage;
import com.doooly.business.product.service.AdSelfProductImageServiceI;
import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.reachad.AdSelfProductImageDao;
import com.reach.redis.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2018-12-26
 */
@Service
public class AdSelfProductImageServiceImpl implements AdSelfProductImageServiceI {

    @Autowired
    private AdSelfProductImageDao adSelfProductImageDao;

    @Cacheable(module = "ADSELFPRODUCTIMAGESERVICEIMPL", event = "getImageByProductId", key = "productId",
            expires = RedisConstants.REDIS_CACHE_EXPIRATION_DATE, required = true)
    @Override
    public AdSelfProductImage getImageByProductId(Map<String, Object> paramMap) {
        String productId = (String) paramMap.get("productId");
        return adSelfProductImageDao.getImageByProductId(productId);
    }
}
