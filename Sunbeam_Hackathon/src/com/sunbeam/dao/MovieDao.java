package com.sunbeam.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sunbeam.entity.Movie;
import com.sunbeam.entity.Review;
import com.sunbeam.utility.Util;

public class MovieDao implements AutoCloseable {
	private static Connection connection = null;
	private static PreparedStatement stmtSave;
	private static PreparedStatement stmtfindById;
	private static PreparedStatement stmtfindAll;
	private static PreparedStatement stmtfindAllId;
	private static PreparedStatement stmtdeleteById;
	private static PreparedStatement stmtupdateById;

	static {
		try {
			connection = Util.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MovieDao() throws SQLException {
		stmtfindAll = connection.prepareStatement("SELECT * FROM MOVIES");

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public List<Movie> getAllMovies() throws SQLException {
		List<Movie> movies = new ArrayList<>();
		try (ResultSet rs = stmtfindAll.executeQuery()) {
			while (rs.next()) {
				movies.add(new Movie(rs.getInt("id"), rs.getString("title"),
						new Date(rs.getDate("rel_date").getTime())));
			}
		} // rs.close();
		return movies;
	}
}
