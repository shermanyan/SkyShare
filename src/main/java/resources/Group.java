package resources;

import java.util.ArrayList;


public class Group {
    public int groupID;
    public String departureTime;
    public String pickupLocation;
    public ArrayList<User> users;

    public Group(int groupID, String departureTime, String pickupLocation) {
        this.groupID = groupID;
        this.departureTime = departureTime;
        this.pickupLocation = pickupLocation;
    }
    
}