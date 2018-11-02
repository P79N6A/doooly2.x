package com.doooly.business.dict.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doooly.business.dict.ConfigDictServiceI;
import com.doooly.dao.reachad.AdConfigDictDao;

@Service
public class ConfigDictServiceImpl implements ConfigDictServiceI {
	@Autowired
	private StringRedisTemplate stringRedis;
	@Autowired
	private AdConfigDictDao dictDao;

	@Override
	public String getValueByTypeAndKey(String dictType, String dictKey) {
		String value = stringRedis.opsForValue().get(dictKey);
		if (StringUtils.isBlank(value)) {
			value = dictDao.getValueByTypeAndKey(dictType, dictKey);
			if(StringUtils.isNotBlank(value)){
				value = value.replaceAll("%s", "\n");
				// 设置超时时间，默认1天
				stringRedis.opsForValue().set(dictKey, value, 1, TimeUnit.DAYS);
			}
		}
		return value;
	}

	@Override
	public List<String> getValueListByTypeAndKey(String dictType, String dictKey) {
		List<String> values = new ArrayList<>();
		String value = stringRedis.opsForValue().get(dictKey);
		if (StringUtils.isBlank(value)) {
			values = dictDao.getValueListByTypeAndKey(dictType, dictKey);
			// 设置超时时间，默认1天
			value = JSONObject.toJSONString(values).replaceAll("%s", "\n");
			stringRedis.opsForValue().set(dictKey, value, 1, TimeUnit.DAYS);
			return values;
		}
		values = JSONArray.parseArray(value, String.class);
		return values;
	}

}
