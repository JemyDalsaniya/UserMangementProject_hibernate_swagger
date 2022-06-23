package com.mvchibernate.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mvchibernate.model.Address;
import com.mvchibernate.model.User;
import com.mvchibernate.service.UserService;

@Controller
//@Transactional
//@EnableWebMvc
public class UserController {

	@Autowired
	private UserService service;

	@RequestMapping("/")
	public String login() {
		return "Userlogin";
	}

	@RequestMapping("/userlogin")
	public String userlogin() {
		return "Userlogin";
	}

	@RequestMapping("/adminHome")
	public String adminHome(Model model, User user, HttpSession session) {
		List<User> adminDetailList = service.adminDetail(user);
		int id = adminDetailList.get(0).getUserId();
		List<Address> addressList = service.userAddress(id);
		session.setAttribute("adminDetails", adminDetailList);
		session.setAttribute("addressList", addressList);
		return "AdminHomePage";
	}

	@RequestMapping("/userHome")
	public String userHome(Model model, User user) {
		return "UserHomePage";
	}

	@RequestMapping(value = { "/register", "/addUser" })
	public String register(HttpSession session) {
		session.removeAttribute("userDetails");
		session.removeAttribute("addressList");
		session.removeAttribute("adminDetails");
		return "UserRegister";
	}

	@RequestMapping("/forgetPassword")
	public String forgetPassword() {
		return "ForgetPassword";
	}

	@PostMapping(value = { "/UserRegister", "/addUser" }, consumes = "multipart/form-data")
	public String registration(Model model, @ModelAttribute("registerForm") User user,
			@RequestParam("addStreet[]") String[] addStreet, @RequestParam("addLandmark[]") String[] addLandmark,
			@RequestParam("addPincode[]") String[] addPincode, @RequestParam("addCity[]") String[] addCity,
			@RequestParam("addState[]") String[] addState, HttpSession session,
			@RequestParam(value = "user", required = false) String userName,
			@RequestParam(value = "id", required = false) String id) throws Exception {

		// set address in database
		Address address = null;
		List<Address> addList = new ArrayList<Address>();

		int count = 0;
		while (count < addStreet.length) {
			address = new Address();
			address.setAddStreet(addStreet[count]);
			address.setAddCity(addCity[count]);
			address.setAddLandmark(addLandmark[count]);
			address.setAddPincode(addPincode[count]);
			address.setAddState(addState[count]);
			address.setUser(user);
			addList.add(address);
			count++;
		}
		user.setAddress(addList);
		service.userRegister(user);
		if (userName != null) {
			List<User> adminDetailList = service.adminDetail(user);
			int userId = adminDetailList.get(0).getUserId();
			List<Address> addressList = service.userAddress(userId);
			session.setAttribute("adminDetails", adminDetailList);
			session.setAttribute("addressList", addressList);
			return "AdminHomePage";
		} else {

			return "Userlogin";
		}

	}

	@PostMapping(value = "/Userlogin")
	public String userLogin(Model model, @ModelAttribute("loginForm") User user, HttpSession session) throws Exception {

		boolean isAdmin = service.userLogin(user);
		if (isAdmin) {
			if (user.getUserStatus()) {
				List<User> adminDetailList = service.adminDetail(user);
				int id = adminDetailList.get(0).getUserId();
				List<Address> addressList = service.userAddress(id);
				session.setAttribute("adminDetails", adminDetailList);
				session.setAttribute("addressList", addressList);
				return "AdminHomePage";
			} else {
				List<User> userDetailList = service.userDetail(user);
				int id = userDetailList.get(0).getUserId();
				List<Address> addressList = service.userAddress(id);
				session.setAttribute("userDetails", userDetailList);
				session.setAttribute("addressList", addressList);
				return "UserHomePage";
			}
		}
		session.setAttribute("errorMSG", "Invalid ID or PassWord!!");
		return "Userlogin";

	}

	@PostMapping(value = "/ForgetPassword")
	public String updatePassword(Model model, @ModelAttribute("forgetPwd") User user) throws Exception {

		service.updatePassword(user);
		return "Userlogin";
	}

	@RequestMapping(value = "/view")
	public String viewuser(Model model, User user, HttpSession session) {
		List<User> allUser = service.viewUser(user);
		session.setAttribute("allusers", allUser);
		return "ViewUser";
	}

	@PostMapping(value = "/DeleteUser")
	public void deleteUser(@RequestParam("userId") int id, HttpServletResponse response) throws IOException {
		service.deleteUser(id);
		response.getWriter().write("in success");
	}

