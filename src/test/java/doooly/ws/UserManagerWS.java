package doooly.ws;

import javax.jws.WebService;

import doooly.entity.User;
@WebService(targetNamespace="doooly.user.service")
public class UserManagerWS {

	public User queryByID(int id){
		User u = new User();
		return u;
	}
}
