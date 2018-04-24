package com.doooly.business.common.service.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.LifeMemberServiceI;
import com.doooly.dao.reachlife.LifeMemberDao;
import com.doooly.entity.reachlife.LifeMember;

/**
 * A系统会员信息(表xx_member)服务类
 * 
 * @author 赵清江
 * @date 2016年7月18日
 * @version 1.0
 */
@Service
public class LifeMemberService implements LifeMemberServiceI {

	@Autowired
	private LifeMemberDao lifeMemberDao;

	@Override
	public boolean memberActivation(String cardNumber) {
		LifeMember lifeMember = new LifeMember();
		lifeMember.setUsername(cardNumber);
		lifeMember.setIsEnabled(2);// 1:未激活;2:激活
		lifeMember.setModifyDate(new Date());// 修改时间
		lifeMember.setLoginFailureCount(0);// 登录失败统计初始化
		try {
			int isSuccess = lifeMemberDao.updateMemberActiveStatus(lifeMember);
			if (isSuccess != 1) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean memberActivationLogin(HashMap<String, Object> param) {
		LifeMember lifeMember = new LifeMember();
		lifeMember.setMobile(param.get("mobile").toString());
		lifeMember.setUsername(param.get("cardNumber").toString());
		lifeMember.setIsEnabled(2);// 1:未激活;2:激活
		lifeMember.setModifyDate(new Date());// 修改时间
		lifeMember.setLoginFailureCount(0);// 登录失败统计初始化
		lifeMember.setPassword(param.get("password").toString());
		try {
			int isSuccess = lifeMemberDao.updateMemberActiveStatus(lifeMember);
			if (isSuccess != 1) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void insert(LifeMember lifemember) {
		lifeMemberDao.insert(lifemember);
	}

}
