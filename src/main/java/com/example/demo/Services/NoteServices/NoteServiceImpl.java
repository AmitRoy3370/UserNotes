package com.example.demo.Services.NoteServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.Configs.SupabaseProperties;
import com.example.demo.CyclicCleaner.Cleaner;
import com.example.demo.DTOFiles.NotePageResponseDTO;
import com.example.demo.DTOFiles.NoteResponseDTO;
import com.example.demo.DTOFiles.UserPageResponseDTO;
import com.example.demo.DTOFiles.UserResponseDTO;
import com.example.demo.ENums.Roles;
import com.example.demo.Models.NoteModels.Notes;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserRoles;
import com.example.demo.Repositories.NoteRepositories.NoteRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserRolesRepository;
import com.example.demo.Services.AttachmentsServices.SupabaseStorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private UserRolesRepository rolesRepository;

	@Autowired
	private Cleaner cleaner;

	@Autowired
	private SupabaseStorageService supabaseStorageService;

	@Autowired
	private SupabaseProperties supabaseProperties;

	private static final String cacheValue = "Notes";

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public Notes adNotes(Notes notes, String userId, MultipartFile files[]) {

		if (userId == null || notes == null || !notes.getUserId().equals(userId) || notes.getPostType() == null
				|| notes.getNoteContent() == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}

		if(files != null) {

            System.out.println("total files send :- " + files.length);

		} else {

		    System.out.println("No files send at here...");

		}

		List<String> attachmentsUrl = new ArrayList<>();

		if (notes.getNoteAttachmentsUrls() != null) {

			for (String i : notes.getNoteAttachmentsUrls()) {

				try {

					if (supabaseStorageService.isValidSupabaseUrl(i)) {

						attachmentsUrl.add(i);

					} else {

					}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

			}

		}

		if (files != null) {

			for (MultipartFile file : files) {

				try {

					if (file == null || file.isEmpty()) {
						throw new Exception();
					}
					String contentType = file.getContentType();
					System.out.println("Content type of file :- " + contentType);
					//if (contentType != null && contentType.startsWith("image/")) {
						String profileImageUrl = uploadPostAttachment(file, user.getUserName());
						attachmentsUrl.add(profileImageUrl);
					//}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

			}

		}

		notes.setNoteAttachmentsUrls(attachmentsUrl);

		notes = noteRepository.save(notes);

		if (notes == null) {

			throw new ArithmeticException("Data not saved...");

		}

		return notes;

	}

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public Notes updateNotes(Notes notes, String userId, String id, MultipartFile files[]) {

		if (id == null || userId == null || notes == null || !notes.getUserId().equals(userId)
				|| notes.getPostType() == null || notes.getNoteContent() == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}

		try {

			Notes _notes = noteRepository.findById(id).get();

			if (_notes == null) {

				throw new Exception();

			}

			if (_notes.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}

		List<String> attachmentsUrl = new ArrayList<>();

		if (notes.getNoteAttachmentsUrls() != null) {

			for (String i : notes.getNoteAttachmentsUrls()) {

				try {

					if (supabaseStorageService.isValidSupabaseUrl(i)) {

						attachmentsUrl.add(i);

					} else {

					}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

			}

		}

		try {

			Notes _notes = noteRepository.findById(id).get();

			if (_notes == null) {

				throw new Exception();

			}

			if (!_notes.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}

		if (files != null) {

			for (MultipartFile file : files) {

				try {

					if (file == null || file.isEmpty()) {
						throw new Exception();
					}
					String contentType = file.getContentType();
					System.out.println("Content type of file :- " + contentType);
					//if (contentType != null && contentType.startsWith("image/")) {
						String profileImageUrl = uploadPostAttachment(file, user.getUserName());
						attachmentsUrl.add(profileImageUrl);
					//}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

			}

		}

		notes.setNoteAttachmentsUrls(attachmentsUrl);

		notes.setId(id);

		notes = noteRepository.save(notes);

		if (notes == null) {

			throw new ArithmeticException("Data not saved...");

		}

		return notes;

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public NoteResponseDTO findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Notes notes = noteRepository.findById(id).get();

			if (notes == null) {

				throw new Exception();

			}

			return getItemResponse(notes);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<NoteResponseDTO> findAll() {

		try {

			List<Notes> list = noteRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllPaginated_' + #page")
	public NotePageResponseDTO findAll(Pageable page, HttpServletRequest http) {

		try {

			Page<Notes> list = noteRepository.findAll(page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public List<NoteResponseDTO> findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByUserId(userId);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserIdPaginated_' + #userId + '_' + #page")
	public NotePageResponseDTO findByUserId(String userId, Pageable page, HttpServletRequest http) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<Notes> list = noteRepository.findByUserId(userId, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersId_' + #usersId")
	public List<NoteResponseDTO> findByUserIdIn(List<String> usersId) {

		if (usersId == null || usersId.isEmpty()) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByUserIdIn(usersId);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPosyType_' + #postType")
	public List<NoteResponseDTO> findByPostTypeContainingIgnoreCase(String postType) {

		if (postType == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByPostTypeContainingIgnoreCase(postType);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPostTypePrefixPagnated_' + #postType + '_' + #page")
	public NotePageResponseDTO findByPostTypeContainingIgnoreCase(String postType, Pageable page,
			HttpServletRequest http) {

		if (postType == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<Notes> list = noteRepository.findByPostTypeContainingIgnoreCase(postType, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByNoteContent_' + #noteContent")
	public List<NoteResponseDTO> findByNoteContentContainingIgnoreCase(String noteContent) {

		if (noteContent == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByNoteContentContainingIgnoreCase(noteContent);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByNotePrefixPaginated_' + #noteContent + '_' + #page")
	public NotePageResponseDTO findByNoteContentContainingIgnoreCase(String noteContent, Pageable page,
			HttpServletRequest http) {

		if (noteContent == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<Notes> list = noteRepository.findByNoteContentContainingIgnoreCase(noteContent, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPostedDateAfter_' + #postedDate")
	public List<NoteResponseDTO> findByPostedDateAfter(Instant postedDate) {

		if (postedDate == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByPostedDateAfter(postedDate);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPostedDateBefore_' + #postedDate")
	public List<NoteResponseDTO> findByPostedDateBefore(Instant postedDate) {

		if (postedDate == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByPostedDateBefore(postedDate);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPostedAfterPaginated_' + #postedDate + '_' + #page")
	public NotePageResponseDTO findByPostedDateAfter(Instant postedDate, Pageable page, HttpServletRequest http) {

		if (postedDate == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<Notes> list = noteRepository.findByPostedDateAfter(postedDate, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPostedBeforePaginated_' + #postedDate + '_' + #page")
	public NotePageResponseDTO findByPostedDateBefore(Instant postedDate, Pageable page, HttpServletRequest http) {

		if (postedDate == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<Notes> list = noteRepository.findByPostedDateBefore(postedDate, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtAfter_' + #updatedAt")
	public List<NoteResponseDTO> findByUpdatedAtAfter(Instant updatedAt) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByUpdatedAtAfter(updatedAt);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtBefore_' + #updatedAt")
	public List<NoteResponseDTO> findByUpdatedAtBefore(Instant updatedAt) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<Notes> list = noteRepository.findByUpdatedAtBefore(updatedAt);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getItemResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtAfterPaginated_' + #updatedAt + '_' + #page")
	public NotePageResponseDTO findByUpdatedAtAfter(Instant updatedAt, Pageable page, HttpServletRequest http) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<Notes> list = noteRepository.findByUpdatedAtAfter(updatedAt, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUpdatedAtBeforePaginated_' + #updatedAt + '_' + #page")
	public NotePageResponseDTO findByUpdatedAtBefore(Instant updatedAt, Pageable page, HttpServletRequest http) {

		if (updatedAt == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<Notes> list = noteRepository.findByUpdatedAtBefore(updatedAt, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getPageResponse(getItemResponse(list.getContent()), http, list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}
	}

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public boolean removeNote(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}

		boolean ownPost = false;

		try {

			Notes _notes = noteRepository.findById(id).get();

			if (_notes == null) {

				throw new Exception();

			}

			if (_notes.getUserId().equals(userId)) {

				ownPost = true;

				return cleaner.removeNotes(id);

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such notes find at here...");

		}

		if (!ownPost) {

			try {

				UserRoles roles = rolesRepository.findByUserId(userId);

				if (roles == null) {

					throw new Exception();

				}

				if (roles.getRoles().contains(Roles.ADMIN)) {

					return cleaner.removeNotes(id);

				} else {

					throw new Exception();

				}

			} catch (Exception e) {

				throw new ArithmeticException("Only admin can remove all one post...");

			}

		}

		return false;

	}

	private String uploadPostAttachment(MultipartFile file, String userName) {
		try {
			String bucketName = supabaseProperties.getBucket().getItem();
			String fileName = userName + "_" + file.getName() + "_" + Instant.now();

			System.out.println("bucket name :- " + bucketName);
			System.out.println("file name :- " + fileName);

			byte[] fileBytes = file.getBytes();

			System.out.println("file byted :- " + fileBytes);

			String fileUrl = supabaseStorageService.uploadFile(bucketName, fileName, fileBytes, file.getContentType());

			System.out.println("file url :- " + fileUrl);

			return fileUrl;
		} catch (Exception e) {

			System.out.println("file upload error :- " + e.getMessage());

			throw new RuntimeException("Failed to upload profile image: " + e.getMessage());
		}
	}

	private ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

	private NoteResponseDTO getItemResponse(Notes note) {

		List<Notes> notes = new ArrayList<>();

		notes.add(note);

		return getItemResponse(notes).get(0);

	}

	private List<NoteResponseDTO> getItemResponse(List<Notes> list) {

		List<NoteResponseDTO> responses = new ArrayList<>();

		CompletableFuture<List<String>> usersIdListFuture = CompletableFuture.supplyAsync(
				() -> list.stream().distinct().map(Notes::getUserId).collect(Collectors.toList()), executor);

		CompletableFuture<Map<String, User>> userMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return userRepository.findAllById(usersId).stream()
					.collect(Collectors.toMap(User::getId, Function.identity()));

		}, executor);

		CompletableFuture.allOf(usersIdListFuture, userMapFuture).join();

		Map<String, User> userMap = userMapFuture.join();

		for (Notes notes : list) {

			try {

				NoteResponseDTO response = new NoteResponseDTO();

				response.setId(notes.getId());
				response.setUpdatedAt(notes.getUpdatedAt());
				response.setNoteContent(notes.getNoteContent());
				response.setNoteAttachmentsUrls(notes.getNoteAttachmentsUrls());
				response.setPostedDate(notes.getPostedDate());
				response.setPostType(notes.getPostType());
				response.setUserId(notes.getUserId());

				try {

					response.setUserName(userMap.get(notes.getUserId()).getUserName());
					response.setFullName(userMap.get(notes.getUserId()).getFullName());
					response.setProfileImage(userMap.get(notes.getUserId()).getProfileImage());

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				responses.add(response);

			} catch (Exception e) {

				System.out.println(e.getMessage());

			}

		}

		return responses;

	}

	private NotePageResponseDTO getPageResponse(List<NoteResponseDTO> responses, HttpServletRequest http,
			Page<Notes> usersPage) {

		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page")
				.replaceQueryParam("size").toUriString();

		String separator = baseUrl.contains("?") ? "&" : "?";

		String nextPage = usersPage.hasNext()
				? baseUrl + separator + "page=" + (usersPage.getNumber() + 1) + "&size=" + usersPage.getSize()
				: null;

		String previousPage = usersPage.hasPrevious()
				? baseUrl + separator + "page=" + (usersPage.getNumber() - 1) + "&size=" + usersPage.getSize()
				: null;

		return new NotePageResponseDTO(responses, usersPage.getNumber(), usersPage.getSize(),
				usersPage.getTotalElements(), nextPage, previousPage);

	}

}
