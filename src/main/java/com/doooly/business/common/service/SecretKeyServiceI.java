package com.doooly.business.common.service;

import java.util.List;

import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyStatus;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥数据库服务类
 * @author Albert(赵清江)
 * @date 2016-7-8
 * @version 1.0
 */
public interface SecretKeyServiceI {
	/**
	 * 新增密钥对(一对或多对)
	 * @param List<SecretKey>
	 * @return 是否插入成功
	 */
	public boolean insertSecretKeys(List<SecretKey> keys);
	/**
	 * 删除密钥对
	 * @param SecretKeyOwner owner
	 * @param SecretKeyStatus isValid
	 * @return 是否删除成功
	 */
	public boolean deleteSecretKeys(SecretKeyOwner owner,SecretKeyStatus isValid);
	/**
	 * 更新密钥对(一对或多对)
	 * @param List<SecretKey> keys
	 * @return 是否更新成功
	 */
	public boolean updateSecretKeys(List<SecretKey> keys);
	/**
	 * 根据密钥是否在有效期查询所有密钥
	 * @param SecretKeyStatus isValid
	 * @return List<SecretKey>
	 */
	public List<SecretKey> get(SecretKeyStatus isValid);
	
}
