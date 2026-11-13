package com.example.demo.Services.UserServices;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.Models.UserModels.UserInterest;

public interface UserInterestService {
	
	public UserInterest adInterest(UserInterest interest, String userId);
	public UserInterest updateInterest(UserInterest interest, String userId, String id);
	
	public UserInterest findById(String id);
	public List<UserInterest> findAll();
    public Page<UserInterest> findAll(Pageable page);
	public UserInterest findByUserId(String userId);
	public List<UserInterest> findByUserIdIn(List<String> usersId);
	public List<UserInterest> findByInterestsContainingIgnoreCase(String interests);
	public Page<UserInterest> findByInterestsContainingIgnoreCase(String interests, Pageable page);

	public boolean deleteInterest(String id, String userId);

}
