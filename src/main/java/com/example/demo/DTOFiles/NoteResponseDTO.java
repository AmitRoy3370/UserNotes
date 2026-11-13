package com.example.demo.DTOFiles;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NoteResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8L;

	private String id;

	private String userId, userName, fullName;

	private String profileImage, postType;

	private String noteContent;

	private Instant postedDate = Instant.now();

	private Instant updatedAt = Instant.now();

	private List<String> noteAttachmentsUrls = new ArrayList<>();

	public NoteResponseDTO(String id, String userId, String userName, String fullName, String profileImage,
			String postType, String noteContent, Instant postedDate, Instant updatedAt,
			List<String> noteAttachmentsUrls) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.fullName = fullName;
		this.profileImage = profileImage;
		this.postType = postType;
		this.noteContent = noteContent;
		this.postedDate = postedDate;
		this.updatedAt = updatedAt;
		this.noteAttachmentsUrls = noteAttachmentsUrls;
	}

	public NoteResponseDTO() {
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getNoteContent() {
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
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

	public List<String> getNoteAttachmentsUrls() {
		return noteAttachmentsUrls;
	}

	public void setNoteAttachmentsUrls(List<String> noteAttachmentsUrls) {
		this.noteAttachmentsUrls = noteAttachmentsUrls;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	@Override
	public String toString() {
		return "NoteResponseDTO [id=" + id + ", userId=" + userId + ", userName=" + userName + ", fullName=" + fullName
				+ ", profileImage=" + profileImage + ", postType=" + postType + ", noteContent=" + noteContent
				+ ", postedDate=" + postedDate + ", updatedAt=" + updatedAt + ", noteAttachmentsUrls="
				+ noteAttachmentsUrls + "]";
	}

}
