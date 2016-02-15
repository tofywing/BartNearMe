package googleapi1.yee.interview.bartnearme.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import googleapi1.yee.interview.bartnearme.CallBack.ServiceCallBack;
import googleapi1.yee.interview.bartnearme.MapStateManager;
import googleapi1.yee.interview.bartnearme.R;
import googleapi1.yee.interview.bartnearme.Service.BartService;
import googleapi1.yee.interview.bartnearme.Station;
import googleapi1.yee.interview.bartnearme.StationManager;

/**
 * Created by Yee on 2/5/16.
 */
public class GoogleMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, ServiceCallBack {

    public static final float DEFAULT_ZOOM = 15;
    public static final float STATION_ZOOM = 9.5f;
    public static final String CURRENT_LOCALITY = "current";
    public static final String INPUT = "input";
    public static final String ME = "me";
    public static final String BART = "bart";
    private static final String CURRENT_LOCATION = "currentLocation";
    private static final String LAST_UPDATE_TIME = "updateTime";
    private static final int GPS_ERROR_DIALOG_REQUEST = 9001;
    EditText mMapInput;
    FloatingActionButton mMeButton;
    FloatingActionButton mBartButton;
    ProgressDialog mDialog;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    Marker mMarker;
    String mLastUpdateTime;
    //city
    String mLocality;
    StationListFragment mListFragment;
    BartService mService;
    FragmentManager mManager;
    //TODO: KEY CHECKING
    boolean mMePressed = true;
    boolean mBartPressed = false;
    SharedPreferences mPreference;

