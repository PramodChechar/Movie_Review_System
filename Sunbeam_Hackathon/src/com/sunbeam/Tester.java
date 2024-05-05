package com.sunbeam;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.sunbeam.dao.MovieDao;
import com.sunbeam.dao.ReviewDao;
import com.sunbeam.dao.ShareDao;
import com.sunbeam.dao.UserDao;
import com.sunbeam.entity.Movie;
import com.sunbeam.entity.Review;
import com.sunbeam.entity.User;

public class Tester {
	private static int loginMenu(Scanner sc) {
		System.out.println("0. : Exit");
		System.out.println("1. : Sign In");
		System.out.println("2. : Sign Up");
		System.out.println("Enter choice");

		return sc.nextInt();
	}

	private static int menu(Scanner sc) {
		System.out.println("0. : Log Out");
		System.out.println("1. : Edit Profile");
		System.out.println("2. : Change Password");
		System.out.println("3. : Create a Review");
		System.out.println("4. : Edit Review");
		System.out.println("5. : Display all Movies");
		System.out.println("6. : Display all Reviews");
		System.out.println("7. : Display my Reviews");
		System.out.println("8. : Display Reviews shared with me");
		System.out.println("9. : Share a Review");
		System.out.println("10. : Delete a Review");
		System.out.println("Enter choice");

		return sc.nextInt();
	}

