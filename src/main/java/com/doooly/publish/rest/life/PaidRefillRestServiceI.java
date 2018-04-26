package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.spi.container.ContainerRequest;

/**
 * @Description: 话费充值立即参与
 * @author: wenwei.yang
 * @date: 2018-04-16
 */
public interface PaidRefillRestServiceI {

    String paidRefill(JSONObject json);//立即参与

}
