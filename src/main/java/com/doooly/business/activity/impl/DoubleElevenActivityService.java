package com.doooly.business.activity.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.activity.DoubleElevenActivityServiceI;
import com.doooly.dao.reachad.AdDoubleElevenRecordDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdDoubleElevenRecord;

/**
 *宝钢化工厨神大赛报名活动Controller
 * 
 * @author wenwei.yang
 * @version 2017年9月4日
 */
@Service
public class DoubleElevenActivityService implements DoubleElevenActivityServiceI {

	private static Logger logger = Logger.getLogger(DoubleElevenActivityService.class);
	@Autowired
	private AdDoubleElevenRecordDao adDoubleElevenRecordDao;
	
	ReentrantLock lock = new ReentrantLock();
	@Override
	public MessageDataBean getActivityIndex(String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		//1.个人获取的总积分
		try {
			AdDoubleElevenRecord userRecord =adDoubleElevenRecordDao.findDataByUserIdForTypeZero(Integer.valueOf(userId));
			if (userRecord == null) {
				AdDoubleElevenRecord record = new AdDoubleElevenRecord();
				record.setUserId(userId);
				record.setType("0");
				record.setPointCount(new BigDecimal(0));
				adDoubleElevenRecordDao.insert(record);
				map.put("totalCount", 0);
				map.put("memberCount", 0);
				map.put("records", "");
			}else {
				AdDoubleElevenRecord totalData = adDoubleElevenRecordDao.findTotalDataBySuperId(Integer.valueOf(userId));
				map.put("totalCount", totalData.getTotalCount());
				//2.参与者人数
				map.put("memberCount", totalData.getMemberCount());
				//3.参与者5条数据
				List<AdDoubleElevenRecord> list = adDoubleElevenRecordDao.findDataBySuperId(userId);
				if (list.isEmpty()) {
					map.put("records", "");
				}else {
					map.put("records", list);
				}
			}
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}
	@Override
	public MessageDataBean getActivityIndexForJoiner(String superUserId, String userId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		//1.个人获取的总积分
		try {
			AdDoubleElevenRecord totalData = adDoubleElevenRecordDao.findTotalDataBySuperId(Integer.valueOf(superUserId));
			map.put("totalCount", totalData.getTotalCount());
			//2.参与者人数
			map.put("memberCount", totalData.getMemberCount());
			//3.参与者5条数据
			List<AdDoubleElevenRecord> list = adDoubleElevenRecordDao.findDataBySuperId(superUserId);
			if (list.isEmpty()) {
				map.put("records", "");
			}else {
				map.put("records", list);
			}
			int jurisdiction = 2000;//未登陆
			if (!StringUtils.isBlank(userId)) {
				List<AdDoubleElevenRecord> joinRecords = adDoubleElevenRecordDao.findDataByUserId(userId);
				if (joinRecords.isEmpty()) {
					jurisdiction=1000;//有投票权限
				}else {
					if (joinRecords.size() ==3) {
						jurisdiction=1004;//3票已经投完
						for (AdDoubleElevenRecord adDoubleElevenRecord : joinRecords) {
							if (adDoubleElevenRecord.getSuperUserId().equals(superUserId)) {
								jurisdiction=1003;//已经给该发起者投过票了
							}
						}
					}else {
						jurisdiction=1000;//有投票权限
						for (AdDoubleElevenRecord adDoubleElevenRecord : joinRecords) {
							if (adDoubleElevenRecord.getSuperUserId().equals(superUserId)) {
								jurisdiction=1003;//已经给该发起者投过票了
							}
						}
					}
				}
			}
			map.put("jurisdiction", jurisdiction);
			messageDataBean.setCode(MessageDataBean.success_code);
			messageDataBean.setData(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}
	@Override
	public MessageDataBean helpInitiator(String superUserId, String userId) {
		// TODO Auto-generated method stub
		MessageDataBean messageDataBean = new MessageDataBean();
		//判断是否有权限投票
		logger.info("帮助加分superId为:"+superUserId+",参与者id为+"+userId);
		int jurisdiction = 2000;//未登陆
		try {
			List<AdDoubleElevenRecord> joinRecords = adDoubleElevenRecordDao.findDataByUserId(userId);
			if (joinRecords.isEmpty()) {
				jurisdiction=1000;//有投票权限
			}else {
				if (joinRecords.size() ==3) {
					jurisdiction=1004;//3票已经投完
					messageDataBean.setCode(MessageDataBean.have_not_code);
					for (AdDoubleElevenRecord adDoubleElevenRecord : joinRecords) {
						if (adDoubleElevenRecord.getSuperUserId().equals(superUserId)) {
							jurisdiction=1003;//已经给该发起者投过票了
							messageDataBean.setCode(MessageDataBean.already_used_code);
						}
					}
				}else {
					jurisdiction=1000;//有投票权限
					for (AdDoubleElevenRecord adDoubleElevenRecord : joinRecords) {
						if (adDoubleElevenRecord.getSuperUserId().equals(superUserId)) {
							jurisdiction=1003;//已经给该发起者投过票了
							messageDataBean.setCode(MessageDataBean.already_used_code);
						}
					}
				}
			}
			if (jurisdiction==1000) {
				//参与者有投票权限并且未给该发起者投过票,记录数据,发放规则进行分配积分,锁死进行积分判断,数据插入等操作
				AdDoubleElevenRecord record = new AdDoubleElevenRecord();
				record.setUserId(userId);
				record.setSuperUserId(superUserId);
				record.setType("1");
				lock.lock();
				AdDoubleElevenRecord totalData = adDoubleElevenRecordDao.findTotalDataBySuperId(Integer.valueOf(superUserId));
				Integer count = totalData.getMemberCount()+1;
				if ((count%14)==0 ) {
					//11.11
					record.setPointCount(new BigDecimal(11.11));
				}else if ((count%14)==1) {
					//1.11
					record.setPointCount(new BigDecimal(1.11));
				}else {
					//0.11
					record.setPointCount(new BigDecimal(0.11));
				}
				adDoubleElevenRecordDao.insert(record);
				lock.unlock();
				messageDataBean.setCode(MessageDataBean.success_code);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}
	
	@Override
	public MessageDataBean receiveGift(String userId) {
		logger.info("送清凉活动 领取饮料用户id=" + userId);
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 判断该用户是否领取过送清凉活动礼品
			AdDoubleElevenRecord historyRecord = adDoubleElevenRecordDao.findDataByUserIdAndType(userId, 2);
			if(historyRecord != null){
				// 已经领取过
				messageDataBean.setCode(MessageDataBean.already_receive_code);
			}else{
				// 未领取则新增领取记录
				AdDoubleElevenRecord record = new AdDoubleElevenRecord();
				record.setUserId(userId);
				record.setType("2");
				record.setPointCount(new BigDecimal("0"));
				adDoubleElevenRecordDao.insert(record);
				messageDataBean.setCode(MessageDataBean.success_code);
			}
		} catch (Exception e) {
			logger.error("送清凉活动立即领取异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}
	@Override
	public MessageDataBean isReceiveGift(String userId) {
		logger.info("进入送清凉活动页面 用户id=" + userId);
		MessageDataBean messageDataBean = new MessageDataBean();
		try {
			// 判断该用户是否领取过送清凉活动礼品
			AdDoubleElevenRecord historyRecord = adDoubleElevenRecordDao.findDataByUserIdAndType(userId, 2);
			messageDataBean.setCode(historyRecord != null ? MessageDataBean.already_receive_code : MessageDataBean.success_code);
		} catch (Exception e) {
			logger.error("进入送清凉活动页面异常！", e);
			messageDataBean.setCode(MessageDataBean.failure_code);
		}
		return messageDataBean;
	}

}
