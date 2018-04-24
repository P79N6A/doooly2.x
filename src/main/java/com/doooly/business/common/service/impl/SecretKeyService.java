package com.doooly.business.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.SecretKeyServiceI;
import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyStatus;
import com.doooly.dao.doooly.SecretKeyDao;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥数据库服务类
 * @author Albert(赵清江)
 * @date 2016-7-8
 * @version 1.0
 */
@Service
public class SecretKeyService implements SecretKeyServiceI{

	@Autowired
	private SecretKeyDao secretKeyDao;

	@Override
	public boolean insertSecretKeys(List<SecretKey> keys) {
		if (keys == null) {
			return false;
		}
		int counter = 0;
		for (SecretKey secretKey : keys) {
			counter += secretKeyDao.insert(secretKey);
		}
		if (counter == keys.size()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteSecretKeys(SecretKeyOwner owner, SecretKeyStatus isValid) {
		int counter = secretKeyDao.delete(owner, isValid);
		if (counter > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateSecretKeys(List<SecretKey> keys) {
		int counter = 0;
		for (SecretKey secretKey : keys) {
			counter += secretKeyDao.update(secretKey);
		}
		if (counter == keys.size()) {
			return true;
		}
		return false;
	}

	@Override
	public List<SecretKey> get(SecretKeyStatus isValid) {
		return secretKeyDao.get(isValid);
	}
}
