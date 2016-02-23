package googlemapapi.yee.interview.bartnearme_1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import googlemapapi.yee.interview.bartnearme_1.Data.Station;

/**
 * Created by Yee on 2/10/16.
 */
public class StationDetailsManager {
    public static final String STATION_STATE = "stationDetailedFragment";
    public static final String NAME = "name";
    public static final String DISTANCE = "distance";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIP = "zip";
    public static final String TEMP_HIGH = "tempHigh";
    public static final String TEMP_LOW = "tempLow";
    public static final String TEMP_IN_GENERAL = "tempInGeneral";
    public static final String CODE = "code";

    public SharedPreferences mSharedPreferences;

    public StationDetailsManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(STATION_STATE, Context.MODE_PRIVATE);
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
            editor.putString(TEMP_HIGH, station.getTempHigh());
            editor.putString(TEMP_LOW, station.getTempLow());
            editor.putString(TEMP_IN_GENERAL, station.getTempInGeneral());
            editor.putInt(CODE, station.getCode());
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
            station.setTempHigh(mSharedPreferences.getString(TEMP_HIGH, ""));
            station.setTempLow(mSharedPreferences.getString(TEMP_LOW, ""));
            station.setTempInGeneral(mSharedPreferences.getString(TEMP_IN_GENERAL, ""));
            station.setCode(mSharedPreferences.getInt(CODE, 0));
            return station;
        }
        return null;
    }
}
