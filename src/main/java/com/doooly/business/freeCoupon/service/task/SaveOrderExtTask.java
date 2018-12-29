package com.doooly.business.freeCoupon.service.task;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.vo.OrderExtVo;
import com.doooly.business.order.vo.OrderItemVo;
import com.doooly.dao.reachad.AdOrderDeliveryDao;
import com.doooly.dao.reachad.AdOrderDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Description: 保存用户扩展信息
 * @author: qing.zhang
 * @date: 2018-12-26
 */
public class SaveOrderExtTask implements Callable<SaveOrderExtTask>{

    private static Logger logger = LoggerFactory.getLogger(SaveOrderExtTask.class);

    private JSONObject req;
    private OrderExtVo orderExt;
    private List<OrderItemVo> orderItem;
    @Autowired
    private AdOrderDeliveryDao adOrderDeliveryDao;
    @Autowired
    private AdOrderDetailDao adOrderDetailDao;

    public SaveOrderExtTask() {
    }

    public SaveOrderExtTask(JSONObject req, OrderExtVo orderExt, List<OrderItemVo> orderItem) {
        this.req = req;
        this.orderExt = orderExt;
        this.orderItem = orderItem;
    }

    @Override
    public SaveOrderExtTask call() throws Exception {
        // 方法进入时间
        Long startTime = System.currentTimeMillis();
        int rows = 0;
        Long orderId = req.getLong("orderId");
        if (orderExt != null) {
            rows += adOrderDeliveryDao.insert(orderId,orderExt);
        }
        rows += adOrderDetailDao.bantchInsert(orderId,orderItem);
        logger.info("保存订单商品信息====orderId:{},rows:{},成功发券执行耗时：{}" , orderId,rows,(System.currentTimeMillis() - startTime));
        return null;
    }
}
