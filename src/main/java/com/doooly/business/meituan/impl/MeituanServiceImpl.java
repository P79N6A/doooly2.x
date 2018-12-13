package com.doooly.business.meituan.impl;

import com.doooly.business.meituan.MeituanService;
import com.doooly.common.util.BeanMapUtil;
import com.doooly.entity.meituan.EasyLogin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/13.
 */
public class MeituanServiceImpl implements MeituanService{

    @Override
    public String easyLogin(String entToken, String staffNo, String staffPhoneNo) {
        EasyLogin easyLogin = new EasyLogin();
        easyLogin.setEntToken(entToken);
        easyLogin.setStaffNo(staffNo);
        easyLogin.setStaffPhoneNo(staffPhoneNo);
        Map<String,Object> paramMap = BeanMapUtil.transBean2Map(easyLogin);
        paramMap =  BeanMapUtil.sortMapByKey(paramMap);
        return null;
    }
}
