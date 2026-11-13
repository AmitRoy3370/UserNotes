package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserModels.UserContact;

@Repository
public interface UserContactRepository extends MongoRepository<UserContact, String> {

	Page<UserContact> findAll(Pageable page);
	
	@Query("{ 'userId': ?0 }")
	UserContact findByUserId(String userId);

	@Query("{ 'userId': { $in: ?0 } }")
	List<UserContact> findByUserIdIn(List<String> usersId);

	@Query("{ 'email': ?0 }")
	UserContact findByEmail(String email);

	@Query("{ 'phoneNumber': ?0 }")
	UserContact findByPhoneNumber(String phoneNumber);

	@Query("{ 'phoneNumber': { $regex: ?0, $options: 'i' } }")
	List<UserContact> findByPhoneNumberContainingIgnoreCase(String phoneNumber);

	@Query("{ 'phoneNumber': { $regex: ?0, $options: 'i' } }")
	Page<UserContact> findByPhoneNumberContainingIgnoreCase(String phoneNumber, Pageable page);

	@Query("{ 'email': { $regex: ?0, $options: 'i' } }")
	List<UserContact> findByEmailContainingIgnoreCase(String email);

	@Query("{ 'email': { $regex: ?0, $options: 'i' } }")
	Page<UserContact> findByEmailContainingIgnoreCase(String email, Pageable page);

}