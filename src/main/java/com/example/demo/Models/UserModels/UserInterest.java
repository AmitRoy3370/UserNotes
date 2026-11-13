package com.example.demo.Models.UserModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "UserInterest")
public class UserInterest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;

	@Id
	private String id;

	@Indexed(unique = true)
	@NonNull
	private String userId;

	@Indexed
	private List<String> interests = new ArrayList<>();

	public UserInterest(String userId, List<String> interests) {
		super();
		this.userId = userId;
		this.interests = interests;
	}

	public UserInterest() {
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

	public List<String> getInterests() {
		return interests;
	}

	public void setInterests(List<String> interests) {
		this.interests = interests;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserInterest [id=" + id + ", userId=" + userId + ", interests=" + interests + "]";
	}

}
