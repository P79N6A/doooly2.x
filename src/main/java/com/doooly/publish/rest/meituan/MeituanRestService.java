package com.doooly.publish.rest.meituan;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.payment.bean.ResultModel;
import com.doooly.dto.common.OrderMsg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/14.
 */
public interface MeituanRestService {

    Map<String,Object> easyLogin(@Context HttpServletRequest request);

}
