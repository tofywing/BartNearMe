package googlemapapi.yee.interview.bartnearme_1.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import googlemapapi.yee.interview.bartnearme_1.CallBack.BartServiceCallBack;
import googlemapapi.yee.interview.bartnearme_1.CallBack.ImageServiceCallBack;
import googlemapapi.yee.interview.bartnearme_1.CallBack.YahooWeatherServiceCallBack;
import googlemapapi.yee.interview.bartnearme_1.Data.Channel;
import googlemapapi.yee.interview.bartnearme_1.Data.Forecast;
import googlemapapi.yee.interview.bartnearme_1.MainActivity;
import googlemapapi.yee.interview.bartnearme_1.MapPositionManager;
import googlemapapi.yee.interview.bartnearme_1.MapStateManager;
import googleapi1.yee.interview.bartnearme_1.R;
import googlemapapi.yee.interview.bartnearme_1.Service.BartService;
import googlemapapi.yee.interview.bartnearme_1.Data.Station;
import googlemapapi.yee.interview.bartnearme_1.Service.ImageService;
import googlemapapi.yee.interview.bartnearme_1.Service.WeatherService;
import googlemapapi.yee.interview.bartnearme_1.StationManager;

/**
 * Created by Yee on 2/5/16.
 */
public class GoogleMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, BartServiceCallBack, YahooWeatherServiceCallBack, ImageServiceCallBack {

    public static final float DEFAULT_ZOOM = 15;
    public static final float STATION_ZOOM = 9.5f;
    public static final String CURRENT_LOCALITY = "current";
    public static final String INPUT = "input";
    public static final String ME = "me";
    public static final String BART = "bart";
    public static final String BACK_ME = "BackTome";
    public static final String BACK_BART = "BackTobart";
    private static final String CURRENT_LOCATION = "currentLocation";
    private static final String LAST_UPDATE_TIME = "updateTime";
    private static final String LOGGED_IN = "loggedIn";
    private static final String PREVIOUS_LATITUDE = "previousLatitude";
    private static final String PREVIOUS_LONGITUDE = "previousLongitude";
    private static final String PREVIOUS_ZOOM = "previousZoom";
    private static final String PREVIOUS_LOCALITY = "previousLocality";

    //TODO:
    private static final String ICON_PATH = "iconPath";
    private static final int GPS_ERROR_DIALOG_REQUEST = 9001;
    private static final int STATION_REQUIRED = 5;
    private static final int SATISFY_DISTANCE = 25;
    private static final String DEFAULT_LOCALITY = "N/A";
    EditText mMapInput;
    FloatingActionButton mMeButton;
    FloatingActionButton mBartButton;
    FloatingActionButton mBackButton;
    LoginButton mLogin;
    CallbackManager mFacebookCallBackManager;
    ProgressDialog mDialog;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    //    float previousLatitude = -1f;
//    float previousLongitude = -1f;
//    float previousZoom = STATION_ZOOM;
    String previousLocality = DEFAULT_LOCALITY;
    Marker mMarker;
    //city
    String mLocality;

    static Bitmap mFacebookAvatar;
    boolean isLoggedIn;

    Station mStation;
    int mIndex = 0;

    StationListFragment mListFragment;
    BartService mBartService;
    WeatherService mWeatherService;
    ImageService mImageService;
    FragmentManager mManager;
    MapPositionManager mPositionManager;
    //TODO: KEY CHECKING
    boolean isMePressed = true;
    boolean isBartPressed = false;
    boolean isBackToMe = true;
    boolean isBackToBart = false;
    SharedPreferences mPreference;
    SharedPreferences.Editor mEditor;

