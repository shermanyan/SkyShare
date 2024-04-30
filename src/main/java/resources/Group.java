package resources;

import java.sql.Timestamp;


public class Group {
    public int groupID;
    public Timestamp departureTime;
    public String pickupLocation;

    public Group(int groupID, Timestamp departureTime, String pickupLocation) {
        this.groupID = groupID;
        this.departureTime = departureTime;
        this.pickupLocation = pickupLocation;
    }
    
}