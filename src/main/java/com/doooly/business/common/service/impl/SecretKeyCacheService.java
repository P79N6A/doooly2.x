package com.doooly.business.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.SecretKeyCacheServiceI;
import com.doooly.common.constants.Constants.SecretKeyOwner;
import com.doooly.common.constants.Constants.SecretKeyPair;
import com.doooly.common.redis.AbstractBaseCache;
import com.doooly.entity.doooly.SecretKey;

/**
 * 密钥缓存服务类
 * @author 赵清江
 * @date 2016-07-10
 * @version 1.0
 */
@Service
public class SecretKeyCacheService extends AbstractBaseCache<String, String> implements SecretKeyCacheServiceI {

	@Override
	public boolean addSecretKey(final SecretKeyPair whichKey, final String key) {
		if (whichKey == null || key == null) {
			return false;
		}
		if (getSecretKey(whichKey) != null) {
			return false;
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] desc = serializer.serialize(whichKey.getDesc());
				byte[] value = serializer.serialize(key);
				return connection.setNX(desc, value);
			}
		});
		
		return result;
	}

	@Override
	public int addSecretKeyPair(final List<SecretKey> keys) {
		if (keys == null) {
			return -1;
		}
		for (SecretKey secretKey : keys) {
			if (getSecretKeyPair(secretKey.getOwner()) != null) {
				return -2;
			}
		}
		Integer result = redisTemplate.execute(new RedisCallback<Integer>() {

			@Override
			public Integer doInRedis(RedisConnection arg0) throws DataAccessException {
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] desc = null;
				byte[] value = null;
				int counter = 0;
				for (SecretKey secretKey : keys) {
					if (secretKey.getOwner() == SecretKeyOwner.Server) {
						desc = serializer.serialize(SecretKeyPair.ServerPublicKey.getDesc());
						value = serializer.serialize(secretKey.getPublicKey());
						arg0.setNX(desc, value);
						desc = serializer.serialize(SecretKeyPair.ServerPrivateKey.getDesc());
						value = serializer.serialize(secretKey.getPrivateKey());
						arg0.setNX(desc, value);
						++counter;
					} else if (secretKey.getOwner() == SecretKeyOwner.Client) {
						desc = serializer.serialize(SecretKeyPair.ClientPublicKey.getDesc());
						value = serializer.serialize(secretKey.getPublicKey());
						arg0.setNX(desc, value);
						desc = serializer.serialize(SecretKeyPair.ClientPrivateKey.getDesc());
						value = serializer.serialize(secretKey.getPrivateKey());
						arg0.setNX(desc, value);
						++counter;
					} else;
				}
				return counter;
			}
		});
		return result;
	}

	@Override
	public boolean deleteSecretKey() {
		List<String> descList = new ArrayList<>();
		descList.add((SecretKeyPair.ServerPublicKey).getDesc());
		descList.add((SecretKeyPair.ServerPrivateKey).getDesc());
		descList.add((SecretKeyPair.ClientPublicKey).getDesc());
		descList.add((SecretKeyPair.ClientPrivateKey).getDesc());
		try {
			redisTemplate.delete(descList);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateSecretKey(final SecretKeyPair whichKey, final String key) {
		if (whichKey == null || key == null) {
			return false;
		}
		if (getSecretKey(whichKey) == null) {
			throw new NullPointerException("There are no "+whichKey.getDesc()+" exsiting in cache!");
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection arg0) throws DataAccessException {
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] desc = serializer.serialize(whichKey.getDesc());
				byte[] value = serializer.serialize(key);
				arg0.set(desc, value);
				return true;
			}
		});
		return result;
	}

	@Override
	public int updateSecretKeyPair(final List<SecretKey> keys) {
		if (keys == null) {
			return -1;
		}
		int result = redisTemplate.execute(new RedisCallback<Integer>() {

			@Override
			public Integer doInRedis(RedisConnection arg0) throws DataAccessException {
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] desc = null;
				byte[] value = null;
				int counter = 0;
				for (SecretKey secretKey : keys) {
					if (secretKey.getOwner() == SecretKeyOwner.Server) {
						if (getSecretKey((SecretKeyPair.ServerPublicKey)) == null || getSecretKey((SecretKeyPair.ServerPrivateKey)) == null) {
							continue;
						} else {
							desc = serializer.serialize(SecretKeyPair.ServerPublicKey.getDesc());
							value = serializer.serialize(secretKey.getPublicKey());
							arg0.set(desc, value);
							desc = serializer.serialize(SecretKeyPair.ServerPrivateKey.getDesc());
							value = serializer.serialize(secretKey.getPrivateKey());
							arg0.set(desc, value);
							++counter;
						}
					} else if (secretKey.getOwner() == SecretKeyOwner.Client) {
						if (getSecretKey((SecretKeyPair.ClientPublicKey)) == null || getSecretKey((SecretKeyPair.ClientPrivateKey)) == null) {
							continue;
						} else {
							desc = serializer.serialize(SecretKeyPair.ClientPublicKey.getDesc());
							value = serializer.serialize(secretKey.getPublicKey());
							arg0.set(desc, value);
							desc = serializer.serialize(SecretKeyPair.ClientPrivateKey.getDesc());
							value = serializer.serialize(secretKey.getPrivateKey());
							arg0.set(desc, value);
							++counter;
						}
					} else;
				}
				return counter;
			}
		});
		return result;
	}

	@Override
	public String getSecretKey(final SecretKeyPair whichKey) {
		String result = redisTemplate.execute(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection arg0) throws DataAccessException {
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] desc = serializer.serialize(whichKey.getDesc());
				byte[] value = arg0.get(desc);
				if (value == null) {
					return null;
				}
				return serializer.deserialize(value);
			}
		});
		return result;
	}

	@Override
	public SecretKey getSecretKeyPair(final SecretKeyOwner owner) {
		SecretKey result = redisTemplate.execute(new RedisCallback<SecretKey>() {

			@Override
			public SecretKey doInRedis(RedisConnection arg0) throws DataAccessException {
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] descPublicKey = null;
				byte[] descPrivateKey = null;
				if (owner == SecretKeyOwner.Server) {
					descPublicKey = serializer.serialize(SecretKeyPair.ServerPublicKey.getDesc());
					descPrivateKey = serializer.serialize(SecretKeyPair.ServerPrivateKey.getDesc());
				} else if (owner == SecretKeyOwner.Client) {
					descPublicKey = serializer.serialize(SecretKeyPair.ClientPublicKey.getDesc());
					descPrivateKey = serializer.serialize(SecretKeyPair.ClientPrivateKey.getDesc());
				} else;
				byte[] valuePublicKey = arg0.get(descPublicKey);
				byte[] valuePrivateKey = arg0.get(descPrivateKey);
				if (valuePublicKey == null || valuePrivateKey == null) {
					return null;
				}
				SecretKey secretKey = new SecretKey();
				secretKey.setPublicKey(serializer.deserialize(valuePublicKey));
				secretKey.setPrivateKey(serializer.deserialize(valuePrivateKey));
				return secretKey;
			}
		});
		
		return result;
	}

	@Override
	public List<SecretKey> getSecretKeyAll() {
		List<SecretKey> result = redisTemplate.execute(new RedisCallback<List<SecretKey>>() {

			@Override
			public List<SecretKey> doInRedis(RedisConnection arg0) throws DataAccessException {
				RedisSerializer<String> serializer = getStringSerializer();
				List<SecretKey> keys = new ArrayList<>();
				SecretKey secretKey = null;
				byte[] descPublicKey = null;
				byte[] descPrivateKey = null;
				byte[] valuePublicKey = null;
				byte[] valuePrivateKey = null;
				
				descPublicKey = serializer.serialize(SecretKeyPair.ServerPublicKey.getDesc());
				descPrivateKey = serializer.serialize(SecretKeyPair.ServerPrivateKey.getDesc());
				valuePublicKey = arg0.get(descPublicKey);
				valuePrivateKey = arg0.get(descPrivateKey);
				if (valuePublicKey == null || valuePrivateKey == null) {
					return null;
				}
				secretKey = new SecretKey();
				secretKey.setPublicKey(serializer.deserialize(valuePublicKey));
				secretKey.setPrivateKey(serializer.deserialize(valuePrivateKey));
				keys.add(secretKey);
				
				descPublicKey = serializer.serialize(SecretKeyPair.ClientPublicKey.getDesc());
				descPrivateKey = serializer.serialize(SecretKeyPair.ClientPrivateKey.getDesc());
				valuePublicKey = arg0.get(descPublicKey);
				valuePrivateKey = arg0.get(descPrivateKey);
				if (valuePublicKey == null || valuePrivateKey == null) {
					return null;
				}
				secretKey = new SecretKey();
				secretKey.setPublicKey(serializer.deserialize(valuePublicKey));
				secretKey.setPrivateKey(serializer.deserialize(valuePrivateKey));
				keys.add(secretKey);
				return keys;
			}
		});
		
		return result;
	}

}
