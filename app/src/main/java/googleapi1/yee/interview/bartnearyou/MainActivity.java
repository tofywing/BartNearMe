package googleapi1.yee.interview.bartnearyou;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import googleapi1.yee.interview.bartnearyou.CallBack.MapCallBack;
import googleapi1.yee.interview.bartnearyou.CallBack.ServiceCallBack;
import googleapi1.yee.interview.bartnearyou.Fragment.GoogleMapFragment;
import googleapi1.yee.interview.bartnearyou.Fragment.StationDetailsFragment;
import googleapi1.yee.interview.bartnearyou.Fragment.StationListFragment;
import googleapi1.yee.interview.bartnearyou.Service.BartService;

/**
 * Created by Yee on 2/2/16.
 */
public class MainActivity extends FragmentActivity {

    android.support.v4.app.FragmentManager mSupportManager;
    FragmentManager mManager;
    //GoogleMapFragment1 mGoogleMapFragment;
    GoogleMapFragment mGoogleMapFragment;
    StationListFragment mListFragment;
    StationDetailsFragment mDetailsFragment;
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
        mDetailsFragment = new StationDetailsFragment();
        mManager.beginTransaction().add(R.id.stationDetail, mDetailsFragment).commit();
        mGoogleMapFragment = new GoogleMapFragment();
        mSupportManager.beginTransaction().add(R.id.map, mGoogleMapFragment).commit();
    }

    public static void showDialog(Context context, ProgressDialog dialog) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
    }
}
