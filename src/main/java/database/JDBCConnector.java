package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		// String user = System.getenv("JDBC_USER");
		// String password = System.getenv("JDBC_PASSWORD");
		// String url = System.getenv("JDBC_URL");

		String user = "root";
		String password = "skyshare22";
		String url = "jdbc:mysql://awseb-e-kgshp47exp-stack-awsebrdsdatabase-eqro3xq2jyhg.c7asiigc2cjn.us-west-1.rds.amazonaws.com:3306/ebdb";

		System.out.println("user: " + user + " password: " + password + " url: " + url);
		
		if (url == null || user == null || password == null) {
			throw new SQLException("Database connection information is missing" + "{ user: " + user + " password: "
					+ password + " url: " + url + " }");
		}

		String jdbcURL = url + "?user=" + user + "&password=" + password;

		return DriverManager.getConnection(jdbcURL);
	}

	/**
	 * Adds a new user to the database.
	 *
	 * @param user the user to be added
	 * @return the ID of the newly added user, or -1 if the username already exists,
	 *         or -2 if the phone number already exists
	 */
	public static int addUser(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			// Check if username already exists
			ps = conn.prepareStatement("SELECT * FROM `users` WHERE Username = ?");
			ps.setString(1, user.username);
			rs = ps.executeQuery();
			if (rs.next()) {
				return -1; // Username already exists
			}

			// Check if phone number already exists
			ps = conn.prepareStatement("SELECT * FROM `users` WHERE PhoneNumber = ?");
			ps.setString(1, user.phoneNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				return -2; // Phone number already exists
			}

			// Insert new user
			ps = conn.prepareStatement(
					"INSERT INTO `users` (Username, Password, PhoneNumber, GroupID) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.username);
			ps.setString(2, user.password);
			ps.setString(3, user.phoneNumber);
			ps.setInt(4, user.groupID);
			ps.executeUpdate();

			rs = ps.executeQuery("SELECT LAST_INSERT_ID()");

			if (rs.next()) {
				user.userID = rs.getInt(1);
			}

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
			ps = conn.prepareStatement("SELECT * FROM `users` WHERE Username = ? AND Password = ?");
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

			ps = conn.prepareStatement(
					"SELECT g.*, u.* FROM `users` u RIGHT JOIN `groups` g ON u.GroupID = g.GroupID WHERE g.GroupID > 0");
			rs = ps.executeQuery();

			Map<Integer, Group> groupMap = new HashMap<>();

			while (rs.next()) {
				int groupID = rs.getInt("GroupID");
				Timestamp departureTime = rs.getTimestamp("DepartureTime");
				String pickupLocation = rs.getString("PickupLocation");

				if (!groupMap.containsKey(groupID)) {
					Group group = new Group(groupID, departureTime.toString(), pickupLocation);
					group.users = new ArrayList<>();
					groupMap.put(groupID, group);
				}

				int userID = rs.getInt("UserID");
				if (!rs.wasNull()) {
					String username = rs.getString("Username");
					String password = rs.getString("Password");
					String phoneNumber = rs.getString("PhoneNumber");
					User user = new User(userID, username, password, phoneNumber, groupID);
					groupMap.get(groupID).users.add(user);
				}
			}

			groups = new ArrayList<>(groupMap.values());

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
	static public int addGroup(Group group) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement("INSERT INTO `groups` (DepartureTime, PickupLocation) VALUES (?, ?)");
			ps.setString(1, group.departureTime);
			ps.setString(2, group.pickupLocation);
			ps.executeUpdate();

			rs = ps.executeQuery("SELECT LAST_INSERT_ID()");

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

		System.out.println("Attempt User " + userID + " joined group " + groupID);

		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement("UPDATE `users` SET GroupID = ? WHERE UserID = ?");
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
				if (ps2 != null) {
					ps2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("sqle: " + e.getMessage());
			}
		}
		System.out.println("Finish User " + userID + " joined group " + groupID);

	}

	/**
	 * Removes a user from a group.
	 *
	 * @param userID the ID of the user
	 */
	public static void leaveGroup(int userID, int groupID) {

		System.out.println("Attempt User " + userID + " leave group " + groupID);

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = getConnection();

			ps = conn.prepareStatement("UPDATE `users` SET GroupID = -1 WHERE UserID = ?");
			ps.setInt(1, userID);
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

		System.out.println("Finish User " + userID + " leave group " + groupID);

	}

	/**
	 * Returns a list of groups that match the specified departure time and pickup
	 * location.
	 *
	 * @param departureTime  the departure time of the groups
	 * @param pickupLocation the pickup location of the groups
	 * @return the list of groups that match the specified departure time and pickup
	 *         location
	 */
	static public ArrayList<Group> getFilteredGroups(Timestamp departureTime, String pickupLocation) {
		ArrayList<Group> groups = getGroups();

		System.out.print("groups" + groups.toString());
		MatchingAlgo.match(groups, departureTime, pickupLocation);

		return groups;
	}

	/**
	 * Returns a list of all users and groups in the database.
	 *
	 * @return a map containing the list of users and groups
	 */
	public static Map<String, Object> getAllData() {
		Map<String, Object> data = new HashMap<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			// Retrieve users
			ps = conn.prepareStatement("SELECT * FROM `users`");
			rs = ps.executeQuery();
			List<User> users = new ArrayList<>();
			while (rs.next()) {
				int userID = rs.getInt("UserID");
				String username = rs.getString("Username");
				String password = rs.getString("Password");
				String phoneNumber = rs.getString("PhoneNumber");
				// int groupID = rs.getInt("GroupID");
				int groupID = -1;
				User user = new User(userID, username, password, phoneNumber, groupID);
				users.add(user);
			}
			data.put("users", users);

			// Retrieve groups
			ps = conn.prepareStatement("SELECT * FROM `groups`");
			rs = ps.executeQuery();
			List<Group> groups = new ArrayList<>();
			while (rs.next()) {
				int groupID = rs.getInt("GroupID");
				Timestamp departureTime = rs.getTimestamp("DepartureTime");
				String pickupLocation = rs.getString("PickupLocation");
				Group group = new Group(groupID, departureTime.toString(), pickupLocation);
				groups.add(group);
			}
			data.put("groups", groups);
		} catch (SQLException e) {
			System.out.println("SQL Exception in getAllData. ");
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
		return data;
	}
}