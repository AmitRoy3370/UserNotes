package com.example.demo.Services.UserServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.HashMap;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.Configs.SupabaseProperties;
import com.example.demo.CyclicCleaner.Cleaner;

import com.example.demo.DTOFiles.JWTResponseDTO;

import com.example.demo.DTOFiles.UserPageResponseDTO;
import com.example.demo.DTOFiles.UserResponseDTO;
import com.example.demo.ENums.Roles;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserInterest;
import com.example.demo.Models.UserModels.UserRoles;
import com.example.demo.Repositories.UserRepositories.UserContactRepository;
import com.example.demo.Repositories.UserRepositories.UserGenderRepository;
import com.example.demo.Repositories.UserRepositories.UserInterestRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserRolesRepository;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Services.AttachmentsServices.SupabaseStorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRolesRepository roleRepository;

	@Autowired
	private UserGenderRepository genderRepository;

	@Autowired
	private UserInterestRepository interestRepository;

	@Autowired
	private UserContactRepository contactRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private SupabaseStorageService supabaseStorageService;

	@Autowired
	private SupabaseProperties supabaseProperties;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private Cleaner cleaner;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private static final String cacheValue = "User";

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public JWTResponseDTO register(User user, MultipartFile file) {

		if (user == null) {

			throw new NullPointerException("False request...");

		}

		System.out.println(user.toString());

		if (user.getUserName() == null || user.getPassword() == null || user.getUserName() == null) {
			throw new NullPointerException("Have to put all the data perfectly at here...");
		}

		if (user.getProfileImage() != null) {

			if (supabaseStorageService.isValidSupabaseUrl(user.getProfileImage())) {

			} else {

				throw new ArithmeticException("False profileImage url...");

			}

		}

		try {
			User _user = userRepository.findByUserName(user.getUserName().trim());
			if (_user != null) {
				throw new ArithmeticException();
			}
		} catch (ArithmeticException e) {
			throw new ArithmeticException("This username is already exist at here...");
		} catch (Exception e) {
		}

		System.out.println("before encrypted :- " + user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println("after encrypted :- " + user.getPassword());

		try {
			if (file == null || file.isEmpty()) {
				throw new Exception();
			}
			String contentType = file.getContentType();
			System.out.println("Content type of file :- " + contentType);
			if (contentType != null && contentType.startsWith("image/")) {
				String profileImageUrl = uploadProfileImage(file, user.getUserName());
				user.setProfileImage(profileImageUrl);
			} else {
				throw new ArithmeticException();
			}
		} catch (ArithmeticException e) {
			throw new ArithmeticException("Profile image is image only....");
		} catch (Exception e) {
		}

		user = userRepository.save(user);

		if (user == null) {
			throw new ArithmeticException("Registration process is not complete....");
		}

		String token = jwtUtil.generateToken(user.getId(), user.getUserName());
		return new JWTResponseDTO(user.getId(), token);
	}

	@Override
	@Cacheable(value = cacheValue, key = "'logIn_' + #userName + '_' + #password")
	public JWTResponseDTO logIn(String userName, String password) {
		if (userName == null || password == null) {
			throw new NullPointerException("False request...");
		}

		try {
			User user = userRepository.findByUserName(userName);
			if (user == null) {
				throw new Exception();
			}
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new Exception();
			}

			String token = jwtUtil.generateToken(user.getId(), user.getUserName());
			return new JWTResponseDTO(user.getId(), token);

		} catch (Exception e) {
			throw new ArithmeticException("Invalid credential...");
		}
	}

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public User addUser(String userId, User user, MultipartFile file) {

		if (user == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User _user = userRepository.findById(userId).get();

			if (_user == null) {

				throw new Exception();

			}

			UserRoles roles = roleRepository.findByUserId(_user.getId());

			if (roles == null) {

				throw new Exception();

			}

			if (!roles.getRoles().contains(Roles.ADMIN)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Only admin can add user...");

		}

		if (user.getUserName() == null || user.getPassword() == null || user.getUserName() == null) {
			throw new NullPointerException("Have to put all the data perfectly at here...");
		}

		if (user.getProfileImage() != null) {

			if (supabaseStorageService.isValidSupabaseUrl(user.getProfileImage())) {

			} else {

				throw new ArithmeticException("False profileImage url...");

			}

		}

		try {
			User _user = userRepository.findByUserName(user.getUserName().trim());
			if (_user != null) {
				throw new ArithmeticException();
			}
		} catch (ArithmeticException e) {
			throw new ArithmeticException("This username is already exist at here...");
		} catch (Exception e) {
		}

		System.out.println("before encrypted :- " + user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println("after encrypted :- " + user.getPassword());

		try {
			if (file == null || file.isEmpty()) {
				throw new Exception();
			}
			String contentType = file.getContentType();
			System.out.println("Content type of file :- " + contentType);
			if (contentType != null && contentType.startsWith("image/")) {
				String profileImageUrl = uploadProfileImage(file, user.getUserName());
				user.setProfileImage(profileImageUrl);
			} else {
				throw new ArithmeticException();
			}
		} catch (ArithmeticException e) {
			throw new ArithmeticException("Profile image is image only....");
		} catch (Exception e) {
		}

		user = userRepository.save(user);

		if (user == null) {
			throw new ArithmeticException("Registration process is not complete....");
		}

		return user;

	}

	@Override
	@CacheEvict(value = cacheValue, allEntries = true)
	public User updateUser(String id, String userId, User user, MultipartFile file) {

		if (user == null || id == null || userId == null || user.getUserName() == null || user.getPassword() == null
				|| user.getUserName() == null) {
			throw new NullPointerException("False request...");
		}

		System.out.println("given id :- " + id + ", sender user id :- " + userId);

		try {
			User _user = userRepository.findById(id).get();
			if (_user == null) {
				throw new Exception();
			}
			if (!_user.getId().equals(userId)) {

				UserRoles roles = roleRepository.findByUserId(userId);

				if (roles == null || !roles.getRoles().contains(Roles.ADMIN)) {

					throw new Exception();

				} else {

				}

			}
		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}

		try {
			User _user = userRepository.findByUserName(user.getUserName());
			if (_user != null) {
				if (!_user.getId().equals(userId)) {
					throw new ArithmeticException();
				}
			}
		} catch (ArithmeticException e) {
			throw new ArithmeticException("Same userName already exist...");
		} catch (Exception e) {
		}

		System.out.println("before encrypted :- " + user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println("after encrypted :- " + user.getPassword());

		if (user.getProfileImage() != null) {

			if (supabaseStorageService.isValidSupabaseUrl(user.getProfileImage())) {

			} else {

				throw new ArithmeticException("False profileImage url...");

			}

		}

		try {
			if (file == null || file.isEmpty()) {
				throw new Exception();
			}
			String contentType = file.getContentType();
			if (contentType != null && contentType.startsWith("image/")) {
				String bucketName = supabaseProperties.getBucket().getProfile();
				String fileName = user.getUserName();
				supabaseStorageService.deleteFile(bucketName, fileName);
				String profileImageUrl = uploadProfileImage(file, user.getUserName());
				user.setProfileImage(profileImageUrl);
			} else {
				throw new ArithmeticException();
			}
		} catch (ArithmeticException e) {
			throw new ArithmeticException("Profile image must be image not other file...");
		} catch (Exception e) {
		}

		try {

			user.setId(userId);

			// user = userRepository.save(user);

			Query query = new Query(Criteria.where("_id").is(user.getId()));

			Update update = new Update();

			update.set("id", user.getId());
			update.set("userName", user.getUserName());
			update.set("fullName", user.getFullName());
			update.set("profileImage", user.getProfileImage());
			update.set("password", user.getPassword());

			mongoTemplate.updateFirst(query, update, User.class);

			user = mongoTemplate.findOne(query, User.class);

			if (user != null) {
				return user;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new ArithmeticException("Profile not updated...");
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserResponseDTO findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(id).get();

			if (user == null) {

				throw new Exception();

			}

			return getUserResponse(user);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserResponseDTO> findAll() {

		try {

			List<User> users = userRepository.findAll();

			if (users.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(users);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllPagination_' + #page")
	public UserPageResponseDTO findAll(Pageable page, HttpServletRequest request) {

		try {

			Page<User> users = userRepository.findAll(page);

			if (users == null || users.isEmpty()) {

				throw new Exception();

			}

			List<UserResponseDTO> responses = getUserResponse(users.getContent());

			return getPageResponse(responses, request, users);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserName_' + #userName")
	public UserResponseDTO findByUserName(String userName) {

		if (userName == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findByUserName(userName);

			if (user == null) {

				throw new Exception();

			}

			return getUserResponse(user);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByFullNamePrefix_' + #fullName")
	public List<UserResponseDTO> findByFullNameContainingIgnoreCase(String fullName) {

		if (fullName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<User> users = userRepository.findAll();

			if (users.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(users);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByFullNamePrefix_' + #fullName + '_' + #page")
	public UserPageResponseDTO findByFullNameContainingIgnoreCase(String fullName, Pageable page,
			HttpServletRequest request) {

		if (fullName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<User> users = userRepository.findByFullNameContainingIgnoreCase(fullName, page);

			if (users == null || users.isEmpty()) {

				throw new Exception();

			}

			List<UserResponseDTO> responses = getUserResponse(users.getContent());

			return getPageResponse(responses, request, users);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByProfileImagePrefix_' + #profileImage")
	public List<UserResponseDTO> findByProfileImageContainingIgnoreCase(String profileImage) {

		if (profileImage == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<User> users = userRepository.findByProfileImageContainingIgnoreCase(profileImage);

			if (users.isEmpty()) {

				throw new Exception();

			}

			return getUserResponse(users);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByProfileImagePrefix_' + #profileImage + '_' + #page")
	public UserPageResponseDTO findByProfileImageContainingIgnoreCase(String profileImage, Pageable page,
			HttpServletRequest request) {

		if (profileImage == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<User> users = userRepository.findByProfileImageContainingIgnoreCase(profileImage, page);

			if (users == null || users.isEmpty()) {

				throw new Exception();

			}

			List<UserResponseDTO> responses = getUserResponse(users.getContent());

			return getPageResponse(responses, request, users);

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	public boolean deleteUser(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		User _user = null;

		boolean isOwn = false;

		try {
			_user = userRepository.findById(id).get();
			if (_user == null) {
				throw new Exception();
			}

			isOwn = _user.getId().equals(userId);

		} catch (Exception e) {
			throw new NoSuchElementException("No such user find at here...");
		}

		if (isOwn) {

			return cleaner.removeUser(id);

		} else {

			try {

				UserRoles roles = roleRepository.findByUserId(userId);

				if (roles == null) {

					throw new Exception();

				}

				if (!roles.getRoles().contains(Roles.ADMIN)) {

					throw new Exception();

				}

			} catch (Exception e) {

				throw new ArithmeticException("Only admin can remove other's profile...");

			}

			return cleaner.removeUser(id);

		}

	}

	private String uploadProfileImage(MultipartFile file, String userName) {
		try {
			String bucketName = supabaseProperties.getBucket().getProfile();
			String fileName = userName;

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

	private UserResponseDTO getUserResponse(User user) {

		List<User> list = new ArrayList<>();

		list.add(user);

		return getUserResponse(list).get(0);

	}

	private List<UserResponseDTO> getUserResponse(List<User> list) {

		List<UserResponseDTO> responses = new ArrayList<>();

		CompletableFuture<List<String>> usersIdListFuture = CompletableFuture
				.supplyAsync(() -> list.stream().map(User::getId).collect(Collectors.toList()), executor);

		CompletableFuture<Map<String, UserContact>> contactMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return contactRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull).distinct()
					.collect(Collectors.toMap(UserContact::getUserId, Function.identity()));

		}, executor);

		CompletableFuture<Map<String, UserGender>> genderMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return genderRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserGender::getUserId, Function.identity()));

		}, executor);

		CompletableFuture<Map<String, UserInterest>> interestMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return interestRepository.findByUserIdIn(usersId).stream().filter(Objects::nonNull)
					.collect(Collectors.toMap(UserInterest::getUserId, Function.identity()));

		}, executor);

		CompletableFuture<Map<String, UserRoles>> roleMapFuture = usersIdListFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return roleRepository.findByUserIdIn(usersId).stream()
					.collect(Collectors.toMap(UserRoles::getUserId, Function.identity()));

		}, executor);

		CompletableFuture.allOf(usersIdListFuture, contactMapFuture, genderMapFuture, interestMapFuture, roleMapFuture)
				.join();

		Map<String, UserContact> contactMap = contactMapFuture.join();
		Map<String, UserGender> genderMap = genderMapFuture.join();
		Map<String, UserInterest> interestMap = interestMapFuture.join();
		Map<String, UserRoles> rolesMap = roleMapFuture.join();

		for (User user : list) {

			try {

				UserResponseDTO response = new UserResponseDTO();

				response.setId(user.getId());
				response.setUserName(user.getUserName());
				response.setFullName(user.getFullName());
				response.setProfileImage(user.getProfileImage());

				try {

					response.setContact(contactMap.get(user.getId()));

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					response.setGender(genderMap.get(user.getId()));

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					response.setInterst(interestMap.get(user.getId()));

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					response.setRoles(rolesMap.get(user.getId()));

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

	private UserPageResponseDTO getPageResponse(List<UserResponseDTO> responses, HttpServletRequest http,
			Page<User> usersPage) {

		String baseUrl = ServletUriComponentsBuilder.fromRequestUri(http).replaceQueryParam("page") // Removes ONLY
																									// "page"
				.replaceQueryParam("size") // Removes ONLY "size"
				.toUriString();

		String separator = baseUrl.contains("?") ? "&" : "?";

		String nextPage = usersPage.hasNext()
				? baseUrl + separator + "page=" + (usersPage.getNumber() + 1) + "&size=" + usersPage.getSize()
				: null;

		String previousPage = usersPage.hasPrevious()
				? baseUrl + separator + "page=" + (usersPage.getNumber() - 1) + "&size=" + usersPage.getSize()
				: null;

		return new UserPageResponseDTO(responses, usersPage.getNumber(), usersPage.getSize(),
				usersPage.getTotalElements(), nextPage, previousPage);

	}

}
