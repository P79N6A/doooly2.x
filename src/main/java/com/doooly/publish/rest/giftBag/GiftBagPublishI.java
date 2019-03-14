package com.doooly.publish.rest.giftBag;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * 礼包相关接口，这是兜礼礼包，不是企业礼包
 *
 * @Author: Mr.Wu
 * @Date: 2019/3/8
 */
public interface GiftBagPublishI {

     /**
      * 获得当前企业所有兜礼礼包
      * @param json
      * @return
      */
     String getAllGiftBagByGroup(JSONObject json, @Context HttpServletRequest request);

     /**
      * 获得礼包商品
      * @param json
      * @return
      */
     String getGiftBagProduct(JSONObject json);
}
