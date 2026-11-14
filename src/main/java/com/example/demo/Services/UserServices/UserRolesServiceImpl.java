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
import com.example.demo.ENums.Roles;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserRoles;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserRolesRepository;

@Service
public class UserRolesServiceImpl implements UserRolesService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRolesRepository rolesRepository;

	@Autowired
	private Cleaner cleaner;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final String cacheValue = "UserRoles";

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserRoles addRole(UserRoles roles, String userId) {

		if (roles == null || userId == null || roles.getRoles() == null || roles.getRoles().isEmpty()
				|| !roles.getUserId().equals(userId)) {

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

			UserRoles userRoles = rolesRepository.findByUserId(userId);

			if (userRoles != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's roles is already setted...");

		} catch (Exception e) {

		}

		if (roles.getRoles().contains(Roles.ADMIN)) {

			throw new ArithmeticException("Admin can be set only by the developer...");

		}

		roles = rolesRepository.save(roles);

		if (roles == null) {

			throw new ArithmeticException("Data not saved...");

		}

		return roles;

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public UserRoles updateRole(UserRoles roles, String userId, String id) {

		if (roles == null || userId == null || roles.getRoles() == null || roles.getRoles().isEmpty()
				|| !roles.getUserId().equals(userId)) {

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

			UserRoles userRoles = rolesRepository.findById(id).get();

			if (userRoles == null) {

				throw new Exception();

			}

			if (!userRoles.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such rols find at here...");

		}

		boolean previousAdmin = false;

		try {

			UserRoles userRoles = rolesRepository.findByUserId(userId);

			if (userRoles != null) {

				if (!userRoles.getId().equals(id)) {

					throw new Exception();

				}

			}

			previousAdmin = userRoles.getRoles().contains(Roles.ADMIN);

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's roles is already setted...");

		} catch (Exception e) {

		}

		if (!previousAdmin) {

			if (roles.getRoles().contains(Roles.ADMIN)) {

				throw new ArithmeticException("Admin can be set only by the developer...");

			}

		}

		roles.setId(id);

		//roles = rolesRepository.save(roles);

		Query query = new Query(Criteria.where("_id").is(id));

		Update update = new Update();

		update.set("id", roles.getId());
		update.set("userId", roles.getUserId());
		update.set("roles", roles.getRoles());

		mongoTemplate.updateFirst(query, update, UserRoles.class);

		roles = mongoTemplate.findOne(query, UserRoles.class);

		if (roles == null) {

			throw new ArithmeticException("Data not saved...");

		}

		return roles;
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserRoles findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserRoles roles = rolesRepository.findById(id).get();

			if (roles == null) {

				throw new Exception();

			}

			return roles;

		} catch (Exception e) {

			throw new NoSuchElementException("No such roles find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserRoles> findAll() {

		try {

			List<UserRoles> users = rolesRepository.findAll();

			if (users.isEmpty()) {

				throw new Exception();

			}

			return users;

		} catch (Exception e) {

			throw new NoSuchElementException("No such roles find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllPaginated_' + #page")
	public Page<UserRoles> findAll(Pageable page) {

		try {

			Page<UserRoles> users = rolesRepository.findAll(page);

			if (users.isEmpty()) {

				throw new Exception();

			}

			return users;

		} catch (Exception e) {

			throw new NoSuchElementException("No such roles find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public UserRoles findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserRoles roles = rolesRepository.findByUserId(userId);

			if (roles == null) {

				throw new Exception();

			}

			return roles;

		} catch (Exception e) {

			throw new NoSuchElementException("No such roles find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersId_' + #usersId")
	public List<UserRoles> findByUserIdIn(List<String> usersId) {

		if (usersId == null || usersId.isEmpty()) {

			throw new NullPointerException("Falsw request...");

		}

		try {

			List<UserRoles> list = rolesRepository.findByUserIdIn(usersId);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such roles find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByRolesPrefix_' + #roles")
	public List<UserRoles> findByRolesContainingIgnoreCase(Roles roles) {

		if (roles == null) {

			throw new NullPointerException("Falsw request...");

		}

		try {

			List<UserRoles> list = rolesRepository.findByRolesContainingIgnoreCase(roles);

			System.out.println("roles list :- " + list);

			if (list.isEmpty()) {

				throw new Exception();

			}



			return list;

		} catch (Exception e) {

		    System.out.println("error in find by role is :- " + e);

			throw new NoSuchElementException("No such roles find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByRolesPrefixPaginated_' + #roles + '_' + #page")
	public Page<UserRoles> findByRolesContainingIgnoreCase(Roles roles, Pageable page) {

		if (roles == null) {

			throw new NullPointerException("Falsw request...");

		}

		try {

			Page<UserRoles> list = rolesRepository.findByRolesContainingIgnoreCase(roles, page);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such roles find at here...");

		}

	}

	@Override
	@Caching(evict = { @CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true) })
	public boolean deleteRoles(String id, String userId) {

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

			UserRoles userRoles = rolesRepository.findById(id).get();

			if (userRoles == null) {

				throw new Exception();

			}

			if (!userRoles.getUserId().equals(userId)) {

				throw new Exception();

			}

			return cleaner.removeUserRoles(id);

		} catch (Exception e) {

			throw new NoSuchElementException("No such rols find at here...");

		}

	}

}
