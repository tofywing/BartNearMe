package googlemapapi.yee.interview.bartnearme_1;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;

/**
 * Created by Yee on 2/21/16.
 */
public class MapPositionManager {

    double latitude;
    double longitude;
    String iconPath;
    String locality;
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ICON_PATH = "iconPath";
    public static final String LOCALITY = "locality";
    public static final String MAP_POSITION = "mapPosition";
    private SharedPreferences mPositionPrefs;

    public MapPositionManager(Context context) {
        mPositionPrefs = context.getSharedPreferences(MAP_POSITION, Context.MODE_PRIVATE);
    }

    public void saveMapPosition(Address address, String iconPath) {
        SharedPreferences.Editor editor = mPositionPrefs.edit();
        editor.putFloat(LATITUDE, (float) address.getLatitude());
        editor.putFloat(LONGITUDE, (float) address.getLongitude());
        editor.putString(LOCALITY, address.getLocality());
        editor.putString(ICON_PATH, iconPath);
        editor.apply();
    }

    public double getLatitude() {
        return mPositionPrefs.getFloat(LATITUDE, 0);
    }

    public double getLongitude() {
        return mPositionPrefs.getFloat(LONGITUDE, 0);
    }

    public String getLocality() {
        return mPositionPrefs.getString(LOCALITY, "");
    }

    public String getIconPath() {
        return mPositionPrefs.getString(ICON_PATH, "");
    }
}
