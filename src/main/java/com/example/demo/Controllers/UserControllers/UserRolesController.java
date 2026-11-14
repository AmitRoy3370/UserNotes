package com.example.demo.Controllers.UserControllers;

import java.util.List;
import java.util.Set;

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

import com.example.demo.ENums.Roles;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserRoles;
import com.example.demo.Services.UserServices.UserRolesService;

@RestController
@RequestMapping("/api/roles")
public class UserRolesController {

	@Autowired
	private UserRolesService rolesService;

	private ResponseEntity<?> errorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@PostMapping("/add")
	public ResponseEntity<?> addRole(@AuthenticationPrincipal User authUser, @RequestBody UserRoles roles) {
		try {
			String userId = authUser.getId();
			UserRoles savedRoles = rolesService.addRole(roles, userId);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedRoles);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateRole(@PathVariable String id, @AuthenticationPrincipal User authUser,
			@RequestBody UserRoles roles) {
		try {
			String userId = authUser.getId();
			UserRoles updatedRoles = rolesService.updateRole(roles, userId, id);
			return ResponseEntity.ok(updatedRoles);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		try {
			UserRoles roles = rolesService.findById(id);
			return ResponseEntity.ok(roles);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		try {
			List<UserRoles> rolesList = rolesService.findAll();
			return ResponseEntity.ok(rolesList);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all/paginated")
	public ResponseEntity<?> findAllPaginated(Pageable page) {
		try {
			Page<UserRoles> rolesPage = rolesService.findAll(page);
			return ResponseEntity.ok(rolesPage);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findByUserId(@PathVariable String userId) {
		try {
			UserRoles roles = rolesService.findByUserId(userId);
			return ResponseEntity.ok(roles);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}}

	@PostMapping("/users/in")
	public ResponseEntity<?> findByUserIdIn(@RequestBody List<String> usersId) {
		try {
			List<UserRoles> rolesList = rolesService.findByUserIdIn(usersId);
			return ResponseEntity.ok(rolesList);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/role")
	public ResponseEntity<?> findByRoles(Roles roles) {
		try {
			List<UserRoles> rolesList = rolesService.findByRolesContainingIgnoreCase(roles);
			return ResponseEntity.ok(rolesList);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/role/paginated")
	public ResponseEntity<?> findByRolesPaginated(Roles roles, Pageable page) {
		try {
			Page<UserRoles> rolesPage = rolesService.findByRolesContainingIgnoreCase(roles, page);
			return ResponseEntity.ok(rolesPage);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteRoles(@PathVariable String id, @AuthenticationPrincipal User authUser) {
		try {
			String userId = authUser.getId();
			boolean isDeleted = rolesService.deleteRoles(id, userId);
			if (isDeleted) {
				return ResponseEntity.ok("User roles successfully deleted.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete user roles.");
			}
		} catch (Exception e) {
			return errorResponse(e);
		}
	}
}
