package com.example.demo.Services.UserServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserRoles;
import com.example.demo.Repositories.UserRepositories.UserContactRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserRolesRepository;
import com.example.demo.Validators.EmailValidator;
import com.example.demo.Validators.PhoneValidator;

@Service
public class UserContactServiceImpl implements UserContactService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserContactRepository contactRepository;

	@Autowired
	private UserRolesRepository rolesRepository;

	private EmailValidator emailValidator = new EmailValidator();

	@Autowired
	private Cleaner cleaner;

	private static final String cacheValue = "UserContact";

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@Caching(evict = {
			@CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true)
			
	})
	public UserContact addContact(UserContact contact, String userId) {

		if (contact == null || userId == null || (contact.getEmail() == null && contact.getPhoneNumber() == null)
				|| !contact.getUserId().equals(userId)) {

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

			if (contact.getEmail() != null && !emailValidator.isValidEmail(contact.getEmail())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Email is not valid...");

		}

		try {

			if (contact.getPhoneNumber() != null && !new PhoneValidator(contact.getPhoneNumber()).isValid()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("phone number is not valid...");

		}

		try {

			UserContact userContact = contactRepository.findByUserId(contact.getUserId());

			if (userContact != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's contact is already setted...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			UserContact userContact = contactRepository.findByEmail(contact.getEmail());

			if (userContact != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's contact mail is already setted...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			UserContact userContact = contactRepository.findByPhoneNumber(contact.getPhoneNumber());

			if (userContact != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's contact phone is already setted...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		contact = contactRepository.save(contact);

		if (contact == null) {

			throw new ArithmeticException("Data not saved...");

		}

		return contact;

	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true)
			
	})
	public UserContact updateContact(UserContact contact, String userId, String id) {

		if (id == null || contact == null || userId == null
				|| (contact.getEmail() == null && contact.getPhoneNumber() == null)
				|| !contact.getUserId().equals(userId)) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact userContact = contactRepository.findById(id).get();

			if (userContact == null) {

				throw new Exception();

			}

			if (!userContact.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

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

			if (contact.getEmail() != null && !emailValidator.isValidEmail(contact.getEmail())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Email is not valid...");

		}

		try {

			if (contact.getPhoneNumber() != null && !new PhoneValidator(contact.getPhoneNumber()).isValid()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("phone number is not valid...");

		}

		try {

			UserContact userContact = contactRepository.findByUserId(contact.getUserId());

			if (userContact != null) {

				if (userContact.getId().equals(id)) {

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's contact is already setted...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			UserContact userContact = contactRepository.findByEmail(contact.getEmail());

			if (userContact != null) {

				if (userContact.getId().equals(id)) {

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's contact mail is already setted...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		try {

			UserContact userContact = contactRepository.findByPhoneNumber(contact.getPhoneNumber());

			if (userContact != null) {

				if (userContact.getId().equals(id)) {

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This user's contact phone is already setted...");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		contact.setId(id);

		// contact = contactRepository.save(contact);

		Query query = new Query(Criteria.where("_id").is(id));

		Update update = new Update();

		update.set("id", contact.getId());
		update.set("email", contact.getEmail());
		update.set("phoneNumber", contact.getPhoneNumber());
		update.set("userId", contact.getUserId());

		mongoTemplate.updateFirst(query, update, UserContact.class);

		contact = mongoTemplate.findOne(query, UserContact.class);

		if (contact == null) {

			throw new ArithmeticException("Data not saved...");

		}

		return contact;

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findById_' + #id")
	public UserContact findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact contact = contactRepository.findById(id).get();

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAll'")
	public List<UserContact> findAll() {
		
		try {
			
			List<UserContact> list = contactRepository.findAll();
			
			if(list.isEmpty()) {
				
				throw new Exception();
				
			}
			
			return list;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such contact find at here...");
			
		}
		
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findAllPaginated_' + #page")
	public Page<UserContact> findAll(Pageable page) {

		try {
			
			Page<UserContact> list = contactRepository.findAll(page);
			
			if(list.isEmpty()) {
				
				throw new Exception();
				
			}
			
			return list;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such contact find at here...");
			
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUserId_' + #userId")
	public UserContact findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact contact = contactRepository.findByUserId(userId);

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}

	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByUsersIdIn_' + #usersId")
	public List<UserContact> findByUserIdIn(List<String> usersId) {

		if(usersId == null || usersId.isEmpty()) {
			
			throw new NullPointerException("False request....");
			
		}
		
		try {
			
			List<UserContact> list = contactRepository.findByUserIdIn(usersId);
			
			if(list.isEmpty()) {
				
				throw new Exception();
				
			}
			
			return list;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException("No such contact find at here...");
			
		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByEmail_' + #email")
	public UserContact findByEmail(String email) {
		if (email == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact contact = contactRepository.findByEmail(email);

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPhone_' + #phoneNumber")
	public UserContact findByPhoneNumber(String phoneNumber) {
		if (phoneNumber == null) {

			throw new NullPointerException("False request...");

		}

		try {

			UserContact contact = contactRepository.findByPhoneNumber(phoneNumber);

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPhonePrefix_' + #phoneNumber")
	public List<UserContact> findByPhoneNumberContainingIgnoreCase(String phoneNumber) {
		if (phoneNumber == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<UserContact> contact = contactRepository.findByPhoneNumberContainingIgnoreCase(phoneNumber);

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByPhnePrefixPaginated_' + #phoneNumber + '_' + #page")
	public Page<UserContact> findByPhoneNumberContainingIgnoreCase(String phoneNumber, Pageable page) {
		if (phoneNumber == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<UserContact> contact = contactRepository.findByPhoneNumberContainingIgnoreCase(phoneNumber, page);

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByEmailPrefix_' + #email")
	public List<UserContact> findByEmailContainingIgnoreCase(String email) {
		if (email == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<UserContact> contact = contactRepository.findByEmailContainingIgnoreCase(email);

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}
	}

	@Override
	@Cacheable(value = cacheValue, key = "'findByEmailPrefix_Paginated_' + #email + '_' + #page")
	public Page<UserContact> findByEmailContainingIgnoreCase(String email, Pageable page) {
		if (email == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Page<UserContact> contact = contactRepository.findByEmailContainingIgnoreCase(email, page);

			if (contact == null) {

				throw new Exception();

			}

			return contact;

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = cacheValue, allEntries = true),
			@CacheEvict(value = "User", allEntries = true)
			
	})
	public boolean removeContact(String id, String userId) {
		
		if(id == null || userId == null) {
			
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

			UserContact userContact = contactRepository.findById(id).get();

			if (userContact == null) {

				throw new Exception();

			}

			if (userContact.getUserId().equals(userId)) {

				return cleaner.removeUserContact(id);

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such contact find at here...");

		}
		
		try {
			
			UserRoles roles = rolesRepository.findByUserId(userId);
			
			if(roles == null) {
				
				throw new Exception();
				
			}
			
			if(!roles.getRoles().contains(Roles.ADMIN)) {
				
				throw new Exception();
				
			} else {
				
				return cleaner.removeUserContact(id);
				
			}
			
		} catch(Exception e) {
			
			throw new ArithmeticException("Only admin can remove al other contacts...");
			
		}
		
	}

}
