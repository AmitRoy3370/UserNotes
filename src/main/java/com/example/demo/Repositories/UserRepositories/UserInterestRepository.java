package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserModels.UserInterest;

@Repository
public interface UserInterestRepository extends MongoRepository<UserInterest, String> {

	Page<UserInterest> findAll(Pageable page);
	
	@Query("{ 'userId': ?0 }")
	UserInterest findByUserId(String userId);

	@Query("{ 'userId': { $in: ?0 } }")
	List<UserInterest> findByUserIdIn(List<String> usersId);

	@Query("{ 'interests': { $regex: ?0, $options: 'i' } }")
	List<UserInterest> findByInterestsContainingIgnoreCase(String interests);

	@Query("{ 'interests': { $regex: ?0, $options: 'i' } }")
	Page<UserInterest> findByInterestsContainingIgnoreCase(String interests, Pageable page);

}