    int[][] colorPattern = new int[][]{new int[]{Color.parseColor("#FF4081")}, new int[]{Color.parseColor("#366792")
    }, new int[]{Color.parseColor("#F0EB5A")}};
    ColorStateList pink = new ColorStateList(colorPattern, colorPattern[0]);
    ColorStateList blue = new ColorStateList(colorPattern, colorPattern[1]);
    ColorStateList yellow = new ColorStateList(colorPattern, colorPattern[2]);
    LatLng mLatLng;
    List<Marker> mMarkerList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view;
        FacebookSdk.sdkInitialize(getContext());
        mFacebookCallBackManager = CallbackManager.Factory.create();
        view = inflater.inflate(R.layout.google_map_fragment, container, false);
        if (serviceAvailable()) {
            initMap();
        } else {
            Toast.makeText(getActivity(), getText(R.string.service_unavailable), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBartService = new BartService(getActivity(), this);
        mWeatherService = new WeatherService(getActivity(), this);
        mImageService = new ImageService(getActivity(), this);
        mPreference = getActivity().getPreferences(Context.MODE_PRIVATE);
        mPositionManager = new MapPositionManager(getActivity());
        if (mPreference.contains(LOGGED_IN)) isLoggedIn = mPreference.getBoolean(LOGGED_IN, false);
//        if (mPreference.contains(PREVIOUS_LATITUDE)) previousLatitude = mPreference.getFloat(PREVIOUS_LATITUDE, 0);
//        if (mPreference.contains(PREVIOUS_LONGITUDE)) previousLongitude = mPreference.getFloat(PREVIOUS_LONGITUDE, 0);
//        if (mPreference.contains(PREVIOUS_ZOOM)) previousZoom = mPreference.getFloat(PREVIOUS_ZOOM, STATION_ZOOM);
        if (mPreference.contains(PREVIOUS_LOCALITY)) previousLocality = mPreference.getString(PREVIOUS_LOCALITY,
                DEFAULT_LOCALITY);
        mLogin = (LoginButton) view.findViewById(R.id.login_button);
        mLogin.setReadPermissions("public_profile");
        mLogin.setFragment(this);
        mLogin.registerCallback(mFacebookCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                isLoggedIn = true;
                mImageService.getImageInfo(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn) {
                    isLoggedIn = false;
                    mMarker.setIcon(BitmapDescriptorFactory.defaultMarker());
                }
            }
        });
        //TODO
        mMapInput = (EditText) view.findViewById(R.id.mapInput);
        if (mPreference.contains(INPUT)) mMapInput.setText(mPreference.getString(INPUT, ""));
        mMapInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    try {
                        hideSoftKeyBoard(view);
                        String location = mMapInput.getText().toString();
                        Geocoder gc = new Geocoder(getActivity());
                        List<Address> list = gc.getFromLocationName(location, 1);
                        if (list.size() != 0) {
                            geoLocate(list.get(0));
                            isBackToMe = true;
                            isBackToBart = false;
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setMessage(getText(R.string.alert_wrong_input));
                            alert.setCancelable(false);
                            alert.setNegativeButton(getText(R.string.alert_button_comfirm), new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        mMeButton = (FloatingActionButton) view.findViewById(R.id.findMe);

        mMeButton.setBackgroundTintList(pink);
        mMeButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             isBartPressed = false;
                                             isBackToMe = true;
                                             isBackToBart = false;
                                             try {
                                                 goToCurrentLocation();
                                             } catch (IOException e) {
                                                 e.printStackTrace();
                                             }
                                         }
                                     }
        );
        mBartButton = (FloatingActionButton) view.findViewById(R.id.findBart);

        mBartButton.setBackgroundTintList(blue);
        mListFragment = StationListFragment.newInstance(null);
        mManager = getActivity().getFragmentManager();
        mBartButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               mBartService.getStationInfo();
                                               isBartPressed = true;
                                               isBackToBart = true;
                                               isBackToMe = false;
                                           }
                                       }
        );
        mBackButton = (FloatingActionButton) view.findViewById(R.id.back);
        mBackButton.setBackgroundTintList(yellow);
        mBackButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {

//                                               MainActivity.makeToast(getActivity(),String.valueOf(previousLatitude)+
//                                                       String.valueOf(previousLongitude)+String.valueOf
// (previousZoom));
//
//                                               if (previousLatitude != -1 && previousLongitude != -1) {
//                                                   setStartMarker(previousLocality,previousLatitude,
// previousLongitude);
//                                                   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
//                                                           LatLng(previousLatitude, previousLongitude), previousZoom);
//                                                   mMap.animateCamera(cameraUpdate);
//                                               }

                                               CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
                                                       LatLng(mPositionManager.getLatitude
                                                       (), mPositionManager.getLongitude()), isBackToMe ?
                                                       DEFAULT_ZOOM : STATION_ZOOM);
                                               mMap.animateCamera(cameraUpdate);
                                           }
                                       }
        );
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
        mEditor = mPreference.edit();
        mEditor.putString(INPUT, mMapInput.getText().toString());
        mEditor.putBoolean(ME, isMePressed);
        mEditor.putBoolean(BART, isBartPressed);
        mEditor.putBoolean(BACK_ME, isBackToMe);
        mEditor.putBoolean(BACK_BART, isBackToBart);
        mEditor.putBoolean(LOGGED_IN, isLoggedIn);
