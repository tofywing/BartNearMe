package googleapi1.yee.interview.bartnearme;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Yee on 2/10/16.
 */
public class StationManager {
    List<Station> mStations;
    int size;
    Map<Double, Integer> mCloseStationMap = new TreeMap<>();


    public StationManager(List<Station> stations) {
        mStations = stations;
        size = stations.size();
    }

    public double getDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }


//    public double getDistance(LatLng start, LatLng end) {
//        double startLat = start.latitude;
//        double startLng = start.longitude;
//        double endLat = end.latitude;
//        double endLng = end.longitude;
//        Location startLocation = new Location("Start");
//        startLocation.setLatitude(startLat);
//        startLocation.setLongitude(startLng);
//        Location endLocation = new Location("End");
//        endLocation.setLatitude(endLat);
//        endLocation.setLongitude(endLng);
//        return startLocation.distanceTo(endLocation);
//    }

    public List<Station> getCloseStationInCount(LatLng start, int count) {
        count = Math.min(count, size);
        List<Station> resultList = new ArrayList<>();
        LatLng end;
        for (int i = 0; i < size; i++) {
            end = new LatLng(Double.parseDouble(mStations.get(i).getLatitude()), Double.parseDouble(mStations.get(i)
                    .getLongitude()));
            mCloseStationMap.put(getDistance(start, end), i);
        }

        for (Map.Entry<Double, Integer> map : mCloseStationMap.entrySet()) {
            if (count == 0) break;
            Station station = mStations.get(map.getValue());
            station.setDistance(getDistanceInKM(map.getKey()));
            //station.setDistance(String.valueOf(map.getKey()));
            resultList.add(station);
            count--;
        }
        return resultList;
    }

    String getDistanceInKM(double distance) {
        return String.format("%.2f km", distance);
    }
}