    int[][] colorPattern = new int[][]{new int[]{Color.parseColor("#FF4081")}, new int[]{Color.parseColor("#366792")}};
    ColorStateList pink = new ColorStateList(colorPattern, colorPattern[0]);
    ColorStateList blue = new ColorStateList(colorPattern, colorPattern[1]);
    LatLng mLatLng;
    List<Marker> mMarkerList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (serviceAvailable()) {
            view = inflater.inflate(R.layout.google_map_fragment, container, false);
            initMap();
        } else {
            Toast.makeText(getActivity(), getText(R.string.service_unavailable), Toast.LENGTH_LONG).show();
            onDestroy();
            return null;
        }

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_LOCATION)) mCurrentLocation = savedInstanceState.getParcelable
                    (CURRENT_LOCATION);
            if (savedInstanceState.containsKey(LAST_UPDATE_TIME)) mLastUpdateTime = savedInstanceState.getString
                    (LAST_UPDATE_TIME);
            if (savedInstanceState.containsKey(CURRENT_LOCALITY))
                mLocality = savedInstanceState.getString(CURRENT_LOCALITY);
        }
        mService = new BartService(getActivity(), this);
        //TODO
        mMapInput = (EditText) getActivity().findViewById(R.id.mapInput);
        mPreference = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (mPreference.contains(INPUT)) mMapInput.setText(mPreference.getString(INPUT, ""));
        mMapInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    try {
                        geoLocate(view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        mMeButton = (FloatingActionButton) getActivity().findViewById(R.id.findMe);
        mMeButton.setBackgroundTintList(pink);
        mMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBartPressed = false;
                try {
                    goToCurrentLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mBartButton = (FloatingActionButton) getActivity().findViewById(R.id.findBart);
        mBartButton.setBackgroundTintList(blue);
        mListFragment = new StationListFragment();
        mManager = getActivity().getFragmentManager();
        mBartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.getStationInfo();
                mBartPressed = true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        MapStateManager manager = new MapStateManager(getActivity());
        manager.saveMapState(mMap);
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(INPUT, mMapInput.getText().toString());
        editor.putBoolean(ME, mMePressed);
        editor.putBoolean(BART, mBartPressed);
        editor.apply();
    }

    private boolean serviceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getActivity());
        if (isAvailable == ConnectionResult.SUCCESS) return true;
        else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(getActivity(), isAvailable, GPS_ERROR_DIALOG_REQUEST);
            onDestroy();
            dialog.show();
        } else {
            Snackbar.make(getView(), getText(R.string.service_unavailable), Snackbar.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initMap() {
        showDialog(getActivity());
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addConnectionCallbacks
                        (this).addOnConnectionFailedListener(this).build();
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id
                .mapFragment);
        fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                if (map != null) {
                    Snackbar.make(getView(), getText(R.string.service_ready), Snackbar
                            .LENGTH_SHORT).show();
                    mMap = map;
                    mDialog.dismiss();
                } else {
                    Snackbar.make(getView(), getText(R.string.service_ready), Snackbar
                            .LENGTH_SHORT).show();
                }

            }
        });
    }

    private void goToLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        mLatLng = ll;
        mCurrentLocation = new Location("");
        mCurrentLocation.setLatitude(lat);
        mCurrentLocation.setLatitude(lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.animateCamera(update);
    }

    private void goToCurrentLocation() throws IOException {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest
                .permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mCurrentLocation != null) {
            Geocoder gc = new Geocoder(getActivity());
            double lat = mCurrentLocation.getLatitude();
            double lng = mCurrentLocation.getLongitude();
            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
            mLocality = addresses.get(0).getLocality();
            setStartMarker(mLocality, lat, lng);
            LatLng ll = new LatLng(lat, lng);
            mLatLng = ll;
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULT_ZOOM);
            mMap.animateCamera(update);
        } else {
            Snackbar.make(getView(), getText(R.string.last_location_failed), Snackbar.LENGTH_LONG).show();
        }
    }

    public void geoLocate(View view) throws IOException {
        hideSoftKeyBoard(view);
        String location = mMapInput.getText().toString();
        Geocoder gc = new Geocoder(getActivity());
        List<Address> list = gc.getFromLocationName(location, 1);
        if (list.size() != 0) {
            Address address = list.get(0);
            if (address != null) {
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                goToLocation(lat, lng, DEFAULT_ZOOM);
                mLocality = address.getLocality();
                setStartMarker(mLocality, lat, lng);
            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(getText(R.string.alter_wrong_input));
            alert.setCancelable(false);
            alert.setNegativeButton(getText(R.string.alter_button_comfirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    private void hideSoftKeyBoard(View view) {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    void showDialog(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        showDialog(getActivity());
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Google suggest production to update location in 60s to save the battery
        mLocationRequest.setInterval(50000);
        //If another app is trying to use the location will get it in interval of 10s
        mLocationRequest.setFastestInterval(10000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager
                        .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google
                .android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("LatLng", "Location:" + location.getLatitude() + location.getLongitude());
            }
        });
        MapStateManager manager = new MapStateManager(getActivity());
        CameraPosition position = manager.getSavedCameraPosition();
        if (position != null) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
            double lat = manager.getLatitude();
            double lng = manager.getLongitude();
            mLatLng = new LatLng(lat, lng);
            setStartMarker(mLocality, lat, lng);
            mService.getStationInfo();
        } else {
            try {
                goToCurrentLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mDialog.dismiss();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CURRENT_LOCATION, mCurrentLocation);
        outState.putString(LAST_UPDATE_TIME, mLastUpdateTime);
        outState.putString(CURRENT_LOCALITY, mLocality);
        super.onSaveInstanceState(outState);
    }

    void setStartMarker(String locality, double lat, double lng) {
        if (mMarker != null) {
            mMarker.remove();
        }
        MarkerOptions options = new MarkerOptions().title(locality).position(new LatLng(lat, lng)).icon
                (BitmapDescriptorFactory.fromResource(R.drawable.small_bart_head));
        mMarker = mMap.addMarker(options);
    }

    void setEndMarker(List<Station> stationList) {
        if (mMarkerList != null) {
            for (Marker marker : mMarkerList) {
                marker.remove();
            }
        }
        mMarkerList = new LinkedList<>();
        Marker marker;
        MarkerOptions options;
        for (Station station : stationList) {
            options = new MarkerOptions().title(station.getName()).position(new LatLng(Double
                    .parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude()))).icon
                    (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            marker = mMap.addMarker(options);
            mMarkerList.add(marker);
        }
    }

    @Override
    public void onActionSuccess(List<Station> list, ProgressDialog dialog) {
        StationManager stationManager = new StationManager(list);
        Station.setData(stationManager.getCloseStationsInCount(mLatLng, 5));
        if (mListFragment != null && mListFragment.isAdded())
            mManager.beginTransaction().remove(mListFragment).commit();
        mListFragment = new StationListFragment();
        mManager.beginTransaction().add(R.id.stationList, mListFragment).commit();
        //Check the Bart Key press state
        if (mPreference.getBoolean(BART, false) || mBartPressed) {
            setEndMarker(Station.data);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mLatLng, STATION_ZOOM);
            mMap.animateCamera(update);
        }
        dialog.dismiss();
    }

    @Override
    public void onActionFailed(Exception e, ProgressDialog dialog) {
        dialog.dismiss();
    }
}