//        mEditor.putFloat(PREVIOUS_LATITUDE, previousLatitude);
//        mEditor.putFloat(PREVIOUS_LONGITUDE, previousLongitude);
//        mEditor.putFloat(PREVIOUS_ZOOM, previousZoom);
        mEditor.putString(PREVIOUS_LOCALITY, previousLocality);
        mEditor.apply();
        if (mListFragment != null && mListFragment.isAdded()) mManager.beginTransaction().remove(mListFragment)
                .commitAllowingStateLoss();
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
        showDialog(getActivity(), R.string.map_initialize);
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
                    //TODO On MAP CLICK
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            //TODO
                            mLatLng = latLng;
                            try {
                                if (mMarker != null) {
                                    mMarker.remove();
                                }
                                Geocoder gc = new Geocoder(getActivity());
                                List<Address> addresses = gc.getFromLocation(latLng.latitude, latLng
                                        .longitude, 1);
                                Address address = addresses.get(0);
                                MarkerOptions options = new MarkerOptions().title(address.getLocality())
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.defaultMarker());
                                mMarker = mMap.addMarker(options);
                                geoLocate(address);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    //TODO On Marker CLICK
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (mListFragment != null && mListFragment.isAdded())
                                mManager.beginTransaction().remove(mListFragment).commit();
                            final Station station = getSelectedStation(Station.data, marker
                                    .getTitle());
                            mListFragment = StationListFragment.newInstance(station);
                            mManager.beginTransaction().add(R.id.stationList, mListFragment).commit();
                            return false;
                        }
                    });
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


//        if (mLatLng != null) {
//            previousLatitude = (float) mLatLng.latitude;
//            previousLongitude = (float) mLatLng.longitude;
//        }
//        if (mMap != null) {
//            previousZoom = mMap.getCameraPosition().zoom;
//        }


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
            double lat = mCurrentLocation.getLatitude();
            double lng = mCurrentLocation.getLongitude();
            Geocoder gc = new Geocoder(getActivity());
            List<Address> addresses = gc.getFromLocation(lat, lng, 1);
            Address address = addresses.get(0);
            previousLocality = mLocality;
            mLocality = address.getLocality();
            setStartMarker(mLocality, lat, lng);
            LatLng ll = new LatLng(lat, lng);


//            if (mLatLng != null) {
//                previousLatitude = (float) mLatLng.latitude;
//                previousLongitude = (float) mLatLng.longitude;
//            }
//            if (mMap != null) {
//                previousZoom = mMap.getCameraPosition().zoom;
//            }


            mLatLng = ll;
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULT_ZOOM);
            mMap.animateCamera(update);
            mEditor = mPreference.edit();
//            mEditor.putFloat(PREVIOUS_LONGITUDE, previousLongitude);
//            mEditor.putFloat(PREVIOUS_ZOOM, previousZoom);
            mEditor.putString(PREVIOUS_LOCALITY, previousLocality);
            mEditor.apply();

            //TODO
            mPositionManager.saveMapPosition(address, ICON_PATH);
        } else {
            Snackbar.make(getView(), getText(R.string.last_location_failed), Snackbar.LENGTH_LONG).show();
        }
    }

    public void geoLocate(Address address) throws IOException {
        if (address != null) {
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            goToLocation(lat, lng, DEFAULT_ZOOM);
            previousLocality = mLocality;
            mLocality = address.getLocality();
            setStartMarker(mLocality, lat, lng);
            //TODO

            mEditor = mPreference.edit();
//            mEditor.putFloat(PREVIOUS_LONGITUDE, previousLongitude);
//            mEditor.putFloat(PREVIOUS_ZOOM, previousZoom);
            mEditor.putString(PREVIOUS_LOCALITY, previousLocality);
            mEditor.apply();


            mPositionManager.saveMapPosition(address, ICON_PATH);
        }
    }


    private void hideSoftKeyBoard(View view) {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    void showDialog(Context context, int resId) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.map_initialize));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        showDialog(getActivity(), R.string.map_initialize);
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
            double lat = mPositionManager.getLatitude();
            double lng = mPositionManager.getLongitude();


