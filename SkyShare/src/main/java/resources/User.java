package resources;

public class User {

	public String username;
	public String password;
	public String phoneNumber;
	public int userID;
	public int groupID;

	public User( int userID, String username, String password, String phoneNumber, int groupID) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.groupID = groupID;
	
	}
	
	public User() {
	}
	
	/**
	 * Validates a User object by checking if all the required fields are not null.
	 * 
	 * @param user the User object to be validated
	 * @return true if all the required fields are not null, false otherwise
	 */
	public static boolean validateUser(User user) {
		return user.username != null && user.password != null && user.phoneNumber != null;
	}

}
