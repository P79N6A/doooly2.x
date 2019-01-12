package com.doooly.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doooly.dao.reachad.AdUserIntegralDao;
import com.doooly.entity.reachad.AdUserIntegral;

@RunWith(SpringJUnit4ClassRunner.class)
//配置了@ContextConfiguration注解并使用该注解的locations属性指明spring和配置文件之后，
@ContextConfiguration(locations = {"classpath:conf/*.xml" })
public class AdUserIntegralDaoTest {

    @Autowired
    private AdUserIntegralDao userIntegralDao;
    
    @Test
    public void query(){
      
       try {
    	   AdUserIntegral user = userIntegralDao.getDirIntegralByUserId(1160658L);
		    System.out.println(user);
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
}