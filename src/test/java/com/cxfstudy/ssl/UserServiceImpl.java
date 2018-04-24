package com.cxfstudy.ssl;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

	public List<User> list() {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setId("" + i);
			user.setName("user_" + i);
			user.setPassword("password_" + i);
			users.add(user);
		}
		return users;
	}

}
