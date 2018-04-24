package com.doooly.publish.rest.life;

import com.alibaba.fastjson.JSONObject;

public interface FamilyInviteAppRestServiceI {

	JSONObject myInvitation(JSONObject jsonReq);

	JSONObject checkTelephone(JSONObject data);

	JSONObject getGroupInfo(JSONObject jsonObj);

	JSONObject saveUserNotActive(JSONObject jsonObj);

	JSONObject getMyFamilyInfo(JSONObject jsonObj);

}
