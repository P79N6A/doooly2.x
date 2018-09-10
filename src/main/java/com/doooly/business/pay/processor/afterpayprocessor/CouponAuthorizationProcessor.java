package com.doooly.business.pay.processor.afterpayprocessor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.business.order.vo.OrderVo;
import com.doooly.business.pay.bean.PayFlow;
import com.doooly.business.utils.DateUtils;
import com.doooly.common.constants.PropertiesConstants;
import com.doooly.common.util.HTTPSClientUtils;
import com.doooly.common.webservice.WebService;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdCouponCodeDao;
import com.doooly.dao.reachad.AdCouponDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dto.common.PayMsg;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdCoupon;
import com.doooly.entity.reachad.AdCouponCode;
import com.doooly.entity.reachad.AdUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 核销券
 */
@Component
public class CouponAuthorizationProcessor implements AfterPayProcessor{

    private static final String coupon_authorization_url = PropertiesConstants.dooolyBundle.getString("coupon_authorization_url");

    private static Logger logger = LoggerFactory.getLogger(CouponAuthorizationProcessor.class);
    @Autowired
    private AdCouponCodeDao  adCouponCodeDao;
    @Autowired
    private AdCouponDao adCouponDao;
    @Autowired
    private AdUserDao adUserDao;
    @Autowired
    private AdBusinessDao adBusinessDao;

    @Override
    public PayMsg process(OrderVo order, PayFlow payFlow, String realPayType) {
        logger.info("CouponAuthorizationProcessor start. orderNum = {}",order.getOrderNumber());
        try {
            String couponId = order.getCouponId();
            if(StringUtils.isNotEmpty(couponId)){
                AdCouponCode couponCode = adCouponCodeDao.get(couponId);
                AdCoupon coupon = adCouponDao.get(String.valueOf(couponCode.getCoupon()));
                AdUser user = adUserDao.getById(order.getUserId().intValue());
                AdBusiness adBusiness = adBusinessDao.get(coupon.getBusinessId());
                JSONArray array =  createRefundPointDetail(order.getItems().get(0));
                JSONObject json = new JSONObject();
                json.put("amount",order.getTotalMount());
                json.put("orderDetail",array);
                json.put("storesId",WebService.STOREID);
                json.put("orderNumber",order.getOrderNumber());
                json.put("serialNumber",order.getOrderNumber());
                json.put("modifyDateTime", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                json.put("couponCode",couponCode.getCode());
                json.put("businessId", adBusiness.getBusinessId());
                json.put("telephone",user.getTelephone());
                String resp = HTTPSClientUtils.sendPostNew(json.toJSONString(), coupon_authorization_url);
                logger.info("resp = {}",resp);
            }
        } catch (Exception e){
            e.printStackTrace();
            logger.info("CouponAuthorizationProcessor e = {}",e);
        }
        return null;
    }

    public static JSONArray createRefundPointDetail(OrderItemVo item) {
        JSONArray orderDetails = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("code", item.getId());
        json.put("goods", item.getGoods());
        json.put("number", item.getNumber());
        json.put("price", item.getPrice());
        json.put("category", item.getCategoryId());
        orderDetails.add(json);
        return orderDetails;
    }
}
