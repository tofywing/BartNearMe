package googleapi1.yee.interview.bartnearme;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Yee on 2/10/16.
 */
public class StationDetailsManager {
    public static final String TAG = "stationDetailedFragment";
    public static final String NAME = "name";
    public static final String DISTANCE = "distance";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIP = "zip";
    public SharedPreferences mSharedPreferences;

    public StationDetailsManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public void saveStationDetails(Station station) {
        if (station != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(NAME, station.getName());
            editor.putString(DISTANCE, station.getDistance());
            editor.putString(ADDRESS, station.getAddress());
            editor.putString(CITY, station.getCity());
            editor.putString(STATE, station.getState());
            editor.putString(ZIP, station.getZipCode());
            editor.apply();
        } else {
            Log.i("not save", "station is null");
        }
    }

    public Station getSavedStation() {
        if (mSharedPreferences.contains(NAME)) {
            Station station = new Station();
            station.setName(mSharedPreferences.getString(NAME, ""));
            station.setDistance(mSharedPreferences.getString(DISTANCE, ""));
            station.setAddress(mSharedPreferences.getString(ADDRESS, ""));
            station.setCity(mSharedPreferences.getString(CITY, ""));
            station.setState(mSharedPreferences.getString(STATE, ""));
            station.setZipCode(mSharedPreferences.getString(ZIP, ""));
            return station;
        }
        return null;
    }
}
