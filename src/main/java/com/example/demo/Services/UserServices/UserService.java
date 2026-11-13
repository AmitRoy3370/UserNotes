package com.example.demo.Services.UserServices;

import com.example.demo.DTOFiles.JWTResponseDTO;
import com.example.demo.DTOFiles.UserPageResponseDTO;
import com.example.demo.Models.UserModels.User;

import jakarta.servlet.http.HttpServletRequest;

import com.example.demo.DTOFiles.UserResponseDTO;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	
	public JWTResponseDTO register(User user, MultipartFile file);
	public JWTResponseDTO logIn(String userName, String password);
	
	public User addUser(String userId, User user, MultipartFile file);
	public User updateUser(String id, String userId, User user, MultipartFile file);
	
	public UserResponseDTO findById(String id);
	public List<UserResponseDTO> findAll();
	public UserPageResponseDTO findAll(Pageable page, HttpServletRequest request);
    public UserResponseDTO findByUserName(String userName);
	
	public List<UserResponseDTO> findByFullNameContainingIgnoreCase(String fullName);
	public UserPageResponseDTO findByFullNameContainingIgnoreCase(String fullName, Pageable page, HttpServletRequest request);
	
	public List<UserResponseDTO> findByProfileImageContainingIgnoreCase(String profileImage);
	public UserPageResponseDTO findByProfileImageContainingIgnoreCase(String profileImage, Pageable page, HttpServletRequest request);
	
	public boolean deleteUser(String id, String userId);

}
