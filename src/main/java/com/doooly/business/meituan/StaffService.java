package com.doooly.business.meituan;



import com.doooly.common.meituan.StaffTypeEnum;
import com.doooly.entity.meituan.StaffInfoVO;

import java.util.List;

/**
 * Created by wanghai on 2018/12/11.
 */
public interface StaffService {

     /**
      * 批量同步员工
      * @param staffInfoVOList
      * @param staffTypeEnum
      * @return
      * @throws Exception
      */
     List<StaffInfoVO> batchSynStaffs(List<StaffInfoVO> staffInfoVOList, StaffTypeEnum staffTypeEnum) throws Exception;

     /**
      * 批量更新员工
      * @param staffInfoVOList
      * @param staffTypeEnum
      * @return
      * @throws Exception
      */
     List<StaffInfoVO> batchUpdateStaff(List<StaffInfoVO> staffInfoVOList, StaffTypeEnum staffTypeEnum) throws Exception;


     /**
      * 批量查询员工
      * @param staffs
      * @param staffTypeEnum
      * @return
      * @throws Exception
      */
     List<StaffInfoVO> getStaffs(List<String> staffs, StaffTypeEnum staffTypeEnum) throws Exception;

     /**
      * 批量删除员工
      * @param staffs
      * @param staffTypeEnum
      * @return
      * @throws Exception
      */
     List<StaffInfoVO> deleteStaffs(List<String> staffs, StaffTypeEnum staffTypeEnum) throws Exception;


}
