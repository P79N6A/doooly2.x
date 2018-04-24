package com.doooly.job;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.doooly.business.common.service.SecretKeyCacheServiceI;
import com.doooly.business.common.service.SecretKeyServiceI;
import com.doooly.business.common.utils.SecretKeyUtil;
import com.doooly.common.constants.Constants.SecretKeyStatus;
import com.doooly.entity.doooly.SecretKey;

/**
 * 服务器启动自动生成密钥
 * @author 赵清江
 * @date 2016年7月12日
 * @version 1.0
 */
public class AutoGenerateSecretKey implements InitializingBean{
	
	@Autowired
	private SecretKeyServiceI secretKeyService;
	@Autowired
	private SecretKeyCacheServiceI secretKeyCacheService;

	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void generateSecretKey(){
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
