package com.sunbeam.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.regex.Pattern;

public class User {
	private int id;
	private String fname;
	private String lname;
	private String email;
	private String password;
	private String mobile;
	private Date birth;

	public User(int id, String fname, String lname, String email, String password, String mobile, Date birth)
			throws Exception {
		super();
		this.id = id;
		this.setFname(fname);
		this.setLname(lname);
		this.setEmail(email);
		this.setPassword(password);
		this.setMobile(mobile);
		this.setBirth(birth);
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) throws Exception {
		if (fname == null || fname.length() == 0)
			throw new Exception("Name cannot be empty");
		else
			this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) throws Exception {
		if (lname == null || lname.length() == 0)
			throw new Exception("Name cannot be empty");
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws Exception {
		if (email == null || email.length() == 0)
			throw new Exception("email cannot be empty");
		if (Pattern.compile("^(.+)@(.+)$").matcher(email).matches())
			this.email = email;
		else
			throw new Exception("Invalid email");
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		if (password == null)
			return;
		// digit + lowercase char + uppercase char + punctuation + symbol
		final String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}";
		if (password.matches(pattern))
			this.password = password;
		else
			throw new Exception("Invalid password");
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) throws Exception {
		String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
		if (mobile.matches(pattern)) {
			this.mobile = mobile;
//			System.out.println("VALID");
		} else
			throw new Exception("Invalid phone number");
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("User [id=%s, fname=%s, lname=%s, email=%s, password=%s, mobile=%s, birth=%s]\n", id,
				fname, lname, email, password, mobile, birth);
	}

}
