package com.doooly.business.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.doooly.business.common.service.AdGroupServiceI;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.SysDictDao;
import com.doooly.dto.common.ConstantsLogin;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.SysDict;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 企业信息表服务类
 * 
 * @author 赵清江
 * @date 2016年7月14日
 * @version 1.0
 */
@Service
public class AdGroupService implements AdGroupServiceI {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private AdGroupDao adGroupDao;

	/** 字典DAO */
	@Autowired
	private SysDictDao sysDictDao;

	@Override
	public boolean isCardNumberValid(String cardNumber) {
		String udid = cardNumber.substring(0, 3);
		AdGroup adGroup = adGroupDao.findGroupByUDID(udid);
		if (adGroup == null) {
			return false;
		}
		String officialNumber = cardNumber.substring(3, 5);
		if (!officialNumber.equals(adGroup.getOfficialNumber())) {
			boolean isRelation = (Integer.valueOf(officialNumber) <= Integer.valueOf(adGroup.getRelationEndNo())
					&& Integer.valueOf(officialNumber) >= Integer.valueOf(adGroup.getRelationStartNo())) ? true : false;
			if (!isRelation) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getGroupName(long id) {
		return adGroupDao.findNameByID(id);
	}

	/**
	 * 获取企业口令问题 groupQuestion-企业口令问题 type - group_qustion
	 */
	@Override
	public MessageDataBean getGroupCommandInfo(JSONObject paramData) throws Exception {
		// 返回数据
		MessageDataBean dataBean = new MessageDataBean();
		try {
			List<SysDict> SysDictList = sysDictDao.getSysDict("group_qustion");
			if (SysDictList != null && SysDictList.size() > 0) {
				dataBean = new MessageDataBean(ConstantsLogin.CommandActive.SUCCESS.getCode(),
						ConstantsLogin.CommandActive.SUCCESS.getMsg());
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("groupQuestion", SysDictList.get(0).getLabel());
				dataBean.setData(dataMap);
			} else {
				logger.info("====未查询到问题-SysDictList：" + SysDictList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【AdGroupService】返回数据-dataBean：" + dataBean.toJsonString());
		return dataBean;
	}

	/**
	 * 通过企业口令获取企业集合
	 * 
	 * @param groupCommand-企业口令
	 */
	@Override
	public MessageDataBean getGroupByCommand(JSONObject paramData) throws Exception {
		// 返回数据
		MessageDataBean dataBean = new MessageDataBean();
		try {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("groupCommand", paramData.get("groupCommand").toString());
			paramMap.put("groupId", null);
			List<AdGroup> groupList = adGroupDao.getGroupListByCommand(paramMap);
			if (groupList != null && groupList.size() > 0) {
				dataBean = new MessageDataBean(ConstantsLogin.CommandActive.SUCCESS.getCode(),
						ConstantsLogin.CommandActive.SUCCESS.getMsg());
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("groupList", groupList);
				dataBean.setData(dataMap);
			} else {
				dataBean = new MessageDataBean(ConstantsLogin.CommandActive.GROUP_NO_DATA.getCode(),
						ConstantsLogin.CommandActive.GROUP_NO_DATA.getMsg());
				logger.info("====【getGroupByCommand】该口令未查询到对应企业-groupList：" + groupList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		logger.info("====【getGroupByCommand】返回数据-dataBean：" + dataBean.toJsonString());
		return dataBean;
	}

	@Override
	public AdGroup getGroupById(String groupId) {
		return adGroupDao.findGroupByID(groupId);
	}


}
