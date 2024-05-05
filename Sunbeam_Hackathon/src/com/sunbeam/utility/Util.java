package com.sunbeam.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
	public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/moviedb";
	public static final String DB_USERNAME = "root";
	public static final String DB_PASSWORD = "manager";
	
	static {
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	}
}
