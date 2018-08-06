package com.doooly.business.nexus;

import com.doooly.business.order.vo.OrderVo;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdUser;

/**
 * 全家集享卡-积分交易接口
 * Created by WANG on 2018/7/20.
 */
public interface NexusSerivce {

    public MessageDataBean consume(OrderVo order,AdUser adUser) ;
}
