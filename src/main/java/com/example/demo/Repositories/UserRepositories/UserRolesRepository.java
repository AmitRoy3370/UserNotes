package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.ENums.Roles;
import com.example.demo.Models.UserModels.UserRoles;

@Repository
public interface UserRolesRepository extends MongoRepository<UserRoles, String> {

	Page<UserRoles> findAll(Pageable page);
	
	@Query("{ 'userId': ?0 }")
	UserRoles findByUserId(String userId);

	@Query("{ 'userId': { $in: ?0 } }")
	List<UserRoles> findByUserIdIn(List<String> usersId);

	@Query("{ 'roles': { $regex: { $toString: ?0 }, $options: 'i' } }")
	List<UserRoles> findByRolesContainingIgnoreCase(Roles roles);

	@Query("{ 'roles': { $regex: { $toString: ?0 }, $options: 'i' } }")
	Page<UserRoles> findByRolesContainingIgnoreCase(Roles roles, Pageable page);

}