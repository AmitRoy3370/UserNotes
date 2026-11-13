package com.example.demo.Models.NoteModels;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

@Document(collection = "Notes")
public class Notes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5L;

	@Id
	private String id;

	@Indexed
	@NonNull
	private String userId;

	@Indexed
	@NonNull
	private String noteContent;

	@Indexed
	private Instant postedDate = Instant.now();

	@Indexed
	@NonNull
	private String postType;

	@Indexed
	private Instant updatedAt = Instant.now();

	@Indexed
	private List<String> noteAttachmentsUrls = new ArrayList<>();

	public Notes(String userId, String noteContent, Instant postedDate, String postType, Instant updatedAt,
			List<String> noteAttachmentsUrls) {
		super();
		this.userId = userId;
		this.noteContent = noteContent;
		this.postedDate = postedDate;
		this.postType = postType;
		this.updatedAt = updatedAt;
		this.noteAttachmentsUrls = noteAttachmentsUrls;
	}

	public Notes() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNoteContent() {
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public List<String> getNoteAttachmentsUrls() {
		return noteAttachmentsUrls;
	}

	public void setNoteAttachmentsUrls(List<String> noteAttachmentsUrls) {
		this.noteAttachmentsUrls = noteAttachmentsUrls;
	}

	public Instant getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Instant postedDate) {
		this.postedDate = postedDate;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Notes [id=" + id + ", userId=" + userId + ", noteContent=" + noteContent + ", postedDate=" + postedDate
				+ ", postType=" + postType + ", updatedAt=" + updatedAt + ", noteAttachmentsUrls=" + noteAttachmentsUrls
				+ "]";
	}

}