//            if (mLatLng != null) {
//                previousLatitude = (float) mLatLng.latitude;
//                previousLongitude = (float) mLatLng.longitude;
//            }
//            if (mMap != null) {
//                previousZoom = mMap.getCameraPosition().zoom;
//            }


            mLatLng = new LatLng(lat, lng);
            if (mMap == null) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mMap == null) {
                            Snackbar.make(getView(), getText(R.string.map_loading_failed), Snackbar.LENGTH_SHORT).show();
                            showDialog(getActivity(),R.string.map_reloading);
                            initMap();
                        }
                    }
                }, 1000);
            }
            mDialog.dismiss();
            setStartMarker(mPositionManager.getLocality(), lat, lng);
            mBartService.getStationInfo();
        } else {
            try {
                goToCurrentLocation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBartService.getStationInfo();
                    }
                }, 2000);
                isBartPressed = true;
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

    void setStartMarker(String locality, double lat, double lng) {
        if (mMarker != null) {
            mMarker.remove();
        }
        MarkerOptions options = new MarkerOptions().title(locality).position(new LatLng(lat, lng)).icon
                (isLoggedIn ? BitmapDescriptorFactory.fromBitmap(mFacebookAvatar) : BitmapDescriptorFactory
                        .defaultMarker());
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
    public void onMapActionSuccess(List<Station> list, ProgressDialog dialog) {
        StationManager stationManager = new StationManager(list);
        Station.setData(stationManager.getCloseStationsInCount(mLatLng, STATION_REQUIRED));
        mIndex = 0;
        dialog.dismiss();
        showDialog(getActivity(), R.string.weather_loading);
        //Set mStation also
        stationGetWeather();
        mWeatherService.getWeatherInfo(mStation.getCity() + "," + mStation.getState());
        if (mListFragment != null && mListFragment.isAdded())
            mManager.beginTransaction().remove(mListFragment).commit();
        mListFragment = StationListFragment.newInstance(null);
        mManager.beginTransaction().add(R.id.stationList, mListFragment).commit();
        //Check the Bart Key press state
        if (mPreference.getBoolean(BART, false) || isBartPressed) {
            setEndMarker(Station.data);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mLatLng, STATION_ZOOM);
            mMap.animateCamera(update);
        }
    }

    @Override
    public void onMapActionFailed(Exception e, ProgressDialog dialog) {
        dialog.dismiss();
    }


    private Station getSelectedStation(List<Station> stations, String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) return station;
        }
        return null;
    }

    @Override
    public void onWeatherActionSuccess(Channel channel) {
        Forecast forecast = new Forecast();
        forecast.parseJSON(channel.getForecastArray().optJSONObject(0));
        mStation.setTempLow(forecast.getLow());
        mStation.setTempHigh(forecast.getHigh());
        mStation.setTempInGeneral(forecast.getInGeneral());
        mStation.setCode(forecast.getCode());
        if (mIndex < STATION_REQUIRED) {
            mIndex++;
            stationGetWeather();
        } else {
            if (Station.data != null) {
                String distance = Station.data.get(0).getDistance();
                if ((Float.parseFloat(distance)) >= SATISFY_DISTANCE) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage(getString(R.string.alert_too_far, distance));
                    alert.setNegativeButton(R.string.confirm_alert, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
                setEndMarker(Station.data);
            }
            mDialog.dismiss();
        }
    }

    @Override
    public void onWeatherActionFailed(Exception e) {

    }

    void stationGetWeather() {
        if (mIndex < STATION_REQUIRED) {
            mStation = Station.data.get(mIndex);
            mDialog.setMessage(getString(R.string.weather_loading, mStation.getName()));
            mWeatherService.getWeatherInfo(mStation.getCity() + "," + mStation.getState());
        }
    }

    Address getAddress(double lat, double lng) throws IOException {
        Geocoder gc = new Geocoder(getActivity());
        List<Address> addresses = gc.getFromLocation(lat, lng, 1);
        return addresses.get(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageActionSuccess(Bitmap bitmap, ProgressDialog dialog) {
        mFacebookAvatar = ImageService.getCroppedBitmap(bitmap, 170);
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(mFacebookAvatar));
        dialog.dismiss();
    }

    @Override
    public void onImageActionFailed(ProgressDialog dialog) {
        dialog.dismiss();
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}

