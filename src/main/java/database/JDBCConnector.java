package database;

import java.sql.*;
import java.util.ArrayList;

import resources.*;

/**
 * The JDBCConnector class provides methods to connect to a MySQL database and
 * perform user-related operations.
 */
public class JDBCConnector {

	/**
	 * Returns a connection to the MySQL database.
	 *
	 * @return the database connection
	 * @throws SQLException 
	 */
	private static Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return DriverManager.getConnection("jdbc:mysql://localhost/SkyShare?user=root&password=shermany");
	}

	/**
	 * Adds a new user to the database.
	 *
	 * @param user the user to be added
	 * @return the ID of the newly added user, or -1 if the username already exists,
	 *         or -2 if the phone number already exists
	 */
	public static int addUser(User user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			// Check if username already exists
			ps = conn.prepareStatement("SELECT * FROM SkyShare.users WHERE Username = ?");
			ps.setString(1, user.username);
			rs = ps.executeQuery();
			if (rs.next()) {
				return -1; // Username already exists
			}
			rs.close();
			ps.close();

			// Check if phone number already exists
			ps = conn.prepareStatement("SELECT * FROM SkyShare.users WHERE PhoneNumber = ?");
			ps.setString(1, user.phoneNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				return -2; // Phone number already exists
			}
			rs.close();
			ps.close();

			// Insert new user
			ps = conn.prepareStatement(
					"INSERT INTO SkyShare.users (Username, Password, PhoneNumber, GroupID) VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(3, user.username);
			ps.setString(4, user.password);
			ps.setString(5, user.phoneNumber);
			ps.setInt(6, user.groupID);
			ps.executeUpdate();

			if (rs.next()) {
				user.userID = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("SQL Exception in sign up. ");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("sqle: " + e.getMessage());
			}
		}

		return user.userID;
	}

	/**
	 * Logs in a user with the specified username and password.
	 *
	 * @param username the username of the user
	 * @param password the password of the user
	 * @return the User object representing the logged-in user, or an empty User
	 *         object if the login fails
	 */
	public static User loginUser(String username, String password) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = new User();

		try {
			conn = getConnection();
			ps = conn.prepareStatement("SELECT * FROM SkyShare.users WHERE Username = ? AND Password = ?");
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();

			if (rs.next()) {
				user.username = rs.getString("Username");
				user.password = rs.getString("Password");
				user.phoneNumber = rs.getString("PhoneNumber");
				user.userID = rs.getInt("UserID");
				user.groupID = rs.getInt("GroupID");
			}

		} catch (SQLException e) {
			System.out.println("SQL Exception in login. ");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("sqle: " + e.getMessage());
			}
		}

		return user;
	}

	/**
	 * Returns a list of all groups in the database.
	 *
	 * @return the list of groups
	 */
	public static ArrayList<Group> getGroups() {
		ArrayList<Group> groups = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement("SELECT * FROM SkyShare.groups");
			rs = ps.executeQuery();

			while (rs.next()) {
				int groupID = rs.getInt("GroupID");
				Timestamp departureTime = rs.getTimestamp("DepartureTime");
				String pickupLocation = rs.getString("PickupLocation");
				groups.add(new Group(groupID, departureTime, pickupLocation));
			}

		} catch (SQLException e) {
			System.out.println("SQL Exception in getGroups. ");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("sqle: " + e.getMessage());
			}
		}

		return groups;
	}

	/**
	 * Adds a new group to the database.
	 *
	 * @param group the group to be added
	 * @return the ID of the newly added group
	 */
	static public int addGroup(Timestamp departureTime, String pickupLocation) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Group group = new Group(0, departureTime, pickupLocation);

		try {
			conn = getConnection();

			ps = conn.prepareStatement("INSERT INTO SkyShare.groups (DepartureTime, PickupLocation) VALUES (?, ?)");
			ps.setTimestamp(1, group.departureTime);
			ps.setString(2, group.pickupLocation);
			rs = ps.executeQuery();

			if (rs.next()) {
				group.groupID = rs.getInt(1);
			}

		} catch (SQLException e) {
			System.out.println("SQL Exception in addGroup. ");
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("sqle: " + e.getMessage());
			}
		}

		return group.groupID;
	}

	/**
	 * Adds a user to a group.
	 *
	 * @param userID  the ID of the user
	 * @param groupID the ID of the group
	 */
	public static void joinGroup(int userID, int groupID) {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement("UPDATE SkyShare.users SET GroupID = ? WHERE UserID = ?");
			ps.setInt(1, groupID);
			ps.setInt(2, userID);
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQL Exception in joinGroup. ");
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("sqle: " + e.getMessage());
			}
		}
	}

	/**
	 * Removes a user from a group.
	 *
	 * @param userID the ID of the user
	 */
	public static void leaveGroup(int userID, int groupID) {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement("UPDATE SkyShare.users SET GroupID = NULL WHERE UserID = ? AND GroupID = ?");
			ps.setInt(1, userID);
			ps.setInt(2, groupID);
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQL Exception in leaveGroup. ");
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("sqle: " + e.getMessage());
			}
		}
	}

	/**
	 * Returns a list of groups that match the specified departure time and pickup
	 * location.
	 *
	 * @param departureTime the departure time of the groups
	 * @param pickupLocation the pickup location of the groups
	 * @return the list of groups that match the specified departure time and pickup
	 *         location
	 */
	static public ArrayList<Group> getFilteredGroups(Timestamp departureTime, String pickupLocation) {
		ArrayList<Group> groups = getGroups();

		MatchingAlgo.match(groups, departureTime, pickupLocation);
	
		return groups;
	}

}