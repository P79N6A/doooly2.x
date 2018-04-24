package com.doooly.business.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doooly.business.common.service.AdActiveCodeServiceI;
import com.doooly.dao.reachad.AdActiveCodeDao;
import com.doooly.entity.reachad.AdActiveCode;

/**
 * 激活码服务类
 * @author 赵清江
 * @date 2016年7月15日
 * @version 1.0
 */
@Service
public class AdActiveCodeService implements AdActiveCodeServiceI {

	@Autowired
	private AdActiveCodeDao adActiveCodeDao;
	
	@Override
	public int isActiveCodeValid(String cardNumber, String code) throws Exception{
		AdActiveCode activeCode = new AdActiveCode();
		activeCode.setCardNumber(cardNumber);
		activeCode.setCode(code);
		List<AdActiveCode> list = adActiveCodeDao.get(activeCode);
		if (list.size() != 0) {
			if (list.get(0).getIsUsed().equals("0")) {
				return ACTIVE_CODE_NOT_USED;
			}
			return ACTIVE_CODE_USED;
		}
		return ACTIVE_CODE_NOT_EXIST;
	}

	@Override
	public int useActiveCode(String cardNumber) throws Exception {
		AdActiveCode code = new AdActiveCode();
		code.setCardNumber(cardNumber);
		
		code.setIsUsed(AdActiveCode.ACTIVE_CODE_USED);//设置激活码使用状态
		code.setUsedDate(new Date());//设置使用时间
		
		return adActiveCodeDao.updateUseStatus(code);
	}

}
