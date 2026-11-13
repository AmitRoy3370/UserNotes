package com.example.demo.Services.NoteServices;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTOFiles.NotePageResponseDTO;
import com.example.demo.DTOFiles.NoteResponseDTO;
import com.example.demo.Models.NoteModels.Notes;

import jakarta.servlet.http.HttpServletRequest;

public interface NoteService {

	public Notes adNotes(Notes notes, String userId, MultipartFile files[]);

	public Notes updateNotes(Notes notes, String userId, String id, MultipartFile files[]);

	public NoteResponseDTO findById(String id);

	public List<NoteResponseDTO> findAll();

	public NotePageResponseDTO findAll(Pageable page, HttpServletRequest http);

	public List<NoteResponseDTO> findByUserId(String userId);

	public NotePageResponseDTO findByUserId(String userId, Pageable page, HttpServletRequest http);

	public List<NoteResponseDTO> findByUserIdIn(List<String> usersId);

	public List<NoteResponseDTO> findByPostTypeContainingIgnoreCase(String postType);

	public NotePageResponseDTO findByPostTypeContainingIgnoreCase(String postType, Pageable page, HttpServletRequest http);

	public List<NoteResponseDTO> findByNoteContentContainingIgnoreCase(String noteContent);

	public NotePageResponseDTO findByNoteContentContainingIgnoreCase(String noteContent, Pageable page, HttpServletRequest http);

	public List<NoteResponseDTO> findByPostedDateAfter(Instant postedDate);

	public List<NoteResponseDTO> findByPostedDateBefore(Instant postedDate);

	public NotePageResponseDTO findByPostedDateAfter(Instant postedDate, Pageable page, HttpServletRequest http);

	public NotePageResponseDTO findByPostedDateBefore(Instant postedDate, Pageable page, HttpServletRequest http);

	public List<NoteResponseDTO> findByUpdatedAtAfter(Instant updatedAt);

	public List<NoteResponseDTO> findByUpdatedAtBefore(Instant updatedAt);

	public NotePageResponseDTO findByUpdatedAtAfter(Instant updatedAt, Pageable page, HttpServletRequest http);

	public NotePageResponseDTO findByUpdatedAtBefore(Instant updatedAt, Pageable page, HttpServletRequest http);

	public boolean removeNote(String id, String userId);

}
