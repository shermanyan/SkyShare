package resources;

import java.sql.*;
import java.util.ArrayList;

public class MatchingAlgo {

    /**
     * Matches groups based on departure time and pickup location.
     * 
     * @param groups         the list of groups to be matched
     * @param departureTime  the departure time to be matched
     * @param pickupLocation the pickup location to be matched
     */
    public static void match(ArrayList<Group> groups, Timestamp departureTime, String pickupLocation) {
        // Looping through the groups
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            if (Timestamp.valueOf(group.departureTime).after(departureTime) || !group.pickupLocation.equals(pickupLocation)) {
                groups.remove(i);
                i--;
            }
        }
    }

}
