package com.doooly.service;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doooly.entity.doooly.DLChannel;
import com.doooly.service.doooly.DLChannelServiceI;

public class dlChannelServiceTest {

    //注入userService
    @Autowired
    private DLChannelServiceI dlchannelService;
    
    @Test
    public void selectByPrimaryKey(){
    	try {
			//使用"spring.xml"和"spring-mybatis.xml"这两个配置文件创建Spring上下文
			ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"conf/spring-mybatis.xml"});
			//从Spring容器中根据bean的id取出我们要使用的userService对象
			dlchannelService = (DLChannelServiceI) ac.getBean("DLChannelService");
			String channelName = "doooly_pc";
//			DLChannel dlChannel = dlchannelService.selectByPrimaryKey(channelName);
//			System.out.println(dlChannel.getIpWhiteLists());
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}