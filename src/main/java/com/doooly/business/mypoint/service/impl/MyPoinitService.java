package com.doooly.business.mypoint.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.didi.constants.DiDiConstants;
import com.doooly.business.mypoint.service.MyPointServiceI;
import com.doooly.business.utils.DateUtils;
import com.doooly.business.utils.Pagelab;
import com.doooly.dao.reachad.AdAvailablePointsDao;
import com.doooly.dao.reachad.AdBusinessDao;
import com.doooly.dao.reachad.AdIntegralAcquireRecordDao;
import com.doooly.dao.reachad.AdRechargeDao;
import com.doooly.dao.reachad.AdReturnPointsDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.OrderDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdAvailablePoints;
import com.doooly.entity.reachad.AdBusiness;
import com.doooly.entity.reachad.AdIntegralAcquireRecord;
import com.doooly.entity.reachad.AdRecharge;
import com.doooly.entity.reachad.AdReturnPoints;
import com.doooly.entity.reachad.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: service实现
 * @author: qing.zhang
 * @date: 2017-05-18
 */
@Service
public class MyPoinitService implements MyPointServiceI {

	@Autowired
	private AdBusinessDao adBusinessDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdAvailablePointsDao adAvailablePointsDao;
	@Autowired
	private AdReturnPointsDao adReturnPointsDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private AdRechargeDao adRechargeDao;
	@Autowired
	private AdIntegralAcquireRecordDao adIntegralAcquireRecordDao;
	/**
	 * 通过家属邀请的所有id查询到返利的列表和积分的总和
	 * 
	 * @param data
	 * @return
	 */
	public JSONObject getFamilyRebateInfo(JSONObject data) {
		JSONObject res = new JSONObject();
		try {
			String familyIds = data.getString("familyIds");
			// 获取活动开始时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginDate = format.parse(format.format(Long.valueOf(data.getString("beginDate"))));
			// 获取活动结束时间
			Date endDate = format.parse(format.format(Long.valueOf(data.getString("endDate"))));
			List<String> familyIdsList = JSONObject.parseArray(familyIds, String.class);
			// 查询家属邀请用户的列表信息
			List<AdReturnPoints> adReturnPointsList = adReturnPointsDao.getByUserIds(familyIdsList, beginDate, endDate);
			// 统计总数
			BigDecimal totalAmount = new BigDecimal(0);
			if (adReturnPointsList != null) {
				for (AdReturnPoints adReturnPoints : adReturnPointsList) {
					if (adReturnPoints.getType().equals("5")) {
						totalAmount = totalAmount.subtract(adReturnPoints.getAmount());
					} else {
						totalAmount = totalAmount.add(adReturnPoints.getAmount());
					}
				}
			}
			// 统计当日的消费人数
			Integer count = adReturnPointsDao.getCountByUserIds(familyIdsList);
			res.put("code", "0");
			res.put("familyRebateList", adReturnPointsList);
			res.put("familyRebateTotal", totalAmount);
			res.put("userCount", count);
			return res;
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MessageDataBean queryUserIntegral(String businessId, String username, String password, String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 判断用户名及密码是否合法
		Boolean flag = adBusinessDao.findByUsernamePassword(businessId, username, password);
		if (!flag) {
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("积分查询失败,用户名及密码错误！");
			return messageDataBean;
		}
		// 判断商户是否存在
		AdBusiness byBusinessId = adBusinessDao.getByBusinessId(businessId);
		if (byBusinessId == null) {
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("积分查询失败,非法的商户！");
			return messageDataBean;
		}
		// 查询可用积分
		BigDecimal availablePoint = adUserDao.getAvailablePoint(userId);
		// 查询待返积分
		BigDecimal returnPoint = adUserDao.getReturnPoint(userId);
		map.put("availablePoint", availablePoint!=null ? availablePoint : new BigDecimal("0"));
		map.put("returnPoint", returnPoint!=null ? returnPoint : new BigDecimal("0"));
		messageDataBean.setCode(MessageDataBean.success_code);
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getAvailablePoints(String income, String userId, Integer currentPage, Integer pageSize)
			throws IOException {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Pagelab pagelab = new Pagelab(currentPage, pageSize);
		// 查询总数
		int totalNum = adAvailablePointsDao.getTotalNum(income, userId);
		if (totalNum > 0) {
			pagelab.setTotalNum(totalNum);// 这里会计算总页码
			// 查询详情
			List<AdAvailablePoints> adAvailablePoints = adAvailablePointsDao.getAdAvailablePoints(income, userId,
					pagelab.getStartIndex(), pagelab.getPageSize());
			for (AdAvailablePoints adAvailablePoint : adAvailablePoints) {
				// 日期处理
				adAvailablePoint.setCreateDateStr(
						DateUtils.formatDate(adAvailablePoint.getCreateDate(), "yyyy年MM月dd日 HH:mm:ss"));
			}
			map.put("adAvailablePoints", adAvailablePoints);// 数据
			map.put("countPage", pagelab.getCountPage());// 总页码
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} else {
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("查询可用积分数据为空");
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean getReturnPoints(String income, String userId, Integer currentPage, Integer pageSize) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Pagelab pagelab = new Pagelab(currentPage, pageSize);
		// 查询总数
		int totalNum = adReturnPointsDao.getTotalNum(income, userId);
		if (totalNum > 0) {
			pagelab.setTotalNum(totalNum);// 这里会计算总页码
			// 查询详情
			List<AdReturnPoints> adReturnPoints = adReturnPointsDao.getAdReturnPoints(income, userId,
					pagelab.getStartIndex(), pagelab.getPageSize());
			for (AdReturnPoints adReturnPoint : adReturnPoints) {
				adReturnPoint
						.setCreateDateStr(DateUtils.formatDate(adReturnPoint.getCreateDate(), "yyyy年MM月dd日 HH:mm:ss"));
			}
			map.put("adAvailablePoints", adReturnPoints);// 数据
			map.put("countPage", pagelab.getCountPage());// 总页码
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} else {
			messageDataBean.setCode(MessageDataBean.failure_code);
			messageDataBean.setMess("查询待返积分数据为空");
		}
		return messageDataBean;
	}

	@Override
	public MessageDataBean getAvailablePointDetail(String availablePointsId, String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 查询详情
		AdAvailablePoints adAvailablePoint = adAvailablePointsDao.getAvailablePointDetail(availablePointsId);
		messageDataBean.setCode(MessageDataBean.failure_code);
		if (adAvailablePoint == null) {
			messageDataBean.setMess("查无此可用积分记录");
		} else if (!userId.equals(adAvailablePoint.getUserId())) {
			messageDataBean.setMess("会员与此可用积分记录不匹配，不能查看");
		} else {
			map.put("adAvailablePoint", adAvailablePoint);
			map.put("integral", "");
			map.put("date", "");
			if (adAvailablePoint.getOrderId() != null) {
				// 查询订单
				Order order = orderDao.get(String.valueOf(adAvailablePoint.getOrderId()));
				if (order != null) {
                    if(DiDiConstants.DIDI_BUSINESS_ID.equals(order.getBussinessId()) && order.getType()==2){
                        map.put("type","1");//滴滴专用
                        messageDataBean.setCode(MessageDataBean.success_code);
                    }else {
                        // 查询总金额
                        getTotalAmountAndTotalPrice(map, order);
                        messageDataBean.setCode(MessageDataBean.success_code);
                    }
				} else {
					messageDataBean.setMess("查无积分订单明细");
				}
			} else if (adAvailablePoint.getRechargeId() != null) {
				// 查询充值记录
				AdRecharge adRecharge = adRechargeDao.get(adAvailablePoint.getRechargeId());
				if (adRecharge != null) {
					adRecharge.setRechargeDate(DateUtils.formatDate(adRecharge.getCreateDate(), "yyyy.MM.dd HH:mm:ss"));
					map.put("showType", "recharge");
					map.put("adRecharge", adRecharge);
					messageDataBean.setCode(MessageDataBean.success_code);
				} else {
					messageDataBean.setMess("查无积分充值明细");
				}
			}else if (adAvailablePoint.getType().equals(AdAvailablePoints.TYPE_INTEGRAL_ACTIVITY)) {
				AdIntegralAcquireRecord record = adIntegralAcquireRecordDao.checkIsHadProvided(Long.valueOf(userId));
				if (record !=null) {
					map.put("showType", "activity");
					map.put("integral", adAvailablePoint.getBusinessRebateAmount());
					map.put("date", DateUtils.formatDate(record.getCreateDate(), "yyyy.MM.dd HH:mm"));
					map.put("integralName",record.getIntegralName());
					messageDataBean.setCode(MessageDataBean.success_code);
				}else {
					messageDataBean.setMess("查无积分活动明细");
				}
			} else {
				messageDataBean.setMess("查无积分明细");
			}
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getReturnPointDetail(String returnPointsId, String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 查询详情
		AdReturnPoints adReturnPoint = adReturnPointsDao.getAvailablePointDetail(returnPointsId);
		messageDataBean.setCode(MessageDataBean.failure_code);
		if (adReturnPoint == null) {
			messageDataBean.setMess("查无此待返积分记录");
		} else if (!userId.equals(adReturnPoint.getUserId())) {
			messageDataBean.setMess("会员与此待返积分记录不匹配，不能查看");
		} else {
			map.put("adReturnPoint", adReturnPoint);
			if (adReturnPoint.getOrderId() != null) {
				// 查询订单
				Order order = orderDao.get(String.valueOf(adReturnPoint.getOrderId()));
				if (order != null) {
					getTotalAmountAndTotalPrice(map, order);
					messageDataBean.setCode(MessageDataBean.success_code);
				} else {
					messageDataBean.setMess("查无待返积分订单明细");
				}
			} else {
				messageDataBean.setMess("查无待返积分明细");
			}
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	/**
	 * 根据订单编号查询订单总金额
	 * 
	 * @param map
	 * @param order
	 */
	private void getTotalAmountAndTotalPrice(HashMap<String, Object> map, Order order) {
		// 根据商家编号获取商家名称
		AdBusiness adBusiness = adBusinessDao.getByBusinessId(order.getBussinessId());
		Order totalOrder = null;
		if("来伊份".equals(adBusiness.getCompany())){
			// 根据流水号查询订单总金额(来伊份特殊处理，因为_order表中type=2的订单号和type=1的订单号不一样)
			totalOrder = orderDao.getTotalBySerialNumber(order.getSerialNumber());
		}else{
			// 根据订单号查询订单总金额
			totalOrder = orderDao.getTotalByOrderNumber(order.getOrderNumber());
		}
		// 查询总金额
		if (totalOrder==null) {
			//睿仕之家退单,orderNumber随机为时间戳,并不能取到type为1的总计,则取type为5的总计
			totalOrder= orderDao.getTotalByOrderNumberByTypeFive(order.getOrderNumber());
		}
		order.setTotalAmount(totalOrder.getTotalAmount());
		order.setTotalPrice(totalOrder.getTotalPrice());
		order.setUserRebate(totalOrder.getUserRebate());
		order.setAmount(totalOrder.getAmount());
		order.setOrderDateStr(DateUtils.formatDate(order.getOrderDate(), "yyyy.MM.dd HH:mm:dd"));
		map.put("showType", "order");
		map.put("order", order);
	}

}
