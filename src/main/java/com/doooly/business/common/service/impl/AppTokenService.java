package com.doooly.business.common.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.AppTokenServiceI;
import com.doooly.common.constants.Constants.AppTokenStatus;
import com.doooly.common.util.AppTokenUtils;
import com.doooly.dao.reachad.AppTokenDao;
import com.doooly.entity.reachad.AppToken;

/**
 * token服务类
 * @author 赵清江
 * @date 2016-06-28
 * @version 1.0
 */
@Service
public class AppTokenService implements AppTokenServiceI {

	@Autowired
	private AppTokenDao tokenDao;
	
	@Override
	public String getNewToken() {
		return AppTokenUtils.getToken();
	}
	
	@Override
	public boolean isBind(String account) {
		AppToken token = new AppToken();
		token.setCard(account);
		token.setMobile(account);
		token.setIsValid(AppTokenStatus.Valid);
		return tokenDao.findRecord(token) == null ? false : true;
	}
	
	/**
	 * 先找出是否有失效的绑定记录,若有则更新记录并生效,若无则新建有效绑定记录
	 */
	@Override
	public String newBind(AppToken token) {
		int isSuccess = 0;
		String tokenStr = null;
		
		try {
			//找出该账号下是否有失效的绑定记录
			token.setIsValid(AppTokenStatus.Invalid);
			boolean isNull = tokenDao.findRecord(token) == null ? true : false;
			//生成token
			tokenStr = getNewToken();
			token.setToken(tokenStr);
			token.setIsValid(AppTokenStatus.Valid);
			if (isNull) {
				token.setCreateDate(new Date());
				isSuccess = tokenDao.insert(token);
			} else {
				token.setModifyDate(new Date());
				isSuccess = tokenDao.update(token);
			}
		} catch (Exception e) {
			System.out.println("[SQL ERROR] : newBind token failed!");
			e.printStackTrace();
		}
		return isSuccess != 0 ? tokenStr : null;
	}

	@Override
	public String renewBind(AppToken token) {
		int isSuccess = 0;
		String tokenStr = null;
		
		try {
			//生成新token
			tokenStr = getNewToken();
			token.setToken(tokenStr);
			token.setIsValid(AppTokenStatus.Valid);
			token.setModifyDate(new Date());
			isSuccess = tokenDao.update(token);
		} catch (Exception e) {
			System.out.println("[SQL ERROR] : renewBing token failed!");
			e.printStackTrace();
		}
		return isSuccess != 0 ? tokenStr : null;
	}
	
	/**
	 * token解绑
	 */
	@Override
	public boolean unBind(AppToken token) {
		boolean isSuccess = false;
		
		try {
			token.setModifyDate(new Date());
			token.setIsValid(AppTokenStatus.Invalid);
			isSuccess = tokenDao.update(token) != 0 ? true : false;
		} catch (Exception e) {
			System.out.println("[SQL ERROR] : unBind token failed!");
			e.printStackTrace();
		}
		return isSuccess;
	}

	

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
