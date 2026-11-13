package com.example.demo.Controllers.UserControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ENums.Gender;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Services.UserServices.UserGenderService;

@RestController
@RequestMapping("/api/genders")
public class UserGenderController {

	@Autowired
	private UserGenderService genderService;

	private ResponseEntity<?> errorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@PostMapping("/add")
	public ResponseEntity<?> addGender(@AuthenticationPrincipal User authUser, @RequestBody UserGender gender) {
		try {
			String userId = authUser.getId();
			UserGender savedGender = genderService.addGender(gender, userId);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedGender);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateGender(@PathVariable String id, @AuthenticationPrincipal User authUser,
			@RequestBody UserGender gender) {
		try {
			String userId = authUser.getId();
			UserGender updatedGender = genderService.updateGender(gender, userId, id);
			return ResponseEntity.ok(updatedGender);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		try {
			UserGender gender = genderService.findById(id);
			return ResponseEntity.ok(gender);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		try {
			List<UserGender> genders = genderService.findAll();
			return ResponseEntity.ok(genders);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all/paginated")
	public ResponseEntity<?> findAllPaginated(Pageable page) {
		try {
			Page<UserGender> genders = genderService.findAll(page);
			return ResponseEntity.ok(genders);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findByUserId(@PathVariable String userId) {
		try {
			UserGender gender = genderService.findByUserId(userId);
			return ResponseEntity.ok(gender);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PostMapping("/users/in")
	public ResponseEntity<?> findByUserIdIn(@RequestBody List<String> usersId) {
		try {
			List<UserGender> genders = genderService.findByUserIdIn(usersId);
			return ResponseEntity.ok(genders);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/gender")
	public ResponseEntity<?> findByGender(Gender gender) {
		try {
			List<UserGender> genders = genderService.findByGender(gender);
			return ResponseEntity.ok(genders);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/gender/paginated")
	public ResponseEntity<?> findByGenderPaginated(Gender gender, Pageable page) {
		try {
			Page<UserGender> genders = genderService.findByGender(gender, page);
			return ResponseEntity.ok(genders);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteGender(@PathVariable String id, @AuthenticationPrincipal User authUser) {
		try {
			String userId = authUser.getId();
			boolean isDeleted = genderService.deleteGender(id, userId);
			if (isDeleted) {
				return ResponseEntity.ok("Gender successfully deleted.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete gender.");
			}
		} catch (Exception e) {
			return errorResponse(e);
		}
	}
}