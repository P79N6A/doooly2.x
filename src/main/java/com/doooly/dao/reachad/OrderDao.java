package com.doooly.dao.reachad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.Order;
import com.doooly.entity.reachad.OrderDetail;

/**
 * _order订单表DAO
 * 
 * @author tangzhiyuan
 *
 */
public interface OrderDao extends BaseDaoI<Order> {

	/**
	 * 获取订单近两周（包括今天）数目 i是今两周日期遍历的变量
	 */
	public Integer getCountByWeek(Integer i);

	/**
	 * 获取订单近六个月数目 i 是近六个月遍历月份变量
	 */
	public Integer getCountByMonth(Integer i);

	/**
	 * 查询订单信息（总应付，总实付）
	 * 
	 * @param userId
	 * @param bussinessId
	 * @param orderNumber
	 * @return
	 */
	Order getOrderInfoByOrderNumber(@Param("userId") Long userId, @Param("bussinessId") String bussinessId,
			@Param("orderNumber") String orderNumber);
	/**
	 * 查看是否在活动期前下过非零订单
	 * 
	 * @param userId
	 * @param date 
	 * @param bussinessId
	 * @param orderNumber
	 * @return
	 */
	public Order getPaidOrder(@Param("userId") Integer userId,@Param("beginDate")  Date beginDate);

	public List<Order> getNewestOrder(@Param("userId") Integer userId,@Param("beginDate")  Date beginDate,@Param("endDate") Date endDate);
	
	/** 根据订单号查询订单总金额 */
	Order getTotalByOrderNumber(String orderNumber);
	
	/** 根据流水号查询订单总金额 */
	Order getTotalBySerialNumber(String serialNumber);
	
	/**
	 * 兜礼自营订单同步到_orde
	 * @param orderNumber
	 * @return
	 */
	int insert(Order order);
	int insertDetail(OrderDetail orderDetail);
	int updateById(Order order);
	int updateBussinessIdByNum(@Param("orderNumber") String orderNumber,@Param("bussinessId") String bussinessId);

	/**
	 * 用户完成的订单数量
	 * @param userId
	 * @return
	 */
	int getFinishedOrderCnt(@Param("userId") String userId);

	public Order getTotalByOrderNumberByTypeFive(String orderNumber);

    //查询已同步订单
    Order getSyncOrder(Order o);

    //计算退货返佣返利
    void computeRefundRebateAndSyncOrder(Map<String, Object> paramMap);

    List<Order> findList(Order order);
}
