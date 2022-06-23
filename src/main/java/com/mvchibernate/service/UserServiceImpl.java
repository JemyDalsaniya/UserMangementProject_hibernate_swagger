package com.mvchibernate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvchibernate.dao.UserDao;
import com.mvchibernate.model.Address;
import com.mvchibernate.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao object;

	@Override
	public void userRegister(User user) {
		object.userRegister(user);
		System.out.println("in service impl" + user);
	}

	@Override
	public boolean userLogin(User user) {
		System.out.println("user in service impl" + user);
		return object.userLogin(user);
	}

	@Override
	public void updatePassword(User user) {
		object.updatePassword(user);
	}

	@Override
	public List<User> userDetail(User user) {
		return object.userDetail(user);
	}

	@Override
	public List<User> adminDetail(User user) {
		return object.adminDetail(user);
	}

	@Override
	public List<Address> userAddress(int id) {
		return object.userAddress(id);
	}

	@Override
	public List<User> viewUser(User user) {
		return object.viewUser(user);
	}

	@Override
	public boolean checkMail(String email) {
		return object.checkMail(email);
	}

	@Override
	public void deleteUser(int id) {
		object.deleteUser(id);
	}

	@Override
	public void changeRole(int id) {
		object.changeRole(id);
	}

	@Override
	public void updateUser(User user) {
		object.updateUser(user);
	}

	@Override
	public List<User> userIdDetail(int id) {
		return object.userIdDetail(id);
	}

	@Override
	public void updateAddress(List<Address> addList) {
		object.updateAddress(addList);
	}

}
