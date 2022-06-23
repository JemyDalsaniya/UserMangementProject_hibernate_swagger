package com.mvchibernate.dao;

import java.util.List;

import com.mvchibernate.model.Address;
import com.mvchibernate.model.User;

public interface UserDao {

	/**
	 * 
	 * @param user
	 */
	void userRegister(User user);

	boolean userLogin(User user);

	void updatePassword(User user);

	List<User> userDetail(User user);

	List<User> adminDetail(User user);

	List<Address> userAddress(int id);

	List<User> viewUser(User user);

	boolean checkMail(String email);

	void deleteUser(int id);

	void changeRole(int id);

	void updateUser(User user);

	List<User> userIdDetail(int id);

	void updateAddress(List<Address> addList);

}
