package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserModels.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	@Query("{ 'userName': ?0 }")
	User findByUserName(String userName);
	
	Page<User> findAll(Pageable page);

	@Query("{ 'fullName': { $regex: ?0, $options: 'i' } }")
	List<User> findByFullNameContainingIgnoreCase(String fullName);

	@Query("{ 'fullName': { $regex: ?0, $options: 'i' } }")
	Page<User> findByFullNameContainingIgnoreCase(String fullName, Pageable page);

	@Query("{ 'profileImage': { $regex: ?0, $options: 'i' } }")
	List<User> findByProfileImageContainingIgnoreCase(String profileImage);

	@Query("{ 'profileImage': { $regex: ?0, $options: 'i' } }")
	Page<User> findByProfileImageContainingIgnoreCase(String profileImage, Pageable page);

}