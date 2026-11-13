package com.example.demo.Services.UserServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.CyclicCleaner.Cleaner;
import com.example.demo.ENums.Gender;
import com.example.demo.ENums.Roles;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserRoles;
import com.example.demo.Repositories.UserRepositories.UserGenderRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserRolesRepository;

@Service
public class UserGenderServiceImpl implements UserGenderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserGenderRepository genderRepository;

	@Autowired
	private UserRolesRepository rolesRepository;

	@Autowired
	private Cleaner cleaner;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final String cacheValue = "UserGender";

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserGender addGender(UserGender gender, String userId) {

		if (gender == null || userId == null || gender.getGender() == null || !gender.getUserId().equals(userId)) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserGender userGender = genderRepository.findByUserId(user.getId());

			if (userGender != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's gender is already setted...");

		} catch (Exception e) {

		}

		gender = genderRepository.save(gender);

		if (gender == null) {

			throw new ArithmeticException("No such gender find at here...");

		}

		return gender;

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserGender updateGender(UserGender gender, String userId, String id) {

		if (id == null || gender == null || userId == null || gender.getGender() == null
				|| !gender.getUserId().equals(userId)) {

			throw new NullPointerException("False request...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserGender userGender = genderRepository.findById(id).get();

			if (userGender == null) {

				throw new Exception();

			}

			if (!userGender.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gender find at here...");

		}

		try {

			UserGender userGender = genderRepository.findByUserId(user.getId());

			if (userGender != null) {

				if (!userGender.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's gender is already setted...");

		} catch (Exception e) {

		}

		gender.setId(id);

		// gender = genderRepository.save(gender);

		Query query = new Query(Criteria.where("_id").is(id));

		Update update = new Update();

		update.set("id", gender.getId());
		update.set("userId", gender.getUserId());
		update.set("gender", gender.getGender());

		mongoTemplate.updateFirst(query, update, UserGender.class);

		gender = mongoTemplate.findOne(query, UserGender.class);

		if (gender == null) {

			throw new ArithmeticException("No such gender find at here...");

		}

		return gender;
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserGender findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserGender gender = genderRepository.findById(id).get();

			if (gender == null) {

				throw new Exception();

			}

			return gender;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gender find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserGender> findAll() {

		try {

			List<UserGender> list = genderRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll_' + #page")
	public Page<UserGender> findAll(Pageable page) {

		try {

			Page<UserGender> list = genderRepository.findAll(page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public UserGender findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserGender list = genderRepository.findByUserId(userId);

			if (list == null) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersId_' + #usersId")
	public List<UserGender> findByUserIdIn(List<String> usersId) {

		if (usersId == null || usersId.isEmpty()) {

			throw new NullPointerException("False request...");

		}

		try {

			List<UserGender> list = genderRepository.findByUserIdIn(usersId);

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}
	}

	@Override
	@CacheEvict(value = cacheValue, key = "'findByGender_' + #gender")
	public List<UserGender> findByGender(Gender gender) {

		if (gender == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<UserGender> list = genderRepository.findByGender(gender);

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}
	}

	@Override
	@CacheEvict(value = cacheValue, key = "'findByGenderPaginated_' + #gender + '_' + #page")
	public Page<UserGender> findByGender(Gender gender, Pageable page) {

		if (gender == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<UserGender> list = genderRepository.findByGender(gender, page);

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}
	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean deleteGender(String id, String userId) {

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

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			UserGender userGender = genderRepository.findById(id).get();

			if (userGender == null) {

				throw new Exception();

			}

			if (userGender.getUserId().equals(userId)) {

				return cleaner.removeUserGender(id);

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gender find at here...");

		}

		try {

			UserRoles roles = rolesRepository.findByUserId(userId);

			if (roles == null) {

				throw new Exception();

			}

			if (roles.getRoles().contains(Roles.ADMIN)) {

				return cleaner.removeUserGender(id);

			} else {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Only admin can remove other gender...");

		}

	}

}
