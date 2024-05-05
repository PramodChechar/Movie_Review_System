package com.sunbeam.entity;

import java.sql.Date;

public class Movie {
	private int id;
	private String title;
	private Date release;

	public Movie(int id, String title, Date release) {
		super();
		this.id = id;
		this.title = title;
		this.release = release;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getRelease() {
		return release;
	}

	public void setRelease(Date release) {
		this.release = release;
	}

	@Override
	public String toString() {
		return String.format("Movie [id=%s, title=%s, release=%s]\n", id, title, release);
	}

}
