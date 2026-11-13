package com.example.demo.Services.UserServices;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.ENums.Gender;
import com.example.demo.Models.UserModels.UserGender;

public interface UserGenderService {
	
	public UserGender addGender(UserGender gender, String userId);
	public UserGender updateGender(UserGender gender, String userId, String id);
	
	public UserGender findById(String id);
	public List<UserGender> findAll();
	public Page<UserGender> findAll(Pageable page);
	public UserGender findByUserId(String userId);
	public List<UserGender> findByUserIdIn(List<String> usersId);
	public List<UserGender> findByGender(Gender gender);
	public Page<UserGender> findByGender(Gender gender, Pageable page);

	public boolean deleteGender(String id, String userId);
	
}
