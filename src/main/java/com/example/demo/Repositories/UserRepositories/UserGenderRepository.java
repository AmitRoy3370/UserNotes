package com.example.demo.Repositories.UserRepositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.ENums.Gender;
import com.example.demo.Models.UserModels.UserGender;

@Repository
public interface UserGenderRepository extends MongoRepository<UserGender, String> {

	Page<UserGender> findAll(Pageable page);
	
	@Query("{ 'userId': ?0 }")
	UserGender findByUserId(String userId);

	@Query("{ 'userId': { $in: ?0 } }")
	List<UserGender> findByUserIdIn(List<String> usersId);

	@Query("{ 'gender': ?0 }")
	List<UserGender> findByGender(Gender gender);

	@Query("{ 'gender': ?0 }")
	Page<UserGender> findByGender(Gender gender, Pageable page);

}