	@GetMapping(value = "/checkEmail")
	public void checkMail(@RequestParam("email") String email, HttpServletResponse response) throws IOException {
		boolean flag = service.checkMail(email);
		if (flag) {
			response.getWriter().write("true");
		} else {
			response.getWriter().write("false");
		}
	}

	@PostMapping(value = "/ChangeRole")
	public String changeRole(@RequestParam("userId") int id, HttpServletResponse response) throws IOException {
		service.changeRole(id);
		response.getWriter().write("in success");
		return "AdminHomePage";

	}

	@RequestMapping(value = "/editProfile")
	public String editUser(User user, HttpSession session, Model model,
			@RequestParam(value = "user", required = false) String userName,
			@RequestParam(value = "id", required = false) String id) {

		model.addAttribute("user", userName);

		if (userName.equals("admin")) {
			session.removeAttribute("userDetails");
			session.removeAttribute("addressList");
			user.setUserStatus(true);
			List<User> adminDetailList = service.adminDetail(user);

			byte[] adminImage = adminDetailList.get(0).getImage();
			session.setAttribute("adminImage", adminImage);

			session.setAttribute("adminDetails", adminDetailList);
			List<Address> addressList = adminDetailList.get(0).getAddress();
			session.setAttribute("addressList", addressList);
		} else if (userName.equals("userEdit") || userName.equals("adminEdit")) {
			session.removeAttribute("adminDetails");

			List<User> userDetailList = service.userIdDetail(Integer.parseInt(id));

			byte[] byteimage = userDetailList.get(0).getImage();
			session.setAttribute("userImage", byteimage);

			System.out.println("image byte is" + byteimage);
			List<Address> addressList = service.userAddress(Integer.parseInt(id));
			session.setAttribute("userDetails", userDetailList);
			session.setAttribute("addressList", addressList);
		}
		return "UserRegister";
	}

	@PostMapping(value = "/UserEdit")
	public String updateUser(Model model, @ModelAttribute("registerForm") User user, HttpSession session,
			@RequestParam("addStreet[]") String[] addStreet, @RequestParam("addLandmark[]") String[] addLandmark,
			@RequestParam("addPincode[]") String[] addPincode, @RequestParam("addCity[]") String[] addCity,
			@RequestParam("addState[]") String[] addState, @RequestParam("addressId[]") String[] addressId,
			@RequestParam(value = "file", required = false) CommonsMultipartFile file,
			@RequestParam(value = "storeImage", required = false) String image,
			@RequestParam(value = "user", required = false) String userName,
			@RequestParam(value = "id", required = false) String id) throws IOException {

		Address address = null;
		List<Address> addList = new ArrayList<Address>();

		int count = 0;
		while (count < addStreet.length) {
			address = new Address();
			address.setAddressId(addressId[count]);
			address.setAddStreet(addStreet[count]);
			address.setAddCity(addCity[count]);
			address.setAddLandmark(addLandmark[count]);
			address.setAddPincode(addPincode[count]);
			address.setAddState(addState[count]);
			address.setUser(user);
			addList.add(address);
			count++;
		}

		if (userName.equals("admin")) {

			
			user.setUserStatus(true);
		}
		if (file.isEmpty() && (userName.equals("userEdit") || userName.equals("adminEdit"))) {
			byte[] imagebytes = (byte[]) session.getAttribute("userImage");
			user.setImage(imagebytes);
		} else if (file.isEmpty()) {
			byte[] adminImagebytes = (byte[]) session.getAttribute("adminImage");
			user.setImage(adminImagebytes);
		}
		service.updateUser(user);

		service.updateAddress(addList);

		if (userName.equals("admin")) {
			session.removeAttribute("userDetails");
			List<User> adminDetailList = service.userIdDetail(user.getUserId());
			System.out.println("adminDetailList on register page" + adminDetailList);
			List<Address> addressList = service.userAddress(user.getUserId());
			session.setAttribute("adminDetails", adminDetailList);
			session.setAttribute("addressList", addressList);
			return "AdminHomePage";
		} else if (userName.equals("adminEdit")) {
			List<User> userDetailList = service.userIdDetail(user.getUserId());
			List<Address> addressList = service.userAddress(user.getUserId());
			session.setAttribute("userDetails", userDetailList);
			session.setAttribute("addressList", addressList);
			return "ViewUser";
		} else if (userName.equals("userEdit")) {
			List<User> userDetailList = service.userIdDetail(user.getUserId());
			List<Address> addressList = service.userAddress(user.getUserId());
			session.setAttribute("userDetails", userDetailList);
			session.setAttribute("addressList", addressList);
			return "UserHomePage";
		} else {
			return "Userlogin";
		}
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		// session.invalidate();
		return "Userlogin";
	}

}
