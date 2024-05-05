package com.sunbeam.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sunbeam.entity.User;
import com.sunbeam.utility.Util;

public class UserDao implements AutoCloseable {

	private static Connection connection = null;
	private static PreparedStatement stmtSave;
	private static PreparedStatement stmtfindById;
	private static PreparedStatement stmtfindPassById;
	private static PreparedStatement stmtfindAll;
	private static PreparedStatement stmtdeleteById;
	private static PreparedStatement stmtupdateById;
	private static PreparedStatement stmtupdatePasswordById;

	static {
		try {
			connection = Util.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public UserDao() throws SQLException {
		stmtSave = connection.prepareStatement("INSERT INTO USERS VALUES(DEFAULT, ?, ?, ?, ? ,? ,?)");
		stmtfindById = connection.prepareStatement("SELECT ID FROM USERS WHERE ID = ?");
		stmtfindPassById = connection.prepareStatement("SELECT PASSWORD FROM USERS WHERE ID = ?");
		stmtfindAll = connection.prepareStatement("SELECT * FROM USERS");
		stmtupdateById = connection.prepareStatement("UPDATE USERS SET first_name = ? , "
				+ "last_name = ?, email = ? ,mobile = ?, birth = ? WHERE ID = ?");
		stmtupdatePasswordById = connection.prepareStatement("UPDATE USERS SET password = ? WHERE ID = ?");
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public int saveUser(User user) throws SQLException {

		stmtSave.setString(1, user.getFname());
		stmtSave.setString(2, user.getLname());
		stmtSave.setString(3, user.getEmail());
		stmtSave.setString(4, user.getPassword());
		stmtSave.setString(5, user.getMobile());
		stmtSave.setDate(6, new java.sql.Date(user.getBirth().getTime()));

		return stmtSave.executeUpdate();
	}

	public static List<User> findAllUser() throws Exception {
		List<User> users = new ArrayList<>();
		try (ResultSet rs = stmtfindAll.executeQuery()) {
			while (rs.next()) {
				users.add(new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("email"), rs.getString("password"), rs.getString("mobile"),
						new Date(rs.getDate("birth").getTime())));
			}
		} // rs.close();
		return users;
	}

	public boolean checkIfAvailable(int id) throws SQLException {
		List<Integer> userId = new ArrayList<>();
		stmtfindById.setInt(1, id);
		try (ResultSet rs = stmtfindById.executeQuery()) {
			while (rs.next()) {
				userId.add(rs.getInt("id"));
			}
		} // rs.close()
		return userId.contains(id);
	}

	public int updateUser(User u, int id) throws SQLException {
		stmtupdateById.setString(1, u.getFname());
		stmtupdateById.setString(2, u.getLname());
		stmtupdateById.setString(3, u.getEmail());
		// stmtupdateById.setString(4, u.getPassword());
		stmtupdateById.setString(4, u.getMobile());
		stmtupdateById.setDate(5, u.getBirth());
		stmtupdateById.setInt(6, id);
		return stmtupdateById.executeUpdate();
	}

	public int updateUserPass(int id, String pass) throws SQLException {
		stmtupdatePasswordById.setString(1, pass);
		stmtupdatePasswordById.setInt(2, id);
		return stmtupdatePasswordById.executeUpdate();
	}

	public boolean checkIfPasswordAvailable(int id, String pass) throws SQLException {
		stmtfindPassById.setInt(1, id);
		String passDb = null;
		try (ResultSet rs = stmtfindPassById.executeQuery()) {
			if (rs.next()) {
				passDb = rs.getString("password");
			}
		}
		return passDb.equals(pass);
	}

}
