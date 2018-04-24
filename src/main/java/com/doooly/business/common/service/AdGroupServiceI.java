package com.doooly.business.common.service;

import com.alibaba.fastjson.JSONObject;
import com.doooly.dto.common.MessageDataBean;

/**
 * 企业信息表服务接口
 * 
 * @author 赵清江
 * @date 2016年7月14日
 * @version 1.0
 */
public interface AdGroupServiceI {

	/**
	 * 验证卡号 1:先对卡号进行拆分,得到udid和officialNumber
	 * 2:对udid进行验证,得到企业名称,若无该企业,即表明该卡号无效,返回true 3:对officialNumber进行验证,判定是否正式员工
	 * 4:若不是正式员工,则更具该企业的relation_start_no和relation_end_no判定是否亲属(在两者之间并包括两者)
	 * 5:若既不是正式员工,又不是亲属,即表明该卡号无效,返回true 6:若都符合,返回true
	 * 
	 * @param cardNumber
	 * @return
	 */
	public boolean isCardNumberValid(String cardNumber);

	/**
	 * 根据企业编号查找企业名称
	 * 
	 * @param groupNumber
	 * @return
	 */
	public String getGroupName(long id);

	/**
	 * 企业口令激活-获取企业问题
	 * 
	 * @param req
	 * @return groupQuestion-企业口令问题
	 */
	public MessageDataBean getGroupCommandInfo(JSONObject param) throws Exception;

	/**
	 * 企业口令激活 - 通过口令获取企业集合
	 * 
	 * @param groupCommand-企业口令
	 * @return groupList-企业集合
	 */
	public MessageDataBean getGroupByCommand(JSONObject param) throws Exception;
}
