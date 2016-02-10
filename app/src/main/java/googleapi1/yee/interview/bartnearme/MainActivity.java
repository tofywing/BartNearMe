package googleapi1.yee.interview.bartnearme;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;

import googleapi1.yee.interview.bartnearme.CallBack.MapCallBack;
import googleapi1.yee.interview.bartnearme.CallBack.ServiceCallBack;
import googleapi1.yee.interview.bartnearme.Fragment.GoogleMapFragment;
import googleapi1.yee.interview.bartnearme.Fragment.StationListFragment;
import googleapi1.yee.interview.bartnearme.Service.BartService;

/**
 * Created by Yee on 2/2/16.
 */
public class MainActivity extends FragmentActivity {

    android.support.v4.app.FragmentManager mSupportManager;
    FragmentManager mManager;
    //GoogleMapFragment1 mGoogleMapFragment;
    GoogleMapFragment mGoogleMapFragment;
    StationListFragment mListFragment;
    //StationDetailsFragment mDetailsFragment;
    //FloatingActionButton mMeButton;
    FloatingActionButton mBartButton;
    BartService mService;
    ServiceCallBack mCallback;
    ProgressDialog mDialog;
    MapCallBack mMapCallBack;
    //List<Station> mData;
    SharedPreferences mSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mSupportManager = getSupportFragmentManager();
        mManager = getFragmentManager();
        mGoogleMapFragment = new GoogleMapFragment();
        mSupportManager.beginTransaction().add(R.id.map, mGoogleMapFragment).commit();
    }
}
