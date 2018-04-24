package com.doooly.dao.doooly;

import java.util.List;

import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyStatus;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥Dao
 * @author Albert
 * @date 2016-07-10
 * @version 1.0
 */
public interface SecretKeyDao {
	
	public int insert(SecretKey keys);
	
	public int delete(SecretKeyOwner owner,SecretKeyStatus isValid);
	
	public int update(SecretKey keys);
	
	public List<SecretKey> get(SecretKeyStatus isValid);
	
}
