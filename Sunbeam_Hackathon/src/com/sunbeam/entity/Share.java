package com.sunbeam.entity;

public class Share {
	private int shareId;
	private int reviewId;
	private int userId;

	public Share(int shareId, int reviewId, int userId) {
		super();
		this.shareId = shareId;
		this.reviewId = reviewId;
		this.userId = userId;
	}

	public int getShareId() {
		return shareId;
	}

	public void setShareId(int shareId) {
		this.shareId = shareId;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return String.format("Share [shareId=%s, reviewId=%s, userId=%s]\n", shareId, reviewId, userId);
	}

}
