package com.doooly.common.servlet;


import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.doooly.business.common.service.SecretKeyCacheServiceI;
import com.doooly.business.common.service.SecretKeyServiceI;
import com.doooly.business.common.utils.SecretKeyUtil;
import com.doooly.common.constants.Constants.SecretKeyStatus;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥初始化(服务器启动自动生成并更新密钥)
 * @author 赵清江
 * @date 2016年7月12日
 * @version 1.0
 */
public class SecretKeyInitServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4641175876068269693L;
	@Autowired
	private SecretKeyServiceI secretKeyService;
	@Autowired
	private SecretKeyCacheServiceI secretKeyCacheService;

	@Override
	public void init() throws ServletException {
		int counter = 0;
		List<SecretKey> newKeys = null;
		List<SecretKey> oldKeys = null;
		oldKeys = secretKeyCacheService.getSecretKeyAll();
		if (oldKeys != null) {
			while(true){
				newKeys = SecretKeyUtil.generateSecretKeyList();
				counter = secretKeyCacheService.updateSecretKeyPair(newKeys);
				if (counter == 2) {
					break;
				}
			}
		} else {
			while(true){
				newKeys = SecretKeyUtil.generateSecretKeyList();
				counter = secretKeyCacheService.addSecretKeyPair(newKeys);
				if (counter == 2) {
					break;
				}
			}
		}
		oldKeys = secretKeyService.get(SecretKeyStatus.VALID);
		if (oldKeys == null) {
			secretKeyService.insertSecretKeys(newKeys);
		} else {
			for (SecretKey secretKey : oldKeys) {
				secretKey.setModifyDate(new Date());
				secretKey.setIsValid(SecretKeyStatus.INVALID);
			}
			secretKeyService.updateSecretKeys(oldKeys);
			secretKeyService.insertSecretKeys(newKeys);
		}
	}

}
