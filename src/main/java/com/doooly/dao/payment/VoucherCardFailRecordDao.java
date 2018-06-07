package com.doooly.dao.payment;


import org.apache.ibatis.annotations.Param;

import com.doooly.common.persistence.annotation.MyBatisDao;
import com.doooly.entity.payment.VoucherCardFailRecord;

/**
 * @Auther: 杨汶蔚	
 * @Date: 2018/6/4 	
 * @Description:
 */
@MyBatisDao
public interface VoucherCardFailRecordDao {

	int insert(VoucherCardFailRecord record);

	Integer find24HourFailDataCount(@Param("telephone")String telephone);


}
