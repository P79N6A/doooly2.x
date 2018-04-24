package com.doooly.business.common.service;

import java.util.List;
import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyPair;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥缓存服务类
 * @author 清江
 * @date 2016-7-10
 * @version 1.0
 */
public interface SecretKeyCacheServiceI {

	/**
	 * 向缓存中增加单个密钥(公钥或私钥)
	 * @param SecretKeyPair whichKey
	 * @param String key
	 * @return 是否新增成功
	 */
	public boolean addSecretKey(SecretKeyPair whichKey,String key);
	/**
	 * 向缓存中增加密钥对(一对或多对)
	 * @param Map<SecretKeyPair, Object> keys
	 * @return 是否新增成功
	 */
	public int addSecretKeyPair(List<SecretKey> keys);
	/**
	 * 清除缓存中的所有密钥
	 * @return 是否清除成功
	 */
	public boolean deleteSecretKey();
	/**
	 * 修改缓存中的单个密钥(公钥或私钥)
	 * @param SecretKeyPair whichKey
	 * @param String key
	 * @return 是否修改成功
	 */
	public boolean updateSecretKey(SecretKeyPair whichKey,String key);
	/**
	 * 修改缓存中的密钥对(一对或多对)
	 * @param Map<SecretKeyPair, Object> keys
	 * @return 是否修改成功
	 */
	public int updateSecretKeyPair(List<SecretKey> keys);
	/**
	 * 获取缓存中的单个密钥
	 * @param SecretKeyPair whichKey
	 * @return 密钥字符串
	 */
	public String getSecretKey(SecretKeyPair whichKey);
	/**
	 * 获取缓存中的密钥对(一对)
	 * @param SecretKeyPair whichKeyPair
	 * @return 密钥对 SecretKey
	 */
	public SecretKey getSecretKeyPair(SecretKeyOwner owner);
	/**
	 * 获取缓存中所有的密钥对
	 * @return 所有密钥对
	 */
	public List<SecretKey> getSecretKeyAll();
}
