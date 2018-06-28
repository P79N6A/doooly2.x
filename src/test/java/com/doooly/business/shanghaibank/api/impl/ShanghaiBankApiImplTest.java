package com.doooly.business.shanghaibank.api.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

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
public class ShanghaiBankApiImplTest {

    @Autowired
    private ShanghaiBankApiImpl shanghaiBankApi;

    @Test
    public void c19SCrVirSingleAccount() throws Exception {
        String result = shanghaiBankApi.c19SCrVirSingleAccount("testGroup2");
        System.out.println(result);
    }

    @Test
    public void c19SCrVirSingleAccountStInq() throws Exception {
        String result = shanghaiBankApi.c19SCrVirSingleAccountStInq("睿渠轧差虚账户");
        System.out.println(result);
    }

    /**
     * 虚账户余额查询接口
     * @throws Exception
     */
    @Test
    public void c19VirAcctBlanceInq() throws Exception {
        try {
            String res = shanghaiBankApi.c19VirAcctBlanceInq("3111110300286719100051");
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void c19VirWithDrawalsInq() throws Exception {
        String res = shanghaiBankApi.c19VirWithDrawalsInq("fasdljfdskaf","3111110300286719100014","10.02","提款操作");
        System.out.println(res);
    }


    @Test
    public void c19VirAmtFrozOrNot() throws Exception {
        String res = shanghaiBankApi.c19VirAmtFrozOrNot("fasdljfdskaf","3111110300286719100014","1","10.02","提款操作","冻结testGroup2");
        System.out.println(res);
    }

    @Test
    public void c19SingleCharge() throws Exception {
        String res = shanghaiBankApi.c19SingleCharge("fasdljfdskaf","3111110300286719100014","11.00","1235324432","上海睿渠","上海银行浦东分行","提款操作","虚账户代缴");
        System.out.println(res);
    }

    /**
     * 虚账户代发
     * @throws Exception
     */
    @Test
    public void c19VirSReTrigSer() throws Exception {
        String res = shanghaiBankApi.c19VirSReTrigSer("fasdljfdskafasdf111122","3111110300286719100051","1000.00","03002792221","测试客户122120","打款操作","虚账户代发");
        System.out.println(res);
    }

    @Test
    public void c19VirAcctDeleCharSttInq() throws Exception {
        String res = shanghaiBankApi.c19VirAcctDeleCharSttInq("fasdljfdskafasdf","RUIQUVIR");
        System.out.println(res);
    }

    @Test
    public void c19VirAcctPrintReceipt() throws Exception {
        shanghaiBankApi.c19VirAcctPrintReceipt("3111110300286719100013","2018-05-28 00:00:00","2018-05-28 00:00:00","fasdljfdskafasdf");
    }

}