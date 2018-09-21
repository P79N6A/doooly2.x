package com.doooly.business.selfApplication.impl;

import com.doooly.business.selfApplication.SelfApplicationServiceI;
import com.doooly.dao.reachad.AdGroupDao;
import com.doooly.dao.reachad.AdGroupDepartmentDao;
import com.doooly.dao.reachad.AdUserDao;
import com.doooly.dao.reachad.AdUserPersonalInfoDao;
import com.doooly.dao.reachlife.LifeGroupDao;
import com.doooly.dao.reachlife.LifeMemberDao;
import com.doooly.dto.common.MessageDataBean;
import com.doooly.entity.reachad.AdGroup;
import com.doooly.entity.reachad.AdGroupDepartment;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserPersonalInfo;
import com.doooly.entity.reachlife.LifeGroup;
import com.doooly.entity.reachlife.LifeMember;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 员工自主申请
 * @author: wenwei.yang
 * @date: 2017-10-09
 */
@Service
@Transactional
public class SelfApplicationService implements SelfApplicationServiceI{
	private static Logger logger = Logger.getLogger(SelfApplicationService.class);
	@Autowired
	private AdGroupDao adGroupDao;
	@Autowired
	private AdGroupDepartmentDao adGroupDepartmentDao;
	@Autowired
	private AdUserDao adUserDao;
	@Autowired
	private AdUserPersonalInfoDao adUserPersonalInfoDao;
	@Autowired
	private LifeMemberDao xxMemberDao;
	@Autowired
	private LifeGroupDao lifeGroupDao;
	@Override
	public MessageDataBean getAllCompany() {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdGroup> list = adGroupDao.getAllCompany();
		if (!list.isEmpty()) {
			map.put("companyList", list);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			map.put("companyList", null);
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean getDepartmentDatasByGroupId(String companyId) {
		MessageDataBean messageDataBean = new MessageDataBean();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<AdGroupDepartment> list = adGroupDepartmentDao.getDepartmentDatasByGroupId(companyId);
		if (!list.isEmpty()) {
			map.put("departmentList", list);
			messageDataBean.setCode(MessageDataBean.success_code);
		} else {
			map.put("departmentList", null);
			messageDataBean.setCode(MessageDataBean.no_data_code);
		}
		messageDataBean.setData(map);
		return messageDataBean;
	}

	@Override
	public MessageDataBean submitData(String companyId, String departmentId, String name, String telephone,
			String workNumber, String sex, Date hiredate) {
		MessageDataBean messageDataBean = new MessageDataBean();
		AdUser user = adUserDao.findByMobileBySelfApplication(telephone);
		if (user == null) {
			AdUser record = new AdUser();
			AdGroup findGroupByID = adGroupDao.findGroupByID(companyId);
			record.setGroupNum(Long.valueOf(companyId));
			//record.setDepartmentId(departmentId);
			record.setName(name);
			record.setTelephone(telephone);
			record.setDelFlag("1");
			//record.setDataSources(1);
			record.setSex(sex);
			record.setPassword("96e79218965eb72c92a549dd5a330112");
			record.setPayPassword(null);// 默认支付密码
			record.setIntegral(new BigDecimal("0"));
			record.setLineCredit(new BigDecimal("0"));
			record.setIsActive("1");// 默认未激活
			record.setType(Short.valueOf("0"));// 默认账户为员工
			record.setDataSyn("0");// 默认第三方同步
			record.setCreateDate(new Date());// 创建时间
			record.setUpdateDate(new Date());
			//record.setIsAudit(0);
			record.setCreateBy(0+"");
			record.setUpdateBy(0+"");
			String count = adUserDao.find12NumberCountByGroupId(companyId,findGroupByID.getGroupNum());
			if (!count.equals("0000000")&&count.length()>7) {
				count = count.substring(count.length()-7);
			}
			count = (Integer.valueOf(count)+1)+"";
			if (count.length()<7) {
				int length = count.length();
				String wPrefix = "";
				for (int j = 0; j < 7 - length; j++) {
					wPrefix += "0";
				}
				count = wPrefix + count;
				
			}
			record.setIsPayPassword("1");
			record.setCardNumber(findGroupByID.getGroupNum()+count);
			adUserDao.insert(record);
			AdUser adUser = adUserDao.findByMobile(telephone);
			AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
			adUserPersonalInfo.setId(adUser.getId());
			adUserPersonalInfo.setEntryDate(hiredate);
			adUserPersonalInfo.setDataSources(1);
			adUserPersonalInfo.setIsAudit(0);
			adUserPersonalInfo.setDepartmentID(Long.valueOf(departmentId));
			adUserPersonalInfo.setWorkNumber(workNumber);
            adUserPersonalInfo.setIsSetPassword(0);
			adUserPersonalInfoDao.insert(adUserPersonalInfo);
			//加A库数据
			LifeGroup lifeGroup = new LifeGroup();
			lifeGroup.setAdId(companyId);
			lifeGroup.setDelFlg("0");
			LifeGroup groupByGroupId = lifeGroupDao.getGroupByGroupId(lifeGroup);// 获取groupId
			LifeMember lifemember = new LifeMember();
			lifemember.setIsNewRecord(true);
			lifemember.setUsername(adUser.getCardNumber());
			lifemember.setCreateDate(new Date());
			lifemember.setMobile(telephone);
			lifemember.setName(name);
			lifemember.setGender(Integer.valueOf(sex));
			
			lifemember.setIsEnabled(1);
			lifemember.setIsLocked(false);
			lifemember.setLoginFailureCount(0);
			lifemember.setGroupId(Long.valueOf(groupByGroupId.getId())); 
			lifemember.setMemberType(0);
			lifemember.setModifyDate(lifemember.getCreateDate());
			lifemember.setDeleteFlg("1");
			lifemember.setAdId(adUser.getId()+"");
			xxMemberDao.insert(lifemember);
			messageDataBean.setCode(MessageDataBean.success_code);
		}else {
			//有数据的情况下判断渠道
			if (user.getDataSources()==0) {
				//平台导入的数据
				messageDataBean.setCode(MessageDataBean.time_out_code);
			}else {
				//是否审核、激活、删除
				if (user.getIsAudit()!=null&&user.getIsAudit()==0&&user.getDelFlag().equals("1")) {
					//待审核
					logger.info("员工自主申请，此时为再次提交待审核数据，不予更改");
					messageDataBean.setCode(MessageDataBean.already_used_code);
				}else if (user.getIsAudit()!=null&&user.getIsAudit()==2&&user.getDelFlag().equals("1")) {
					//审核不通过
					logger.info("员工自主申请，此时原始数据为审核不通过，予以修改信息");
					user.setGroupNum(Long.valueOf(companyId));
					//user.setDepartmentId(departmentId);
					user.setName(name);
					user.setTelephone(telephone);
					user.setSex(sex);
					adUserDao.updateSelfInfomation(user);
					AdUserPersonalInfo adUserPersonalInfo = new AdUserPersonalInfo();
					adUserPersonalInfo.setId(user.getId());
					adUserPersonalInfo.setEntryDate(hiredate);
					adUserPersonalInfo.setDepartmentID(Long.valueOf(departmentId));
					adUserPersonalInfo.setWorkNumber(workNumber);
					adUserPersonalInfo.setDataSources(1);
					adUserPersonalInfo.setIsAudit(0);
					adUserPersonalInfoDao.update(adUserPersonalInfo);
					messageDataBean.setCode(MessageDataBean.success_code);
				}
				else if (user.getIsAudit()!=null&&user.getIsAudit()==1&&user.getDelFlag().equals("0")) {
					//审核后并且已激活并且未删除
					messageDataBean.setCode(MessageDataBean.have_not_code);
				}else {
					//数据出错
					logger.error("员工自主申请，提交时数据库原有的数据出错，出错数据user的id为:"+user.getId());
					messageDataBean.setCode(MessageDataBean.failure_code);
				}
			}
		}
		return messageDataBean;
	}
	

}
