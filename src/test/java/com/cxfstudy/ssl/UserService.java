package com.cxfstudy.ssl;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface UserService {
	@WebMethod
	@WebResult List<User> list();

}

