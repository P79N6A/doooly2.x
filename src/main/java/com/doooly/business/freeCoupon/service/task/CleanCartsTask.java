package com.doooly.business.freeCoupon.service.task;

import com.alibaba.fastjson.JSONObject;
import com.doooly.common.constants.Constants;
import com.doooly.common.util.HttpClientUtil;
import com.doooly.dto.common.MessageDataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 清空购物车
 * @author: qing.zhang
 * @date: 2019-04-10
 */
public class CleanCartsTask implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(CleanCartsTask.class);

    private JSONObject req;

    public CleanCartsTask() {
    }

    public CleanCartsTask(JSONObject req) {
        this.req = req;
    }


    @Override
    public void run() {
        // 方法进入时间
        logger.info("清空购物车商品====开始");
        Long startTime = null;
        try {
            startTime = System.currentTimeMillis();
            JSONObject result = HttpClientUtil.httpPost(Constants.OrderApiConstants.ORDER_BASE_URL + Constants.OrderApiConstants.SHOP_CART_URL, req);
            if(result!=null && MessageDataBean.success_code.equals(result.get("code"))){
                logger.info("购物车清空成功,返回结果{}",result);
            }else {
                //购物车清空失败
                logger.error("购物车清空失败,返回结果{}",result);
            }
        } catch (Exception e) {
            logger.info("保存订单商品信息出错，错误原因" , e);
        }
        logger.info("清空购物车商品====完成耗时"+(System.currentTimeMillis()-startTime));
    }
}
