package com.doooly.dao.reachad;

import com.doooly.entity.reachad.AdGroup;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * adGroup Dao层
 * 
 * @author 赵清江
 * @date 2016年7月14日
 * @version 1.0
 */
public interface AdGroupDao {

	/**
	 * 获取公司卡号信息
	 * 
	 * @param adGroup
	 * @return
	 */
	public AdGroup findGroupByUDID(String udid);

	/**
	 * 
	 * @param groupNumber
	 * @return
	 */
	public String findNameByID(long id);

	int deleteByPrimaryKey(Long id);

	int insert(AdGroup record);

	int insertSelective(AdGroup record);

	AdGroupDao selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(AdGroup record);

	int updateByPrimaryKey(AdGroup record);

	AdGroup getGroupLogoByUserId(Integer userId);

	AdGroup findGroupByGroupName(String groupName);

	public AdGroup findGroupByUserId(@Param("userId") String userId);

	public AdGroup findGroupByID(String id);

	public List<AdGroup> getAllCompany();

	AdGroup findGroupByGroupNum(String groupNum);

	/**
	 * 通过企业口令获取企业集合
	 * 
	 * @param paramMap
	 *            groupCommand-企业口令 groupId-企业ID
	 */
	List<AdGroup> getGroupListByCommand(HashMap<String, Object> paramMap);
}