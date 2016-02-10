package googleapi1.yee.interview.bartnearme;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
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
    Map<Integer, Integer> mCloseStationMap = new TreeMap<>();


    public StationManager(List<Station> stations) {
        mStations = stations;
        size = stations.size();
    }

    public int getDistance(LatLng StartP, LatLng EndP) {
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
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return (int) (Radius * c);
    }

    public List<Station> getCloseStation(LatLng start, int count) {
        count = Math.min(count, size);
        List<Station> resultList = new ArrayList<>();
        LatLng end;
        for (int i = 0; i < size; i++) {
            end = new LatLng(Double.parseDouble(mStations.get(i).getLatitude()), Double.parseDouble(mStations.get(i)
                    .getLongitude()));
            mCloseStationMap.put(i, getDistance(start, end));
        }
        for (int i = 1; i <= count; i++) {
            resultList.add(mStations.get(mCloseStationMap.get(i)));
        }
        return resultList;
    }
}
