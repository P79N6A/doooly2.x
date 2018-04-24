package com.doooly.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdUserServiceI;
import com.doooly.entity.reachad.AdUser;

@RunWith(SpringJUnit4ClassRunner.class)
//配置了@ContextConfiguration注解并使用该注解的locations属性指明spring和配置文件之后，
@ContextConfiguration(locations = {"classpath:conf/spring-mybatis.xml","classpath:conf/spring-mybatis-reachad.xml" })
public class AdUserServiceTest {

    //注入userService
    @Autowired
    private AdUserServiceI adUserService;
    
    @Test
    public void save(){
        AdUser user = new AdUser();
      
       try {
		JSONObject count =  adUserService.batchSendSms(user, null, null, null, null,true);
		    System.out.println(count);
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}