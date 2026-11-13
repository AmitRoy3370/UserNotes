package com.example.demo.Controllers.UserControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTOFiles.JWTResponseDTO;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Services.UserServices.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	private ResponseEntity<?> errorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	// Changed from @RequestPart to @RequestParam
	@PostMapping("/register")
	public ResponseEntity<?> register(
			@RequestParam("user") String userJson,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			// Manually map the JSON String to the User object
			User user = objectMapper.readValue(userJson, User.class);

			JWTResponseDTO response = userService.register(user, file);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> logIn(@RequestParam String userName, @RequestParam String password) {
		try {
			JWTResponseDTO response = userService.logIn(userName, password);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}
}
