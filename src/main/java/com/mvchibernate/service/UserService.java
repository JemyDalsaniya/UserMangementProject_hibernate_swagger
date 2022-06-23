package com.mvchibernate.service;

import java.util.List;

import com.mvchibernate.model.Address;
import com.mvchibernate.model.User;

public interface UserService {

	/**
	 * 
	 * @param user
	 */
	void userRegister(User user);

	/**
	 * 
	 * @param user
	 * @return
	 */
	boolean userLogin(User user);

	/**
	 * 
	 * @param user
	 */
	void updatePassword(User user);

	/**
	 * 
	 * @param user
	 * @return
	 */
	List<User> userDetail(User user);

	/**
	 * 
	 * @param user
	 * @return
	 */
	List<User> adminDetail(User user);

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<Address> userAddress(int id);

	/**
	 * 
	 * @param user
	 * @return
	 */
	List<User> viewUser(User user);

	/**
	 * 
	 * @param email
	 * @return
	 */
	boolean checkMail(String email);

	/**
	 * 
	 * @param id
	 */
	void deleteUser(int id);

	/**
	 * 
	 * @param id
	 */
	void changeRole(int id);

	/**
	 * 
	 * @param user
	 */
	void updateUser(User user);

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<User> userIdDetail(int id);

	/**
	 * 
	 * @param addList
	 */
	void updateAddress(List<Address> addList);

}
