package com.doooly.business.meituan.impl;

import com.doooly.business.meituan.StaffService;
import com.doooly.common.meituan.MeituanConstants;
import com.doooly.common.meituan.MeituanRequest;
import com.doooly.common.meituan.StaffTypeEnum;
import com.doooly.entity.meituan.Staff;
import com.doooly.entity.meituan.StaffInfoVO;
import com.doooly.entity.meituan.StaffQuery;
import com.google.gson.reflect.TypeToken;
import com.reach.redis.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wanghai on 2018/12/11.
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Override
    public List<StaffInfoVO> batchSynStaffs(List<StaffInfoVO> staffInfoVOList, StaffTypeEnum staffTypeEnum) throws Exception{
        Staff staff = new Staff();
        staff.setStaffType(staffTypeEnum.getCode());
        staff.setStaffInfos(staffInfoVOList);
        MeituanRequest meituanRequest = new MeituanRequest();
        String content = GsonUtils.son.toJson(staff);
        meituanRequest.setContent(content);
        String ret = meituanRequest.doPost(MeituanConstants.URL_ADD_STAFF);
        Map<String,Object> retMap = GsonUtils.son.fromJson(ret,Map.class);
        String data = String.valueOf(retMap.get("data"));
        List<StaffInfoVO> list = GsonUtils.son.fromJson(data,new TypeToken<List<StaffInfoVO>>(){}.getType());
        return list;
    }


    @Override
    public List<StaffInfoVO> getStaffs(List<String> staffs, StaffTypeEnum staffTypeEnum) throws Exception{
        StaffQuery staffQuery = new StaffQuery();
        staffQuery.setStaffType(staffTypeEnum.getCode());
        staffQuery.setVendorName(MeituanConstants.TOKEN);
        staffQuery.setStaffContents(staffs);
        MeituanRequest meituanRequest = new MeituanRequest();
        meituanRequest.setContent(GsonUtils.son.toJson(staffQuery));
        String ret = meituanRequest.doPost(MeituanConstants.URL_QUERY_STAFF);
        Map<String,Object> retMap = GsonUtils.son.fromJson(ret,Map.class);
        String data = String.valueOf(retMap.get("data"));
        List<StaffInfoVO> staffInfoVOS = GsonUtils.son.fromJson(data,new TypeToken<List<StaffInfoVO>>(){}.getType());
        return staffInfoVOS;
    }

    @Override
    public List<StaffInfoVO> deleteStaffs(List<String> staffs, StaffTypeEnum staffTypeEnum) throws Exception {
        StaffQuery staffQuery = new StaffQuery();
        staffQuery.setStaffType(staffTypeEnum.getCode());
        staffQuery.setStaffContents(staffs);
        MeituanRequest meituanRequest = new MeituanRequest();
        meituanRequest.setContent(GsonUtils.son.toJson(staffQuery));
        String ret = meituanRequest.doPost(MeituanConstants.URL_DEL_STAFF);
        Map<String,Object> retMap = GsonUtils.son.fromJson(ret,Map.class);
        String data = String.valueOf(retMap.get("data"));
        List<StaffInfoVO> staffInfoVOS = GsonUtils.son.fromJson(data,new TypeToken<List<StaffInfoVO>>(){}.getType());
        return staffInfoVOS;
    }

    @Override
    public List<StaffInfoVO> batchUpdateStaff(List<StaffInfoVO> staffInfoVOList, StaffTypeEnum staffTypeEnum) throws Exception {
        Staff staff = new Staff();
        staff.setStaffType(staffTypeEnum.getCode());
        staff.setStaffInfos(staffInfoVOList);
        MeituanRequest meituanRequest = new MeituanRequest();
        String content = GsonUtils.son.toJson(staff);
        meituanRequest.setContent(content);
        String ret = meituanRequest.doPost(MeituanConstants.URL_UPDATE_STAFF);
        Map<String,Object> retMap = GsonUtils.son.fromJson(ret,Map.class);
        String data = String.valueOf(retMap.get("data"));
        List<StaffInfoVO> list = GsonUtils.son.fromJson(data,new TypeToken<List<StaffInfoVO>>(){}.getType());
        return list;
    }

}
