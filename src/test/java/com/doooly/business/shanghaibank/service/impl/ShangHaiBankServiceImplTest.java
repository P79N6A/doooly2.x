package com.doooly.business.shanghaibank.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description: 上海银行接口单元测试
 * @author: qing.zhang
 * @date: 2018-05-28
 */
@RunWith(SpringJUnit4ClassRunner.class)
//配置了@ContextConfiguration注解并使用该注解的locations属性指明spring和配置文件之后，
@ContextConfiguration(locations = {"classpath:conf/mybatis-config.xml",
        "classpath:conf/spring-hessian.xml",
        "classpath:conf/spring-jersey.xml",
        "classpath:conf/spring-mybatis.xml",
        "classpath:conf/spring-mybatis-reachad.xml",
        "classpath:conf/spring-mybatis-reachlife.xml",
        "classpath:conf/spring-mybatis-report.xml",
        "classpath:conf/spring-mybatis-payment.xml",
        "classpath:conf/spring-redis.xml",
        "classpath:conf/spring-task.xml"})
public class ShangHaiBankServiceImplTest {

    @Autowired
    private ShangHaiBankServiceImpl shangHaiBankService;

    @Test
    public void c19SCrVirSingleAccount() throws Exception {
        shangHaiBankService.c19SCrVirSingleAccount("上海睿渠测试2",1,"111",111L);
    }

    @Test
    public void c19SCrVirSingleAccountStInq() throws Exception {

    }

    @Test
    public void c19VirAcctBlanceInq() throws Exception {

    }

    @Test
    public void c19VirWithDrawalsInq() throws Exception {

    }

    @Test
    public void c19VirAmtFrozOrNot() throws Exception {

    }

    @Test
    public void c19SingleCharge() throws Exception {

    }

    @Test
    public void c19VirSReTrigSer() throws Exception {

    }

    @Test
    public void c19VirAcctDeleCharSttInq() throws Exception {

    }

    @Test
    public void c19VirAcctPrintReceipt() throws Exception {

    }

}