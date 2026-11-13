package com.example.demo.DTOFiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserPageResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7L;

	private List<UserResponseDTO> responses = new ArrayList<>();

	private int page, size;

	private long totalElements;

	private String nextPageUrl, previousPageUrl;

	public UserPageResponseDTO(List<UserResponseDTO> responses, int page, int size, long totalElements,
			String nextPageUrl, String previousPageUrl) {
		super();
		this.responses = responses;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.nextPageUrl = nextPageUrl;
		this.previousPageUrl = previousPageUrl;
	}

	public UserPageResponseDTO() {
		super();
	}

	public List<UserResponseDTO> getResponses() {
		return responses;
	}

	public void setResponses(List<UserResponseDTO> responses) {
		this.responses = responses;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public String getNextPageUrl() {
		return nextPageUrl;
	}

	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}

	public String getPreviousPageUrl() {
		return previousPageUrl;
	}

	public void setPreviousPageUrl(String previousPageUrl) {
		this.previousPageUrl = previousPageUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserPageResponseDTO [responses=" + responses + ", page=" + page + ", size=" + size + ", totalElements="
				+ totalElements + ", nextPageUrl=" + nextPageUrl + ", previousPageUrl=" + previousPageUrl + "]";
	}

}
