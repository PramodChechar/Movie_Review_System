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
import com.sunbeam.entity.User;
import com.sunbeam.utility.Util;

public class ReviewDao implements AutoCloseable {
	private static Connection connection = null;
	private static PreparedStatement stmtSave;
	private static PreparedStatement stmtfindById;
	private static PreparedStatement stmtfindByReviewId;
	private static PreparedStatement stmtfindAllId;
	private static PreparedStatement stmtfindAllIdMovies;
	private static PreparedStatement stmtfindAll;
	private static PreparedStatement stmtfindAllMyReviews;
	private static PreparedStatement stmtdeletebyId1;
	private static PreparedStatement stmtdeletebyId2;
	private static PreparedStatement stmteditReview;
	private static PreparedStatement stmtfindAllReviewIdsbyUserId;

	static {
		try {
			connection = Util.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ReviewDao() throws SQLException {
		stmtfindAllReviewIdsbyUserId = connection.prepareStatement("SELECT ID FROM REVIEWS WHERE USER_ID = ?");
		stmteditReview = connection.prepareStatement("UPDATE REVIEWS SET REVIEW = ? , RATING = ? WHERE ID = ?");
		stmtfindByReviewId = connection.prepareStatement("SELECT * FROM REVIEWS WHERE ID = ?");
		stmtfindAllIdMovies = connection.prepareStatement("SELECT ID FROM MOVIES");
		stmtfindAllId = connection.prepareStatement("SELECT ID FROM REVIEWS");
		stmtdeletebyId1 = connection.prepareStatement("DELETE FROM SHARES WHERE ID = ?");
		stmtdeletebyId2 = connection.prepareStatement("DELETE FROM REVIEWS WHERE ID = ?");
		stmtfindAllMyReviews = connection
				.prepareStatement(" SELECT * FROM REVIEWS WHERE ID = ANY(SELECT ID FROM SHARES\n"
						+ "		 WHERE USER_ID = ?)");
		stmtfindAll = connection.prepareStatement("SELECT * FROM REVIEWS");
		stmtfindById = connection.prepareStatement("SELECT * FROM REVIEWS WHERE USER_ID = ?");
		stmtSave = connection.prepareStatement("INSERT INTO REVIEWS VALUES(DEFAULT, ?, ?, ?, ?, DEFAULT)");
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	public List<Review> getAllReviews() throws SQLException {
		List<Review> reviews = new ArrayList<>();
		try (ResultSet rs = stmtfindAll.executeQuery()) {
			while (rs.next()) {
				reviews.add(new Review(rs.getInt("id"), rs.getInt("movie_id"), rs.getString("review"),
						rs.getInt("rating"), rs.getInt("user_id"), new Date(rs.getDate("modified").getTime())));
			}
		} // rs.close();
		return reviews;
	}

	public int insertReview(int id, int movieId, int rating, String review) throws SQLException {
		stmtSave.setInt(1, movieId);
		stmtSave.setString(2, review);
		stmtSave.setInt(3, rating);
		stmtSave.setInt(4, id);
		return stmtSave.executeUpdate();
	}

	public List<Review> getAllMyReviews(int id) throws SQLException {
		stmtfindById.setInt(1, id);
		List<Review> reviews = new ArrayList<>();
		try (ResultSet rs = stmtfindById.executeQuery()) {
			while (rs.next()) {
				reviews.add(new Review(rs.getInt("id"), rs.getInt("movie_id"), rs.getString("review"),
						rs.getInt("rating"), rs.getInt("user_id"), new Date(rs.getDate("modified").getTime())));
			}
		} // rs.close();
		return reviews;
	}

	public List<Review> getAllMyReviewsShared(int id) throws SQLException {
		stmtfindAllMyReviews.setInt(1, id);
		List<Review> reviews = new ArrayList<>();
		try (ResultSet rs = stmtfindAllMyReviews.executeQuery()) {
			while (rs.next()) {
				reviews.add(new Review(rs.getInt("id"), rs.getInt("movie_id"), rs.getString("review"),
						rs.getInt("rating"), rs.getInt("user_id"), new Date(rs.getDate("modified").getTime())));
			}
		} // rs.close();
//		System.out.println(reviews);
		return reviews;
	}

	public int deleteReview(int id, int review_id) throws SQLException {
		List<Integer> reviewsIdsOfUser = this.checkIfReviewAvailable(id, review_id);
		
		if(reviewsIdsOfUser.contains(review_id)) {
			stmtdeletebyId1.setInt(1, review_id);
			stmtdeletebyId2.setInt(1,review_id);
			int i1 = stmtdeletebyId1.executeUpdate();
			int i2 = stmtdeletebyId2.executeUpdate();
			return i1 + i2;
		}
		
		else return -1;
	}
	
	public List<Integer> checkIfReviewAvailable(int id, int review_id) throws SQLException {
		List<Integer> reviewsIdsOfUser = new ArrayList<>();
		stmtfindAllReviewIdsbyUserId.setInt(1, id);
		
		try(ResultSet rs = stmtfindAllReviewIdsbyUserId.executeQuery()){
			while(rs.next()) {
				reviewsIdsOfUser.add(rs.getInt("review_id"));
			}
		}
		return reviewsIdsOfUser;
	}
	// review of own 
	public boolean checkIfAvailable(int reviewId) throws SQLException {
		List<Integer> reviews = new ArrayList<>();
		try (ResultSet rs = stmtfindAllId.executeQuery()) {
			while (rs.next()) {
				reviews.add(rs.getInt("id"));
			}
		} // rs.close()
		return reviews.contains(reviewId);
	}
	
	public boolean movieAvailable(int movie_id) throws SQLException {
		List<Integer> movies = new ArrayList<>();
		try (ResultSet rs = stmtfindAllIdMovies.executeQuery()) {
			while (rs.next()) {
				movies.add(rs.getInt("id"));
			}
		} // rs.close()
		return movies.contains(movie_id);
	}

	public Review getReviewById(int reviewId) throws SQLException {
		stmtfindByReviewId.setInt(1, reviewId);
		try (ResultSet rs = stmtfindByReviewId.executeQuery()) {
			if (rs.next()) {
				return new Review(rs.getInt("id"), rs.getInt("movie_id"), rs.getString("review"),
						rs.getInt("rating"), rs.getInt("user_id"), new Date(rs.getDate("modified").getTime()));
			}
		}
		return null;
	}

	public int editReview(int reviewId, String review, int rating) throws SQLException {
		stmteditReview.setString(1, review);
		stmteditReview.setInt(2, rating);
		stmteditReview.setInt(3, reviewId);

		return stmteditReview.executeUpdate();
	}

}