	private static User addUser(Scanner sc, boolean signUp) throws Exception {
		String password = null;
		// System.out.println("Enter user id");
		// int id = sc.nextInt();
		System.out.println("Enter user firstName");
		String firstName = sc.nextLine();
		firstName += sc.nextLine();
		if (firstName.isEmpty())
			throw new Exception("First name cannot be empty");

		System.out.println("Enter user lastName");
		String lastName = sc.nextLine();
		if (lastName.isEmpty())
			throw new Exception("Last name cannot be empty");

		System.out.println("Enter user email");

		String email = sc.nextLine();
		if (email.isEmpty())
			throw new Exception("Email cannot be empty");

		if (signUp) {
			System.out.println("Enter user password");
			password = sc.nextLine();
			if (password.isEmpty())
				throw new Exception("Password cannot be empty");
		}

		System.out.println("Enter user mobile");
		String mobile = sc.nextLine();
		if (mobile.isEmpty())
			throw new Exception("Mobile cannot be empty");

		System.out.println("Enter date of birth");
		String birth = sc.nextLine();
		java.util.Date dobUtil = new SimpleDateFormat("dd-MM-yyyy").parse(birth);
		java.sql.Date dob = new java.sql.Date(dobUtil.getTime());

		return new User(0, firstName, lastName, email, password, mobile, dob);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (Scanner sc = new Scanner(System.in)) {
			try (UserDao userdao = new UserDao()) {
				try (ReviewDao reviewdao = new ReviewDao()) {
					try (MovieDao movieDao = new MovieDao()) {
						try (ShareDao shareDao = new ShareDao()) {
							while (true) {
								int loginChoice = loginMenu(sc);
								switch (loginChoice) {
									case 0:// Exit
										System.out.println("Thank You for using our application!");
										return;
									case 1:// Sign in
									{
										System.out.println("Please enter user id");
										int id = sc.nextInt();

										if (userdao.checkIfAvailable(id)) {
											System.out.println("Enter password");
											String pass = sc.next();
											if (userdao.checkIfPasswordAvailable(id, pass))
												System.err.printf("Signed In\n");
											else {
												System.err.println("Password wrong");
												loginChoice = 2;
											}
										} else {
											System.err.println("User not available please Sign up");
											loginChoice = 2;
										}

										while (loginChoice == 1) {

											int choice = menu(sc);
											switch (choice) {
												case 0: // Log Out
													loginChoice = 0;
													break;
												case 1: // Edit Profile
												{
													try {
														User u = addUser(sc, false);
														int rows = userdao.updateUser(u, id);
														// System.err.printf("Query OK : %d rows
														// affected\n", rows);
													} catch (Exception e) {
														System.err.println(e.getMessage());
													}
												}
													break;
												case 2: // Change Password
												{
													System.out.println("Enter new password");
													String password = sc.next();

													final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
													final Pattern pattern = Pattern
															.compile(PASSWORD_PATTERN);
													if (pattern.matcher(password).matches()) {
														int rows = userdao.updateUserPass(id, password);
														System.err.printf("Query OK : %d rows affected\n",
																rows);
													} else
														System.err.println("Invalid password");
												}
													break;
												case 3: // Write a Review
												{
													List<Movie> movies = movieDao.getAllMovies();
													movies.forEach(System.out::println);

													System.out.println("Enter movie id");
													int movieId = sc.nextInt();

													if (reviewdao.movieAvailable(movieId)) {
														System.out.print("Write the review");
														String review = sc.nextLine();
														review += sc.nextLine();
														if (review == null || review.length() == 0) {
															System.err.println("Review cannot be empty");
														} else {
															System.out.println("Enter movie rating");
															int rating = sc.nextInt();
															int rows = reviewdao.insertReview(id,
																	movieId, rating, review);
															System.err.printf(
																	"Query OK : %d rows affected\n",
																	rows);
														}
													} else {
														System.err.println("Movie not available");
													}
												}
													break;
												case 4: // Edit Review
												{
													// printing all users reviews
													List<Review> reviews = reviewdao.getAllMyReviews(id);
													reviews.forEach(System.out::println);

													System.out.println("Enter review Id");
													int reviewId = sc.nextInt();

													List<Integer> listOfreviews = reviewdao
															.checkIfReviewAvailable(id,
																	reviewId);

													if (listOfreviews.contains(reviewId)) {
														Review reviewObj = reviewdao
																.getReviewById(reviewId);

														System.out.println(reviewObj);

														System.out.println("Enter new rating");
														int rating = sc.nextInt();

														System.out.println("Enter new review");
														String review = sc.nextLine();
														review += sc.nextLine();

														if (review == null || review.length() == 0) {
															System.err.println("Review cannot be empty");
														} else {
															System.err.printf("Review Updated: %d\n",
																	reviewdao.editReview(reviewId,
																			review, rating));
														}
													} else {
														System.err.println(
																"You cannot edit others review");
													}

												}
													break;
												case 5: // Display all Movies
												{
													List<Movie> movies = movieDao.getAllMovies();
													movies.forEach(System.out::println);
												}
													break;
												case 6: // Display all Reviews
												{
													List<Review> reviews = reviewdao.getAllReviews();
													reviews.forEach(System.out::println);
												}
													break;
												case 7: // Display my Reviews
												{
													List<Review> reviews = reviewdao.getAllMyReviews(id);
													reviews.forEach(System.out::println);
												}
													break;
												case 8: // Display Reviews shared with me
												{
													List<Review> reviews = reviewdao
															.getAllMyReviewsShared(id);
													reviews.forEach(System.out::println);
												}
													break;
												case 9: // Share a Review
												{
													List<Review> review = reviewdao.getAllMyReviews(id);
													for (Review r : review)
														System.out.print(r);

													System.out.println("Enter the review_id");
													int reviewId = sc.nextInt();

													if (reviewdao.checkIfAvailable(reviewId)) {
														List<User> allUser = userdao.findAllUser();
														allUser.forEach(System.out::println);

														System.out.println(
																"Enter user_ids to share review with (Enter 0 to end)");
														int input = 0;
														List<Integer> users = new ArrayList<>();

														while ((input = sc.nextInt()) != 0) {
															users.add(input);
														}

														if (users.contains(id))
															System.err.println(
																	"You cannot share review with Yourself");
														else
															System.err.printf(
																	"Review shared with %d users succesfully\n",
																	shareDao.shareAReview(users, id,
																			reviewId));

													} else {
														System.err.println("Cannot Share others review");
														break;
													}
												}
													break;
												case 10: // Delete a Review
												{
													System.out.println("Enter the review id");
													int review_id = sc.nextInt();
													int row = reviewdao.deleteReview(id, review_id);
													if (row == -1)
														System.err.println(
																"You cannot delete other users review");
													else
														System.err.printf("Query OK : %d rows affected\n",
																row);
												}
													break;
												default:
													System.err.println("Enter correct choice");
													break;
											}
										}
									}
										break;
									case 2: // Sign up
									{
										try {
											User u = addUser(sc, true);
											System.out.println(u);
											System.err.printf("Query OK : %d rows affected\n",
													userdao.saveUser(u));
										} catch (Exception e) {
											System.err.println(e.getMessage());
										}
									}
										break;
									default:
										System.err.println("Enter correct choice");
										break;
								}
							}
						} // sharedDao.close()
					} // userdao.close();
				} // reviewdao.close();
			} // movieDao.close();
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // sc.close();
	}

}
