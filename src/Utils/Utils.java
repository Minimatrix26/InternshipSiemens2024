package Utils;

import static java.lang.Math.*;

public class Utils {

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // calculating distance between 2 points using Haversine formula
        final int R = 6371; // radius of earth

        double latDistance = toRadians(lat2 - lat1);
        double lonDistance = toRadians(lon2 - lon1);
        double a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                        sin(lonDistance / 2) * sin(lonDistance / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return R * c * 1000; // to convert to meters
    }
}
