package com.example.demo.CyclicCleaner;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.NoteModels.Notes;
import com.example.demo.Models.UserModels.User;
import com.example.demo.Models.UserModels.UserContact;
import com.example.demo.Models.UserModels.UserGender;
import com.example.demo.Models.UserModels.UserInterest;
import com.example.demo.Models.UserModels.UserRoles;
import com.example.demo.Repositories.NoteRepositories.NoteRepository;
import com.example.demo.Repositories.UserRepositories.UserContactRepository;
import com.example.demo.Repositories.UserRepositories.UserGenderRepository;
import com.example.demo.Repositories.UserRepositories.UserInterestRepository;
import com.example.demo.Repositories.UserRepositories.UserRepository;
import com.example.demo.Repositories.UserRepositories.UserRolesRepository;
import com.example.demo.Services.UserServices.RedisService;

@Service
public class Cleaner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserContactRepository contactRepository;

	@Autowired
	private UserGenderRepository genderRepository;

	@Autowired
	private UserRolesRepository rolesRepository;

	@Autowired
	private UserInterestRepository interestRepository;

	@Autowired
	private NoteRepository notesRepository;
	
	@Autowired
	private RedisService redisService;

	public boolean removeUser(String id) {

		try {

			User user = userRepository.findById(id).get();

			if (user == null) {

				throw new Exception();

			}

			long count = userRepository.count();

			userRepository.deleteById(id);

			boolean removed = count != userRepository.count();

			if (removed) {
				
				redisService.clearAllCaches();

				try {

					UserContact contact = contactRepository.findByUserId(user.getId());

					if (contact != null) {

						removeUserContact(contact.getId());

					}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserGender gender = genderRepository.findByUserId(user.getId());

					if (gender != null) {

						removeUserGender(gender.getId());

					}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserRoles roles = rolesRepository.findByUserId(user.getId());

					if (roles != null) {

						removeUserRoles(roles.getId());

					}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					UserInterest interest = interestRepository.findByUserId(user.getId());

					if (interest != null) {

						removeUserInterest(interest.getId());

					}

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

				try {

					List<Notes> notes = notesRepository.findByUserId(user.getId());

					List<String> notesId = notes.stream().map(Notes::getId).collect(Collectors.toList());

					notesRepository.deleteAllById(notesId);

				} catch (Exception e) {

					System.out.println(e.getMessage());

				}

			}

			return removed;

		} catch (Exception e) {

			return false;

		}

	}

	public boolean removeUserContact(String id) {

		try {

			UserContact contact = contactRepository.findById(id).get();

			if (contact == null) {

				throw new Exception();

			}

			long count = contactRepository.count();

			contactRepository.deleteById(id);

			boolean removed = contactRepository.count() != count;

			return removed;

		} catch (Exception e) {

			System.out.println(e.getMessage());

			return false;

		}

	}

	public boolean removeUserGender(String id) {

		try {

			UserGender gender = genderRepository.findById(id).get();

			if (gender == null) {

				throw new Exception();

			}

			long count = genderRepository.count();

			genderRepository.deleteById(id);

			boolean removed = count != genderRepository.count();

			return removed;

		} catch (Exception e) {

			System.out.println(e.getMessage());

			return false;

		}

	}

	public boolean removeUserRoles(String id) {

		try {

			UserRoles roles = rolesRepository.findById(id).get();

			if (roles == null) {

				throw new Exception();

			}

			long count = rolesRepository.count();

			rolesRepository.deleteById(id);

			boolean removed = count != rolesRepository.count();

			return removed;

		} catch (Exception e) {

			System.out.println(e.getMessage());

			return false;

		}

	}

	public boolean removeUserInterest(String id) {

		try {

			UserInterest interest = interestRepository.findById(id).get();

			if (interest == null) {

				throw new Exception();

			}

			long count = interestRepository.count();

			interestRepository.deleteById(id);

			return count != interestRepository.count();

		} catch (Exception e) {

			System.out.println(e.getMessage());

			return false;

		}

	}

	public boolean removeNotes(String id) {

		try {

			Notes notes = notesRepository.findById(id).get();

			if (notes == null) {

				throw new Exception();

			}

			long count = notesRepository.count();

			notesRepository.deleteById(id);

			boolean removed = count != notesRepository.count();

			return removed;

		} catch (Exception e) {

			System.out.println(e.getMessage());

			return false;

		}

	}

}
