package com.example.demo.Services.UserServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.CyclicCleaner.Cleaner;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserInterest;
import com.example.demo.Repositories.UserRepositories.UserInterestRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserRolesRepository;

@Service
public class UserInterestServiceImpl implements UserInterestService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInterestRepository interestRepository;

	@Autowired
	private UserRolesRepository rolesRepository;

	@Autowired
	private Cleaner cleaner;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final String cacheValue = "UserInterest";

	@Override
	@Caching(evict = {

			@CacheEvict(value = cacheValue, allEntries = true), @CacheEvict(value = "User", allEntries = true)

	})
	public UserInterest adInterest(UserInterest interest, String userId) {

		if (interest == null || userId == null || !interest.getUserId().equals(userId)
				|| interest.getInterests() == null || interest.getInterests().isEmpty()) {

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

			UserInterest userInterest = interestRepository.findByUserId(userId);

			if (userInterest != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's inetest is set already...");

		} catch (Exception e) {

		}

		interest = interestRepository.save(interest);

		if (interest == null) {

			throw new ArithmeticException("Data is not saved...");

		}

		return interest;

	}

	@Override
	@Caching(evict = {

			@CacheEvict(value = cacheValue, allEntries = true), @CacheEvict(value = "User", allEntries = true)

	})
	public UserInterest updateInterest(UserInterest interest, String userId, String id) {

		if (id == null || interest == null || userId == null || !interest.getUserId().equals(userId)
				|| interest.getInterests() == null || interest.getInterests().isEmpty()) {

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

			UserInterest userInterest = interestRepository.findById(id).get();

			if (userInterest == null) {

				throw new Exception();

			}

			if (!userInterest.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such interest find at here...");

		}

		try {

			UserInterest userInterest = interestRepository.findByUserId(userId);

			if (userInterest != null) {

				if (!userInterest.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's inetest is set already...");

		} catch (Exception e) {

		}

		interest.setId(id);

		// interest = interestRepository.save(interest);

		Query query = new Query(Criteria.where("_id").is(id));

		Update update = new Update();

		update.set("id", interest.getId());
		update.set("userId", interest.getUserId());
		update.set("interests", interest.getInterests());

		mongoTemplate.updateFirst(query, update, UserInterest.class);

		interest = mongoTemplate.findOne(query, UserInterest.class);

		if (interest == null) {

			throw new ArithmeticException("Data is not saved...");

		}

		return interest;

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserInterest findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserInterest interest = interestRepository.findById(id).get();

			if (interest == null) {

				throw new Exception();

			}

			return interest;

		} catch (Exception e) {

			throw new NullPointerException("No such interest find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserInterest> findAll() {

		try {

			List<UserInterest> list = interestRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such interest find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllPaginated_' + #page")
	public Page<UserInterest> findAll(Pageable page) {

		try {

			Page<UserInterest> list = interestRepository.findAll(page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such interest find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public UserInterest findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserInterest interest = interestRepository.findByUserId(userId);

			if (interest == null) {

				throw new Exception();

			}

			return interest;

		} catch (Exception e) {

			throw new NullPointerException("No such interest find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersId_' + #usersId")
	public List<UserInterest> findByUserIdIn(List<String> usersId) {

		if (usersId == null || usersId.isEmpty()) {

			throw new NullPointerException("False request...");

		}

		try {

			List<UserInterest> list = interestRepository.findByUserIdIn(usersId);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such interest find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByInterests_' + #interests")
	public List<UserInterest> findByInterestsContainingIgnoreCase(String interests) {

		if (interests == null || interests.isEmpty()) {

			throw new NullPointerException("False request...");

		}

		try {

			List<UserInterest> list = interestRepository.findByInterestsContainingIgnoreCase(interests);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such interest find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByInterestsPaginated_' + #interests + '_' + #page")
	public Page<UserInterest> findByInterestsContainingIgnoreCase(String interests, Pageable page) {

		if (interests == null || interests.isEmpty()) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<UserInterest> list = interestRepository.findByInterestsContainingIgnoreCase(interests, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such interest find at here...");

		}
	}

	@Override
	@Caching(evict = {

			@CacheEvict(value = cacheValue, allEntries = true), @CacheEvict(value = "User", allEntries = true)

	})
	public boolean deleteInterest(String id, String userId) {

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

			UserInterest userInterest = interestRepository.findById(id).get();

			if (userInterest == null) {

				throw new Exception();

			}

			if (!userInterest.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such interest find at here...");

		}

		try {

			UserInterest userInterest = interestRepository.findByUserId(userId);

			if (userInterest != null) {

				if (!userInterest.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's inetest is set already...");

		} catch (Exception e) {

		}

		return cleaner.removeUserInterest(id);

	}

}
