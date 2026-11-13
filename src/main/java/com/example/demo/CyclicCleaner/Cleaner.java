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
import com.example.demo.Services.AttachmentsServices.SupabaseStorageService;
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

	@Autowired
	private SupabaseStorageService supabaseStorageService;


	private void deleteAttachmentFromSupabase(String url) {
		if (url == null || url.isBlank()) return;
		try {

			String prefix = "/storage/v1/object/public/";
			int index = url.indexOf(prefix);
			if (index != -1) {

				String path = url.substring(index + prefix.length());
				String[] parts = path.split("/", 2);
				if (parts.length == 2) {
					String bucketName = parts[0];
					String fileName = parts[1];
					supabaseStorageService.deleteFile(bucketName, fileName);
					System.out.println("Deleted from Supabase: Bucket=" + bucketName + ", File=" + fileName);
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to delete attachment from Supabase: " + e.getMessage());
		}
	}

	public boolean removeUser(String id) {

		try {

			User user = userRepository.findById(id).get();

			if (user == null) {
				throw new Exception();
			}


			if (user.getProfileImage() != null) {
				deleteAttachmentFromSupabase(user.getProfileImage());
			}


			try {
				List<Notes> notes = notesRepository.findByUserId(user.getId());
				if (notes != null && !notes.isEmpty()) {
					for (Notes note : notes) {

						if (note.getNoteAttachmentsUrls() != null) {
							for (String url : note.getNoteAttachmentsUrls()) {
								deleteAttachmentFromSupabase(url);
							}
						}

						notesRepository.deleteById(note.getId());
					}
				}
			} catch (Exception e) {
				System.out.println("Failed to delete user notes: " + e.getMessage());
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
			}

			return removed;

		} catch (Exception e) {
			System.out.println("Error removing user: " + e.getMessage());
			return false;
		}
	}

	public boolean removeUserContact(String id) {
		try {
			UserContact contact = contactRepository.findById(id).get();
			if (contact == null) throw new Exception();
			long count = contactRepository.count();
			contactRepository.deleteById(id);
			return contactRepository.count() != count;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean removeUserGender(String id) {
		try {
			UserGender gender = genderRepository.findById(id).get();
			if (gender == null) throw new Exception();
			long count = genderRepository.count();
			genderRepository.deleteById(id);
			return count != genderRepository.count();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean removeUserRoles(String id) {
		try {
			UserRoles roles = rolesRepository.findById(id).get();
			if (roles == null) throw new Exception();
			long count = rolesRepository.count();
			rolesRepository.deleteById(id);
			return count != rolesRepository.count();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean removeUserInterest(String id) {
		try {
			UserInterest interest = interestRepository.findById(id).get();
			if (interest == null) throw new Exception();
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


			if (notes.getNoteAttachmentsUrls() != null) {
				for (String url : notes.getNoteAttachmentsUrls()) {
					deleteAttachmentFromSupabase(url);
				}
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
