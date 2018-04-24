package com.doooly.business.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.order.service.CpsSummaryServiceI;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.OrderCpsDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dao.reachad.ReMerchantCpsDao;
import com.doooly.dao.reachad.ReMerchantCpsDetailDao;
import com.doooly.entity.reachad.Order;
import com.doooly.entity.reachad.OrderCps;
import com.doooly.entity.reachad.ReMerchantCps;
import com.doooly.entity.reachad.ReMerchantCpsDetail;

/**
 * 商家营销补贴汇总类
 * 
 * @author tangzhiyuan
 * @date 2016年12月14日
 * @version 1.0
 */
@Service
@Transactional
public class CpsSummaryService implements CpsSummaryServiceI {

	private static Logger log = Logger.getLogger(CpsSummaryService.class);

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderCpsDao orderCpsDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private ReMerchantCpsDao reMerchantCpsDao;
	@Autowired
	private ReMerchantCpsDetailDao reMerchantCpsDetailDao;

	/**
	 * 更新订单营销补贴
	 */
	@Override
	public void updateCpsFee(String orderJson) {
		/*
		 * 数据格式： { "orderNumber":"201612CP29109380", "type":1,
		 * "bussinessId":"TEST_bfd7ca1e9569a33172e24c0649fdae8e",
		 * "cardNumber":"11111111111" }
		 */
		try {
			JSONObject orderObject = JSONObject.parseObject(orderJson);
			// 解析json
			int type = orderObject.getIntValue("type");// 交易阶段 1：完成订单 5：退货
			String orderNumber = orderObject.getString("orderNumber");// 订单号
			String bussinessId = orderObject.getString("bussinessId");// 商家bussinessId
			String phoneOrCard = orderObject.getString("cardNumber");// 会员卡号或手机号

			if (type == 1) {
				// 完成订单
				Long userId = adUserDao.getIdByPhoneOrCard(phoneOrCard);
				Long adBusinessId = adBusinessDao.getAdBusinessId(bussinessId);

				ReMerchantCps reMerchantCps = reMerchantCpsDao.getCpsByBusinessId(adBusinessId);

				if (reMerchantCps != null && reMerchantCps.getSetStatus() == 1) {
					// 商家已设置营销补贴规则
					Long merchantCpsId = reMerchantCps.getId();
					List<List<ReMerchantCpsDetail>> allOrderCpsDetailList = getCpsDetailList(merchantCpsId);

					int cpsStatus = reMerchantCps.getCpsStatus();
					if (cpsStatus == 0) {
						// 固定价格
						List<ReMerchantCpsDetail> orderCpsDetailList = new ArrayList<>();
						for (List<ReMerchantCpsDetail> list : allOrderCpsDetailList) {
							// 固定价格只取一个配置项
							orderCpsDetailList.add(list.get(0));
						}

						// 商家CPS设置优惠订单数
						int orderCpsCount = orderCpsDetailList.size();

						// 用户已享受优惠订单数
						Integer maxCpsNumber = orderCpsDao.getMaxCpsNumber(userId, adBusinessId);

						// 当前优惠订单数
						int currentCpsNumber = 0;
						if (maxCpsNumber != null && maxCpsNumber != 0) {
							currentCpsNumber = maxCpsNumber + 1;
						} else {
							currentCpsNumber = 1;
						}

						if (currentCpsNumber <= orderCpsCount) {
							ReMerchantCpsDetail cpsDetail = orderCpsDetailList.get(currentCpsNumber - 1);
							BigDecimal thresholdValue = cpsDetail.getThresholdValue();

							Order order = orderDao.getOrderInfoByOrderNumber(userId, bussinessId, orderNumber);

							int countType = cpsDetail.getCountType();
							if (countType == 0) {
								// 按应付
								BigDecimal totalPrice = order.getTotalPrice();

								if (totalPrice.compareTo(thresholdValue) == -1) {
									currentCpsNumber = 0;
								}
							} else if (countType == 1) {
								// 按实付
								BigDecimal totalAmount = order.getTotalAmount();
								if (totalAmount.compareTo(thresholdValue) == -1) {
									currentCpsNumber = 0;
								}
							}
						} else {
							currentCpsNumber = 0;
						}

						// 商家营销费用
						BigDecimal cpsFee = null;
						if (currentCpsNumber == 0) {
							cpsFee = new BigDecimal("0.00");
						} else {
							cpsFee = orderCpsDetailList.get(currentCpsNumber - 1).getCpaFee();
						}

						OrderCps orderCps = orderCpsDao.getByOrderNumber(userId, adBusinessId, orderNumber);
						log.info("更新 or 插入 orderCps记录");
						// 是否存在记录
						if (orderCps != null) {
							Integer cpsNumber = orderCps.getCpsNumber();
							if (cpsNumber != null && cpsNumber > 0) {
								log.info("订单：" + orderNumber + "已享受过商家优惠");
								return;
							}

							orderCps.setCpsFee(cpsFee);
							orderCps.setCpsNumber(currentCpsNumber);
							orderCps.setUpdateDate(new Date());
							orderCps.setCreateDate(null);
							// 更新
							log.info("更新orderCps记录：userid:" + userId + " businessId:" + bussinessId + " orderNumber:"
									+ orderNumber + " cpsFee:" + cpsFee + " cpsNumber:" + currentCpsNumber);
							orderCpsDao.update(orderCps);
						} else {
							orderCps = new OrderCps();
							orderCps.setOrderType(1);
							orderCps.setUserId((long) userId);
							orderCps.setBusinessId(adBusinessId);
							orderCps.setOrderNumber(orderNumber);
							orderCps.setCpsNumber(currentCpsNumber);
							orderCps.setCpsFee(cpsFee);

							Date date = new Date();
							orderCps.setCreateDate(date);
							orderCps.setUpdateDate(date);
							// 新增
							log.info("新增orderCps记录：userid:" + userId + " businessId:" + bussinessId + " orderNumber:"
									+ orderNumber + " cpsFee:" + cpsFee + " cpsNumber:" + currentCpsNumber);
							orderCpsDao.insert(orderCps);
						}
					} else if (cpsStatus == 1) {
						// TODO 品类返点,暂时不处理
					}
				}
			} else if (type == 5) {
				// TODO 退货订单,暂时不处理
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("更新商家营销补贴异常：" + e.getMessage());
		}
	}

	/**
	 * 获取商家营销费用规则
	 * 
	 * @param merchantCpsId
	 * @param orderNumber
	 * @return
	 */
	private List<List<ReMerchantCpsDetail>> getCpsDetailList(Long merchantCpsId) {
		List<List<ReMerchantCpsDetail>> list = new ArrayList<>();

		int count = reMerchantCpsDetailDao.getOrderCount(merchantCpsId);

		if (count > 0) {
			int startOrderNumber = 1;
			int endOrderNumber = count;

			for (int orderNumber = startOrderNumber; orderNumber <= endOrderNumber; orderNumber++) {
				List<ReMerchantCpsDetail> reMerchantCpsDetailBeans = reMerchantCpsDetailDao
						.getDetailByOrderNumber(merchantCpsId, orderNumber);
				list.add(reMerchantCpsDetailBeans);
			}
		}

		return list;
	}
}
