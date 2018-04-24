package com.doooly.business.selfApplication;

import java.util.Date;

import com.doooly.dto.common.MessageDataBean;

/**
 * @Description:
 * @author: wenwei.yang
 * @date: 2017-10-09
 */
public interface SelfApplicationServiceI {

	MessageDataBean getAllCompany();

	MessageDataBean getDepartmentDatasByGroupId(String companyId);

	MessageDataBean submitData(String companyId, String departmentId, String name, String telephone, String workNumber,
			String sex, Date hiredate);


}
