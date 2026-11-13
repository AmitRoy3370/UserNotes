package com.example.demo.Services.UserServices;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.ENums.Roles;
import com.example.demo.Models.UserModels.UserRoles;

public interface UserRolesService {
	
	public UserRoles addRole(UserRoles roles, String userId);
	public UserRoles updateRole(UserRoles roles, String userId, String id);
	
	public UserRoles findById(String id);
	public List<UserRoles> findAll();
    public Page<UserRoles> findAll(Pageable page);
	public UserRoles findByUserId(String userId);
	public List<UserRoles> findByUserIdIn(List<String> usersId);
	public List<UserRoles> findByRolesContainingIgnoreCase(Roles roles);
	public Page<UserRoles> findByRolesContainingIgnoreCase(Roles roles, Pageable page);

	public boolean deleteRoles(String id, String userId);

}
