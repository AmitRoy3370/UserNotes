package com.example.demo.Models.UserModels;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "UserContact")
public class UserContact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	@Id
	private String id;

	@Indexed(unique = true)
	private String userId;

	@Indexed(unique = true)
	private String email;

	@Indexed(unique = true)
	private String phoneNumber;

	public UserContact(String userId, String email, String phoneNumber) {
		super();
		this.userId = userId;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public UserContact() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "UserContact [id=" + id + ", userId=" + userId + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ "]";
	}

}
