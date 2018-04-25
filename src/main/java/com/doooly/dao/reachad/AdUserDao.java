package com.doooly.dao.reachad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.doooly.common.dao.BaseDaoI;
import com.doooly.entity.reachad.AdUser;
import com.doooly.entity.reachad.AdUserConn;
import com.doooly.entity.reachad.AdUserPersonalInfo;

public interface AdUserDao extends BaseDaoI<AdUser> {
	int deleteByPrimaryKey(Long id);

	int insert(AdUser record);

	int insertSelective(AdUser record);

	AdUser selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(AdUser record);

	int updateByPrimaryKey(AdUser record);

	/**
	 * 该方法根据用户会员卡号或绑定的手机号查找该用户ID
	 * 
	 * @param AdUser
	 * @return
	 */
	public AdUser get(AdUser user);

	public int updatePwd(@Param("mobile") String mobile, @Param("newPwd") String newPwd);

	/**
	 * 修改用户激活状态
	 * 
	 * @param adUser
	 * @return
	 */
	public int updateActiveStatus(AdUser adUser);

	public AdUser findAvailableByCardNumber(AdUser adUser);

	/**
	 * 获取近两周每天数目 type 1 家属 0 员工
	 */
	public Integer getCountByWeekActive(int type, int i);

	/**
	 * 获取近六个月每个月数目 * type 1 家属 0 员工
	 */
	public Integer getCountByMonthActive(int type, int i);

	/**
	 * 获取截止至两周的每天的数目 * type 1 家属 0 员工
	 */
	public Integer getCountByWeekMember(int type, int i);

	/**
	 * 获取截止至近六个月的每个月的数目 * type 1 家属 0 员工
	 */
	public Integer getCountByMonthMember(int type, int i);

	/**
	 * 获取当天激活数目 type 1 家属 0 员工 null 全部
	 */
	public Integer getCountByToday(Integer type);

	/**
	 * 获取截止今天总激活数目 type 1 家属 0 员工
	 */
	public Integer getCountByTotal(Integer type);

	/**
	 * 根据查询条件获取用户信息
	 * 
	 * @param adUser
	 * @return
	 */
	public int getUserCounts(AdUser adUser);

	/**
	 * 根据查询条件拼接会员手机号，以‘,’逗号分隔
	 * 
	 * @param userMap
	 * @return
	 */
	public String getUserTels(Map<String, Object> paramMap);

	public Long getIdByPhoneOrCard(String phoneOrCard);

	/**
	 * 获取用户财富值
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	Integer getTotalWealth(String userId);

	/**
	 * 更新用户财富值
	 * 
	 * @param userId
	 *            用户id
	 * @param wealth
	 *            财富值
	 * @return
	 */
	Integer updateWealthByUserId(@Param(value = "userId") String userId, @Param(value = "wealth") int wealth);

	/**
	 * 获取用户总财富值在所有用户中的百分比
	 * 
	 * @param wealth
	 *            财富值
	 * @return 排名百分比
	 */
	Integer getTotalWealthPercent(int wealth);

	/**
	 * 获取财富值最高的用户
	 * 
	 */
	AdUser getUserWithMaxWealth();

	/**
	 * 根据卡号或手机号获取用户信息
	 * 
	 */
	public AdUser getUserInfo(AdUser adUser);

	AdUser getById(@Param("id") String userId);

	/**
	 * 通过用户id查询用户的账户信息
	 * 
	 * @param userId
	 *            会员id
	 * @return
	 */
	AdUserConn findAdUserConnByUserId(String userId);

	/**
	 * 更新用户个人信息(ad_user)
	 * 
	 * @param adUserConn
	 *            用户个人信息
	 * @return
	 */
	void updateUserData(AdUserConn adUserConn);

	/**
	 * 更新用户个人信息(ad_user_personal_info)
	 * 
	 * @param adUserConn
	 *            用户个人信息
	 * @return
	 */
	void updatePersonalData(AdUserConn adUserConn);

	void insertPersonalData(AdUserConn adUserConn);

	Integer getPersonalInfoByUserId(AdUserConn adUserConn);

	Integer getMemberCount(Integer userId);

	AdUserConn getFamilyUserInfo(String userId);

	// 根据userId查询可用积分
	BigDecimal getAvailablePoint(String userId);

	// 根据userId查询共享积分
	BigDecimal getSharedPoints(String userId);

	// 根据userId查询待返积分
	BigDecimal getReturnPoint(String userId);

	/** 根据被邀请人userId查询邀请人姓名 */
	String findInviterNameByInviteeId(String userId);

	int addActiveUser(AdUser user);

	/**
	 * 根据手机号/工号获取用户信息
	 */
	public AdUser getUserByTelWorkNum(AdUser adUser);

	/**
	 * 根据手机号获取用户信息
	 */
	public AdUser findByTelephone(AdUser adUser);

	/**
	 * 根据工号获取用户信息
	 */
	public AdUser findUserByNameAndWorkNumber(AdUser adUser);

	/**
	 * 保存用户个人信息
	 */
	public void savePersonalInfo(AdUserPersonalInfo adUserPersonalInfo);

	/**
	 * 更新用户个人信息(user,personal)
	 */
	public void updateUserPersonal(AdUser user);

	/**
	 * 删除用户信息(逻辑删除)
	 */
	public void deleteAdUser(AdUser user);

	/**
	 * 查詢家属邀请所有用户
	 * 
	 * @param userList
	 * @return
	 */
	public List<AdUser> getFamilysByids(@Param("userList") List<String> userList);

	/**
	 * 根据手机号查询
	 * 
	 * @param mobile
	 * @return
	 */
	AdUser findByMobile(String mobile);

	/**
	 * 根据会员卡号获取会员信息
	 * 
	 * @param cardNumber
	 * @return
	 */
	public AdUser findByCardNumber(String cardNumber);

	/**
	 * 查询单个插入的员工
	 * 
	 * @param adUser
	 * @return
	 */
	public AdUser getUser(AdUser adUser);

	public AdUser findByMobileBySelfApplication(String mobile);

	public List<Map> getMyFamilyInfo(@Param("userId") String userId);

	void updateSelfInfomation(AdUser user);

	String find12NumberCountByGroupId(@Param("companyId") String companyId, @Param("groupNum") String groupNum);

	Integer isDongHang(@Param("userId") Integer userId);

	ArrayList<String> findByTelephones(@Param("mobiles") List<String> mobiles);

	List<String> getIds(@Param("userIds") List<String> userIds);

	/**
	 * 根据企业编号生成企业下新用户编号
	 * 
	 * @param groupId-企业ID
	 */
	public String createCardNumber(@Param("groupId") String groupId);

	/**
	 * 部分字段存储用户
	 * 
	 * @return adUser-id
	 */
	public int saveUser(AdUser adUser);

	void addIntegral(@Param("userId") Long userId, @Param("addIntegral") BigDecimal addIntegral);

	AdUserConn getOrderTotal(String userId);// 获取用户各状态(已下单、已完成、已取消)订单数

	/**
	 * 获取微信端已登录用户免登陆信息
	 * 
	 * @param cardNumber
	 *            - 用户卡号
	 * @return userId - 用户Id
	 * @return userName - 用户名
	 * @return mobile - 手机号
	 * @return groupShortName - 企业简称
	 * 
	 */
	public HashMap<String, Object> getWechatUserInitInfo(AdUser adUser);

	/**
	 * 查询手机号对应会员是否为武钢员工
	 * 
	 * @param telephone
	 *            - 手机号
	 */
	public AdUser getUserByTelephoneBloc(@Param("telephone") String telephone);
}