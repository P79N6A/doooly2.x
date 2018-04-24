package com.doooly.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doooly.entity.doooly.DLChannel;
import com.doooly.service.doooly.DLChannelServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
//配置了@ContextConfiguration注解并使用该注解的locations属性指明spring和配置文件之后，
@ContextConfiguration(locations = {"classpath:conf/spring-mybatis.xml" })
public class MyBatisTestBySpringTestFramework {

    //注入userService
    @Autowired
    private DLChannelServiceI dlchannelService;
    
    @Test
    public void save(){
        DLChannel channel = new DLChannel();
        channel.setChannelName("LYF_APP");
        channel.setChannelPwd("pwd");
        channel.setCreateDate(new Date());
        channel.setModifyDate(new Date());
//        channel.setDelFlag(true);
        channel.setIpWhiteLists("localhost");
        channel.setReqMaxNum(10);
       int count =  dlchannelService.insert(channel);
        System.out.println(count);
    }
    @Test
    public void testPage(){
//    	List<DLChannel> list = dlchannelService.queryByPage(2, 2);
//    	for(DLChannel c :list){
//    		System.out.println(c.getChannelName());
//    	}
    }
}