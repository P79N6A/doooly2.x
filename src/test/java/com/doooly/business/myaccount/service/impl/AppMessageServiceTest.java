package com.doooly.business.myaccount.service.impl;

import com.doooly.common.constants.AppConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description:
 * @author: qing.zhang
 * @date: 2017-09-14
 */
@RunWith(SpringJUnit4ClassRunner.class)
//配置了@ContextConfiguration注解并使用该注解的locations属性指明spring和配置文件之后，
@ContextConfiguration(locations = {"classpath:conf/mybatis-config.xml",
        "classpath:conf/spring-hessian.xml",
        "classpath:conf/spring-jersey.xml",
        "classpath:conf/spring-mybatis.xml",
        "classpath:conf/spring-mybatis-reachad.xml",
        "classpath:conf/spring-mybatis-reachlife.xml",
        "classpath:conf/spring-redis.xml",
        "classpath:conf/spring-task.xml" })
public class AppMessageServiceTest {

    @Autowired
    private AppMessageService appMessageService;

    @Test
    public void sendMessage() throws Exception {
        //appMessageService.sendMessage("test====ios=====false",1,false);
        appMessageService.sendMessage("测试发送消息", "47883", AppConstants.ANDROID_SEND, AppConstants.IOS_ENVIRONMENT);
        //DeviceClient deviceClient = new DeviceClient(AppConstants.MASTER_SECRET, AppConstants.APP_KEY, null, ClientConfig.getInstance());
        //appMessageService.ifBindAlias(deviceClient,"47883");
    }

}