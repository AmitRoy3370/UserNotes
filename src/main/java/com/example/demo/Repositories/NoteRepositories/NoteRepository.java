package com.example.demo.Repositories.NoteRepositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.NoteModels.Notes;

@Repository
public interface NoteRepository extends MongoRepository<Notes, String> {

	Page<Notes> findAll(Pageable page);
	
	@Query("{ 'userId': ?0 }")
	List<Notes> findByUserId(String userId);

	@Query("{ 'userId': ?0 }")
	Page<Notes> findByUserId(String userId, Pageable page);

	@Query("{ 'userId': { $in: ?0 } }")
	List<Notes> findByUserIdIn(List<String> usersId);

	@Query("{ 'postType': { $regex: ?0, $options: 'i' } }")
	List<Notes> findByPostTypeContainingIgnoreCase(String postType);

	@Query("{ 'postType': { $regex: ?0, $options: 'i' } }")
	Page<Notes> findByPostTypeContainingIgnoreCase(String postType, Pageable page);

	@Query("{ 'noteContent': { $regex: ?0, $options: 'i' } }")
	List<Notes> findByNoteContentContainingIgnoreCase(String noteContent);

	@Query("{ 'noteContent': { $regex: ?0, $options: 'i' } }")
	Page<Notes> findByNoteContentContainingIgnoreCase(String noteContent, Pageable page);

	@Query("{ 'postedDate': { $gte: ?0 } }")
	List<Notes> findByPostedDateAfter(Instant postedDate);

	@Query("{ 'postedDate': { $lte: ?0 } }")
	List<Notes> findByPostedDateBefore(Instant postedDate);

	@Query("{ 'postedDate': { $gte: ?0 } }")
	Page<Notes> findByPostedDateAfter(Instant postedDate, Pageable page);

	@Query("{ 'postedDate': { $lte: ?0 } }")
	Page<Notes> findByPostedDateBefore(Instant postedDate, Pageable page);

	@Query("{ 'updatedAt': { $gte: ?0 } }")
	List<Notes> findByUpdatedAtAfter(Instant updatedAt);

	@Query("{ 'updatedAt': { $lte: ?0 } }")
	List<Notes> findByUpdatedAtBefore(Instant updatedAt);

	@Query("{ 'updatedAt': { $gte: ?0 } }")
	Page<Notes> findByUpdatedAtAfter(Instant updatedAt, Pageable page);

	@Query("{ 'updatedAt': { $lte: ?0 } }")
	Page<Notes> findByUpdatedAtBefore(Instant updatedAt, Pageable page);

}