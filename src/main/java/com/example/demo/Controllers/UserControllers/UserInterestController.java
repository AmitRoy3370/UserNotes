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

import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserInterest;
import com.example.demo.Services.UserServices.UserInterestService;

@RestController
@RequestMapping("/api/interests")
public class UserInterestController {

	@Autowired
	private UserInterestService interestService;

	private ResponseEntity<?> errorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@PostMapping("/add")
	public ResponseEntity<?> addInterest(@AuthenticationPrincipal User authUser, @RequestBody UserInterest interest) {
		try {
			String userId = authUser.getId();
			UserInterest savedInterest = interestService.adInterest(interest, userId);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedInterest);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateInterest(@PathVariable String id, @AuthenticationPrincipal User authUser,
			@RequestBody UserInterest interest) {
		try {
			String userId = authUser.getId();
			UserInterest updatedInterest = interestService.updateInterest(interest, userId, id);
			return ResponseEntity.ok(updatedInterest);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		try {
			UserInterest interest = interestService.findById(id);
			return ResponseEntity.ok(interest);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		try {
			List<UserInterest> interests = interestService.findAll();
			return ResponseEntity.ok(interests);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all/paginated")
	public ResponseEntity<?> findAllPaginated(Pageable page) {
		try {
			Page<UserInterest> interests = interestService.findAll(page);
			return ResponseEntity.ok(interests);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findByUserId(@PathVariable String userId) {
		try {
			UserInterest interest = interestService.findByUserId(userId);
			return ResponseEntity.ok(interest);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PostMapping("/users/in") // Using POST because we are sending a list in the body
	public ResponseEntity<?> findByUserIdIn(@RequestBody List<String> usersId) {
		try {
			List<UserInterest> interests = interestService.findByUserIdIn(usersId);
			return ResponseEntity.ok(interests);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> findByInterestsContainingIgnoreCase(String interests) {
		try {
			List<UserInterest> interestList = interestService.findByInterestsContainingIgnoreCase(interests);
			return ResponseEntity.ok(interestList);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/paginated")
	public ResponseEntity<?> findByInterestsContainingIgnoreCasePaginated(String interests, Pageable page) {
		try {
			Page<UserInterest> interestPage = interestService.findByInterestsContainingIgnoreCase(interests, page);
			return ResponseEntity.ok(interestPage);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteInterest(@PathVariable String id, @AuthenticationPrincipal User authUser) {
		try {
			String userId = authUser.getId();
			boolean isDeleted = interestService.deleteInterest(id, userId);
			if (isDeleted) {
				return ResponseEntity.ok("User interest successfully deleted.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete user interest.");
			}
		} catch (Exception e) {
			return errorResponse(e);
		}
	}
}