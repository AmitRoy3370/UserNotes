package com.example.demo.Controllers.NoteControllers;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTOFiles.NotePageResponseDTO;
import com.example.demo.DTOFiles.NoteResponseDTO;
import com.example.demo.Models.NoteModels.Notes;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Services.NoteServices.NoteService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

	@Autowired
	private NoteService noteService;

	private ResponseEntity<?> errorResponse(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addNotes(@AuthenticationPrincipal User authUser, @RequestPart("notes") Notes notes,
			@RequestPart(value = "files", required = false) MultipartFile[] files) {
		try {
			String userId = authUser.getId();
			Notes savedNote = noteService.adNotes(notes, userId, files);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateNotes(@PathVariable String id, @AuthenticationPrincipal User authUser,
			@RequestPart("notes") Notes notes, @RequestPart(value = "files", required = false) MultipartFile[] files) {
		try {
			String userId = authUser.getId();
			Notes updatedNote = noteService.updateNotes(notes, userId, id, files);
			return ResponseEntity.ok(updatedNote);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {
		try {
			NoteResponseDTO response = noteService.findById(id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		try {
			List<NoteResponseDTO> responses = noteService.findAll();
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/all/paginated")
	public ResponseEntity<?> findAllPaginated(Pageable page, HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findAll(page, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findByUserId(@PathVariable String userId) {
		try {
			List<NoteResponseDTO> responses = noteService.findByUserId(userId);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/user/{userId}/paginated")
	public ResponseEntity<?> findByUserIdPaginated(@PathVariable String userId, Pageable page,
			HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findByUserId(userId, page, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@PostMapping("/users/in")
	public ResponseEntity<?> findByUserIdIn(@org.springframework.web.bind.annotation.RequestBody List<String> usersId) {
		try {
			List<NoteResponseDTO> responses = noteService.findByUserIdIn(usersId);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/postType")
	public ResponseEntity<?> findByPostType(String postType) {
		try {
			List<NoteResponseDTO> responses = noteService.findByPostTypeContainingIgnoreCase(postType);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/postType/paginated")
	public ResponseEntity<?> findByPostTypePaginated(String postType, Pageable page, HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findByPostTypeContainingIgnoreCase(postType, page, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/content")
	public ResponseEntity<?> findByNoteContent(String noteContent) {
		try {
			List<NoteResponseDTO> responses = noteService.findByNoteContentContainingIgnoreCase(noteContent);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/content/paginated")
	public ResponseEntity<?> findByNoteContentPaginated(String noteContent, Pageable page, HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findByNoteContentContainingIgnoreCase(noteContent, page,
					request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/postedDate/after")
	public ResponseEntity<?> findByPostedDateAfter(@RequestParam Instant postedDate) {
		try {
			List<NoteResponseDTO> responses = noteService.findByPostedDateAfter(postedDate);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/postedDate/before")
	public ResponseEntity<?> findByPostedDateBefore(@RequestParam Instant postedDate) {
		try {
			List<NoteResponseDTO> responses = noteService.findByPostedDateBefore(postedDate);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/postedDate/after/paginated")
	public ResponseEntity<?> findByPostedDateAfterPaginated(@RequestParam Instant postedDate, Pageable page,
			HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findByPostedDateAfter(postedDate, page, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/postedDate/before/paginated")
	public ResponseEntity<?> findByPostedDateBeforePaginated(@RequestParam Instant postedDate, Pageable page,
			HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findByPostedDateBefore(postedDate, page, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/updatedAt/after")
	public ResponseEntity<?> findByUpdatedAtAfter(@RequestParam Instant updatedAt) {
		try {
			List<NoteResponseDTO> responses = noteService.findByUpdatedAtAfter(updatedAt);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/updatedAt/before")
	public ResponseEntity<?> findByUpdatedAtBefore(@RequestParam Instant updatedAt) {
		try {
			List<NoteResponseDTO> responses = noteService.findByUpdatedAtBefore(updatedAt);
			return ResponseEntity.ok(responses);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/updatedAt/after/paginated")
	public ResponseEntity<?> findByUpdatedAtAfterPaginated(@RequestParam Instant updatedAt, Pageable page,
			HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findByUpdatedAtAfter(updatedAt, page, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@GetMapping("/search/updatedAt/before/paginated")
	public ResponseEntity<?> findByUpdatedAtBeforePaginated(@RequestParam Instant updatedAt, Pageable page,
			HttpServletRequest request) {
		try {
			NotePageResponseDTO response = noteService.findByUpdatedAtBefore(updatedAt, page, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return errorResponse(e);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> removeNote(@PathVariable String id, @AuthenticationPrincipal User authUser) {
		try {
			String userId = authUser.getId();
			boolean isDeleted = noteService.removeNote(id, userId);
			if (isDeleted) {
				return ResponseEntity.ok("Note successfully deleted.");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete note.");
			}
		} catch (Exception e) {
			return errorResponse(e);
		}
	}
}