package com.doooly.business.common.service.impl;

import com.doooly.business.common.service.AdCouponCodeServiceI;
import com.doooly.common.constants.RedisConstants;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachlife.LifeGiftOrderDao;
import com.doooly.dao.reachlife.LifeProductDao;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachlife.LifeGiftOrder;
import com.doooly.entity.reachlife.LifeMember;
import com.doooly.entity.reachlife.LifeProduct;
import com.reach.redis.annotation.Cacheable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lxl
 */
@Service
public class AdCouponCodeService implements AdCouponCodeServiceI {
	
	/** 日志  */
	private Log logger = LogFactory.getLog(this.getClass());
	
	/** 优惠码DAO */
	@Autowired
	private AdCouponCodeDao adCouponCodeDao;
	
	/** 福利DAO */
	@Autowired
	private LifeGiftOrderDao lifeGiftOrderDao;
	
	/** 商品DAO */
	@Autowired
	private LifeProductDao lifeProductDao;

	@Override
	public HashMap<String, Object> giveCouponCode(LifeProduct lifeProduct,LifeMember lifeMember) throws Exception{
		//返回信息
		HashMap<String, Object> infoMap = new HashMap<String, Object>();
		//优惠码参数
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		try {
			dataMap.put("userId", Integer.valueOf(lifeMember.getAdId()));
			dataMap.put("productSN", lifeProduct.getSn());
			dataMap.put("couponCodeId", 0);
			adCouponCodeDao.excuteSendGiftCouponProc(dataMap);
			int couponCodeId = Integer.valueOf(dataMap.get("couponCodeId").toString());
			logger.info("=====更新兑换码userID" + lifeMember.getAdId() + ",产品SN:" + lifeProduct.getSn() + ",couponCodeId:" + couponCodeId );
			if (couponCodeId > 0) {
				AdCouponCode couponCode = adCouponCodeDao.get(String.valueOf(couponCodeId));
				logger.info("=====giveCoupon分发兑换码couponCode" + couponCode);
				if (couponCode != null) {
					// 同步到A系统表life_gift_order
					LifeGiftOrder gift = new LifeGiftOrder();
					Date date = new Date();
					gift.setCode(couponCode.getCode());
					gift.setSn(lifeProduct.getSn());
					gift.setProduct(Long.valueOf(lifeProduct.getId()));
					gift.setCreateDate(date);
					gift.setReceiveDate(date);
					gift.setModifyDate(date);
					// 领取未使用
					gift.setStatus(0);
					gift.setMember(Long.valueOf(lifeMember.getId()));
					lifeGiftOrderDao.insert(gift);
					// 更改库存
					String newStock = String.valueOf(Integer.valueOf(lifeProduct.getStock()) - 1);
					lifeProduct.setStock(newStock);
					lifeProduct.setModifyDate(new Date());
					lifeProductDao.update(lifeProduct);
					infoMap.put("code", "1000");
					infoMap.put("msg", "操作成功");
					infoMap.put("couponCode", couponCode.getCode());
				} else {
					infoMap.put("code", "1007");
					infoMap.put("msg", "未查询到优惠码数据");
				}
			}else {
				infoMap.put("code", "1008");
				infoMap.put("msg", "更新优惠码用户失败");
			}
		} catch (Exception e) {
			infoMap.put("code", "5000");
			infoMap.put("msg", "系统错误");
			e.printStackTrace();
			throw new RuntimeException();
		}
		return infoMap;
	}

    @Override
    public AdCouponCode getSelfCouponByMap(Map<String, Object> paramMap) {
        return adCouponCodeDao.getSelfCouponByMap(paramMap);
    }
}
