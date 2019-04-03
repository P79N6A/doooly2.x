package com.doooly.business.freeCoupon.service.task;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.vo.OrderExtVo;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.dao.reachad.AdOrderDeliveryDao;
import com.doooly.dao.reachad.AdOrderDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description: 保存用户扩展信息
 * @author: qing.zhang
 * @date: 2018-12-26
 */
public class SaveOrderExtTask implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(SaveOrderExtTask.class);

    private JSONObject req;
    private OrderExtVo orderExt;
    private List<OrderItemVo> orderItem;
    private AdOrderDeliveryDao adOrderDeliveryDao;
    private AdOrderDetailDao adOrderDetailDao;

    public SaveOrderExtTask() {
    }

    public SaveOrderExtTask(JSONObject req, OrderExtVo orderExt, List<OrderItemVo> orderItem,
                            AdOrderDeliveryDao adOrderDeliveryDao,AdOrderDetailDao adOrderDetailDao) {
        this.req = req;
        this.orderExt = orderExt;
        this.orderItem = orderItem;
        this.adOrderDeliveryDao = adOrderDeliveryDao;
        this.adOrderDetailDao = adOrderDetailDao;
    }


    @Override
    public void run() {
        // 方法进入时间
        logger.info("保存订单商品信息====orderId:{},开始");
        Long startTime = null;
        int rows = 0;
        Long orderId = null;
        try {
            startTime = System.currentTimeMillis();
            rows = 0;
            orderId = req.getLong("orderId");
            if (orderExt != null) {
                rows += adOrderDeliveryDao.insert(orderId,orderExt);
            }
            rows += adOrderDetailDao.bantchInsert(orderId,orderItem);
        } catch (Exception e) {
            logger.info("保存订单商品信息出错，错误原因" , e);
        }
        logger.info("保存订单商品信息====orderId:{},rows:{},成功保存订单执行耗时：{}" , orderId,rows,(System.currentTimeMillis() - startTime));
    }
}
