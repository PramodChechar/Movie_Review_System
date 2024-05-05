package com.sunbeam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.sunbeam.utility.Util;

public class ShareDao implements AutoCloseable {
	private static Connection connection = null;
	private static PreparedStatement stmtSave;
	private static PreparedStatement stmtfindById;
	private static PreparedStatement stmtfindAll;
	private static PreparedStatement stmtdeleteById;
	private static PreparedStatement stmtupdateById;

	static {
		try {
			connection = Util.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ShareDao() throws SQLException {
		stmtSave = connection.prepareStatement("INSERT INTO SHARES VALUES(DEFAULT, ?, ?)");

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public int shareAReview(List<Integer> users, int otherId, int reviewId) throws SQLException {
		int rows = 0;
		for (int ele : users) {
			stmtSave.setInt(1, reviewId);
			stmtSave.setInt(2, ele);
			rows += stmtSave.executeUpdate();
		}
		return rows;
	}

}
