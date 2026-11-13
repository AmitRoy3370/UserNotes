package com.example.demo.DTOFiles;

import java.io.Serializable;

import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserInterest;
import com.example.demo.Models.UserModels.UserRoles;

public class UserResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;

	private String id;

	private String userName;

	private String fullName;

	private String profileImage;

	private UserContact contact;

	private UserGender gender;

	private UserInterest interst;

	private UserRoles roles;

	public UserResponseDTO(String id, String userName, String fullName, String profileImage, UserContact contact,
			UserGender gender, UserInterest interst, UserRoles roles) {
		super();
		this.id = id;
		this.userName = userName;
		this.fullName = fullName;
		this.profileImage = profileImage;
		this.contact = contact;
		this.gender = gender;
		this.interst = interst;
		this.roles = roles;
	}

	public UserResponseDTO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public UserContact getContact() {
		return contact;
	}

	public void setContact(UserContact contact) {
		this.contact = contact;
	}

	public UserGender getGender() {
		return gender;
	}

	public void setGender(UserGender gender) {
		this.gender = gender;
	}

	public UserRoles getRoles() {
		return roles;
	}

	public void setRoles(UserRoles roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserInterest getInterst() {
		return interst;
	}

	public void setInterst(UserInterest interst) {
		this.interst = interst;
	}

	@Override
	public String toString() {
		return "UserResponseDTO [id=" + id + ", userName=" + userName + ", fullName=" + fullName + ", profileImage="
				+ profileImage + ", contact=" + contact + ", gender=" + gender + ", interst=" + interst + ", roles="
				+ roles + "]";
	}

}
