package com.doooly.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.doooly.business.common.service.SecretKeyCacheServiceI;
import com.doooly.business.common.service.SecretKeyServiceI;
import com.doooly.business.common.utils.SecretKeyUtil;
import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyStatus;
import com.doooly.common.util.RSAUtils;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥定时更新计时器类
 * @author Albert(赵清江)
 * @date 2016-07-10
 * @version 1.0
 */
@Component
@Lazy(false)
public class RenewSecretKeyJob {

	@Autowired
	private SecretKeyServiceI secretKeyService;
	@Autowired
	private SecretKeyCacheServiceI secretKeyCacheService;
	
	/**
	 * 定时任务:每天中午12点自动更新安全密钥
	 */
	@Scheduled(cron="")
	public void renewSecretKey(){
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
