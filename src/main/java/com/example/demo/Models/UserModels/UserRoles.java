
package com.example.demo.Models.UserModels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.io.Serializable;
import java.util.HashSet;
import com.example.demo.ENums.Roles;
import com.mongodb.lang.NonNull;

@Document(collection = "UserRoles")
public class UserRoles implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;

	@Id
	private String id;

	@Indexed(unique = true)
	@NonNull
	private String userId;

	@Indexed
	@NonNull
	private Set<Roles> roles = new HashSet<>();

	public UserRoles(String userId, Set<Roles> roles) {
		super();
		this.userId = userId;
		this.roles = roles;
	}

	public UserRoles() {
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

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserRoles [id=" + id + ", userId=" + userId + ", roles=" + roles + "]";
	}

}
