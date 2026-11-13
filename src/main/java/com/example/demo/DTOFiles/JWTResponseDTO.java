package com.example.demo.DTOFiles;

import java.io.Serializable;

public class JWTResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	private String userId;
	private String type = "Bearer ";
	private String token;

	public JWTResponseDTO(String userId, String token) {
		super();
		this.userId = userId;
		this.type = type;
		this.token = token;
	}

	public JWTResponseDTO() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "JWTResponseDTO [userId=" + userId + ", type=" + type + ", token=" + token + "]";
	}

}
