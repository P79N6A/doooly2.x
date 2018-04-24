package com.doooly.publish.rest.life;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public interface ComplaintServiceI {
	
	String complaintSave(@Context HttpServletRequest request);

}
