package com.meivaldi.wisatatanohgayo;

public class Util {

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double distance = 0;

        int R = 6371;
        double dLat = degToRad(lat2-lat1);
        double dLon = degToRad(lon2-lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        distance = c * R;

        return distance;
    }

    private static double degToRad(double deg) {
        return deg * (Math.PI/180);
    }

}
