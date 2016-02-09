package googleapi1.yee.interview.bartnearyou;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import googleapi1.yee.interview.bartnearyou.Fragment.GoogleMapFragment;

/**
 * Created by Yee on 2/8/16.
 */
public class MapStateManager {

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String TILT = "tilt";
    public static final String BEARING = "bearing";
    public static final String GOOGLE_MAP_STATE = "googleMapState";
    public static final String ME_PRESSED = "mePressed";
    public static final String BART_PRESSED = "bartPressed";
    public static final String MAP_TYPE = "mapType";
    public static final String ZOOM = "zoom";
    private SharedPreferences mapStatePrefs;

    public MapStateManager(Context context) {
        mapStatePrefs = context.getSharedPreferences(GOOGLE_MAP_STATE, Context.MODE_PRIVATE);
    }

    public void saveMapState(GoogleMap map) {
        SharedPreferences.Editor edit = mapStatePrefs.edit();
        CameraPosition position = map.getCameraPosition();
        edit.putFloat(LATITUDE, (float) position.target.latitude);
        edit.putFloat(LONGITUDE, (float) position.target.longitude);
        edit.putFloat(TILT, position.tilt);
        edit.putFloat(BEARING, position.bearing);
        edit.putFloat(ZOOM, position.zoom);
        edit.putInt(MAP_TYPE, map.getMapType());
        edit.apply();
    }

    public CameraPosition getSavedCameraPosition() {
        double lat = mapStatePrefs.getFloat(LATITUDE, 0);
        double lng = mapStatePrefs.getFloat(LONGITUDE, 0);
        if (lat == 0 || lng == 0) return null;
        LatLng ll = new LatLng(lat, lng);
        float zoom = mapStatePrefs.getFloat(ZOOM, GoogleMapFragment.DEFAULT_ZOOM);
        float tilt = mapStatePrefs.getFloat(TILT, 0);
        float bearing = mapStatePrefs.getFloat(BEARING, 0);
        return new CameraPosition(ll, zoom, tilt, bearing);
    }


}
