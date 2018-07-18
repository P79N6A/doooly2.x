package com.doooly.business.exwings;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.vo.OrderVo;

/**
 * Created by WANG on 2018/7/11.
 */
public interface ExWingsService {

    public final static String MOBIKE_SUCCESS_CODE = "0";

    public JSON balance();

    public JSONObject recharge(OrderVo order);
}
