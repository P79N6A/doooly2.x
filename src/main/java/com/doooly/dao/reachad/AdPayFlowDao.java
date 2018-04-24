package com.doooly.dao.reachad;

import org.apache.ibatis.annotations.Param;

import com.doooly.business.pay.bean.PayFlow;

public interface AdPayFlowDao {
	
    int insert(PayFlow record);

    PayFlow selectByPrimaryKey(Long id);
    
    PayFlow getByOrderNum(@Param("orderNum")String orderNum,@Param("payType")String payType,@Param("payStatus") String payStatus);
    
    PayFlow getByTransNo(@Param("transNo")String transNo,@Param("payType")String payType);

    int updateByPrimaryKeySelective(PayFlow record);
    
    PayFlow getByTransNum(String transNum);
    
    PayFlow getById(String id);

}