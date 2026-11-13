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
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Services.UserServices.UserContactService;

@RestController
@RequestMapping("/api/contacts")
public class UserContactController {

	@Autowired
	private UserContactService contactService;

	private ResponseEntity<?> errorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@PostMapping("/add")
	public ResponseEntity<?> addContact(@AuthenticationPrincipal User authUser, @RequestBody UserContact contact) {
		try {

			String userId = authUser.getId();
			UserContact savedContact = contactService.addContact(contact, userId);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateContact(@PathVariable String id, @AuthenticationPrincipal User authUser,
			@RequestBody UserContact contact) {
		try {
			String userId = authUser.getId();
			UserContact updatedContact = contactService.updateContact(contact, userId, id);
			return ResponseEntity.ok(updatedContact);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		try {
			UserContact contact = contactService.findById(id);
			return ResponseEntity.ok(contact);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		try {
			List<UserContact> contacts = contactService.findAll();
			return ResponseEntity.ok(contacts);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all/paginated")
	public ResponseEntity<?> findAllPaginated(Pageable page) {
		try {
			Page<UserContact> contacts = contactService.findAll(page);
			return ResponseEntity.ok(contacts);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findByUserId(@PathVariable String userId) {
		try {
			UserContact contact = contactService.findByUserId(userId);
			return ResponseEntity.ok(contact);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PostMapping("/users/in")
	public ResponseEntity<?> findByUserIdIn(@RequestBody List<String> usersId) {
		try {
			List<UserContact> contacts = contactService.findByUserIdIn(usersId);
			return ResponseEntity.ok(contacts);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<?> findByEmail(@PathVariable String email) {
		try {
			UserContact contact = contactService.findByEmail(email);
			return ResponseEntity.ok(contact);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/phone/{phoneNumber}")
	public ResponseEntity<?> findByPhoneNumber(@PathVariable String phoneNumber) {
		try {
			UserContact contact = contactService.findByPhoneNumber(phoneNumber);
			return ResponseEntity.ok(contact);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/phone")
	public ResponseEntity<?> searchByPhone(String phoneNumber) {
		try {
			List<UserContact> contacts = contactService.findByPhoneNumberContainingIgnoreCase(phoneNumber);
			return ResponseEntity.ok(contacts);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/phone/paginated")
	public ResponseEntity<?> searchByPhonePaginated(String phoneNumber, Pageable page) {
		try {
			Page<UserContact> contacts = contactService.findByPhoneNumberContainingIgnoreCase(phoneNumber, page);
			return ResponseEntity.ok(contacts);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/email")
	public ResponseEntity<?> searchByEmail(String email) {
		try {
			List<UserContact> contacts = contactService.findByEmailContainingIgnoreCase(email);
			return ResponseEntity.ok(contacts);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/email/paginated")
	public ResponseEntity<?> searchByEmailPaginated(String email, Pageable page) {
		try {
			Page<UserContact> contacts = contactService.findByEmailContainingIgnoreCase(email, page);
			return ResponseEntity.ok(contacts);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> removeContact(@PathVariable String id, @AuthenticationPrincipal User authUser) {
		try {
			String userId = authUser.getId();
			boolean isDeleted = contactService.removeContact(id, userId);
			if (isDeleted) {
				return ResponseEntity.ok("Contact successfully removed.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not remove contact.");
			}
		} catch (Exception e) {
			return errorResponse(e);
		}
	}
}