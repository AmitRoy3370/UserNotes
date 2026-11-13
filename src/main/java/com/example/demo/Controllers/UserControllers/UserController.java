package com.example.demo.Controllers.UserControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTOFiles.JWTResponseDTO;
import com.example.demo.DTOFiles.UserPageResponseDTO;
import com.example.demo.DTOFiles.UserResponseDTO;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Services.UserServices.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	private ResponseEntity<?> errorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestPart("user") User user,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		try {
			JWTResponseDTO response = userService.register(user, file);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> logIn(String userName, String password) {
		try {
			JWTResponseDTO response = userService.logIn(userName, password);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<?> addUser(@AuthenticationPrincipal User authUser, @RequestPart("user") User user,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		try {
			User createdUser = userService.addUser(authUser.getId(), user, file);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @AuthenticationPrincipal User authUser,
			@RequestPart("user") User user, @RequestPart(value = "file", required = false) MultipartFile file) {
		try {
			User updatedUser = userService.updateUser(id, authUser.getId(), user, file);
			return ResponseEntity.ok(updatedUser);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		try {
			UserResponseDTO response = userService.findById(id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		try {
			List<UserResponseDTO> responses = userService.findAll();
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all/paginated")
	public ResponseEntity<?> findAllPaginated(Pageable page, HttpServletRequest request) {
		try {
			UserPageResponseDTO responses = userService.findAll(page, request);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/username/{userName}")
	public ResponseEntity<?> findByUserName(@PathVariable String userName) {
		try {
			UserResponseDTO response = userService.findByUserName(userName);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/fullName")
	public ResponseEntity<?> findByFullName(String fullName) {
		try {
			List<UserResponseDTO> responses = userService.findByFullNameContainingIgnoreCase(fullName);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/fullName/paginated")
	public ResponseEntity<?> findByFullNamePaginated(String fullName, Pageable page, HttpServletRequest request) {
		try {
			UserPageResponseDTO responses = userService.findByFullNameContainingIgnoreCase(fullName, page, request);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/profileImage")
	public ResponseEntity<?> findByProfileImage(String profileImage) {
		try {
			List<UserResponseDTO> responses = userService.findByProfileImageContainingIgnoreCase(profileImage);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/profileImage/paginated")
	public ResponseEntity<?> findByProfileImagePaginated(String profileImage, Pageable page,
			HttpServletRequest request) {
		try {
			UserPageResponseDTO responses = userService.findByProfileImageContainingIgnoreCase(profileImage, page,
					request);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id, @AuthenticationPrincipal User authUser) {
		try {
			boolean isDeleted = userService.deleteUser(id, authUser.getId());
			if (isDeleted) {
				return ResponseEntity.ok("User successfully deleted.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete user.");
			}
		} catch (Exception e) {
			return errorResponse(e);
		}
	}
}