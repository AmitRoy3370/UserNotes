package com.example.demo.Services.UserServices;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.Models.UserModels.UserContact;

public interface UserContactService {

	public UserContact addContact(UserContact contact, String userId);

	public UserContact updateContact(UserContact contact, String userId, String id);

	public UserContact findById(String id);

	public List<UserContact> findAll();

	public Page<UserContact> findAll(Pageable page);

	UserContact findByUserId(String userId);

	List<UserContact> findByUserIdIn(List<String> usersId);

	UserContact findByEmail(String email);

	UserContact findByPhoneNumber(String phoneNumber);

	List<UserContact> findByPhoneNumberContainingIgnoreCase(String phoneNumber);

	Page<UserContact> findByPhoneNumberContainingIgnoreCase(String phoneNumber, Pageable page);

	List<UserContact> findByEmailContainingIgnoreCase(String email);

	Page<UserContact> findByEmailContainingIgnoreCase(String email, Pageable page);

	public boolean removeContact(String id, String userId);
	
}
