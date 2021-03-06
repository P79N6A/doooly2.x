package com.doooly.dao.payment;

import com.doooly.common.persistence.annotation.MyBatisDao;
import com.doooly.entity.payment.PayRecordDomain;

import java.util.List;

/**
 * @Auther: liangjun
 * @Date: 2018/5/29 10:46
 * @Description:
 */
@MyBatisDao
public interface PayRecordMapper {

    PayRecordDomain getPayRecordDomain(PayRecordDomain payRecordDomain);


    List<PayRecordDomain> getNeedCancaelMerchantOrder();

}
