package com.mvchibernate.model;

import java.util.Base64;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int userId;

	@Column(name = "name")
	private String userName;

//	@Email
	@NotBlank(message = "Please enter your email addresss.")
	@Column(name = "email")
	private String userEmail;

	// @NotEmpty(message = "Please enter your password.")
	// @Size(min = 6, max = 15, message = "Your password must between 6 and 15
	// characters")
	@Column(name = "password")
	private String userPassword;

	@Column(name = "contact")
	private String userContact;
	@Column(name = "gender")
	private String userGender;
	@Column(name = "hobby")
	private String userHobby;
	@Column(name = "dob")
	private String userDOB;

	@Column(name = "status")
	private boolean userStatus;
	@Lob
	private byte[] image;

	@Transient
	private String base64Image;

	@Transient
	private CommonsMultipartFile file;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Address> address;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserContact() {
		return userContact;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserHobby() {
		return userHobby;
	}

	public void setUserHobby(String userHobby) {
		this.userHobby = userHobby;
	}

	public String getUserDOB() {
		return userDOB;
	}

	public void setUserDOB(String userDOB) {
		this.userDOB = userDOB;
	}

	public boolean getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Boolean userStatus) {
		this.userStatus = userStatus;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public String getBase64Image() {
		return Base64.getEncoder().encodeToString(image);
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.image = file.getBytes();
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail + ", userPassword="
				+ userPassword + ", userContact=" + userContact + ", userGender=" + userGender + ", userHobby="
				+ userHobby + ", userDOB=" + userDOB + ", userStatus=" + userStatus + ", address=" + address + "]";
	}

}
