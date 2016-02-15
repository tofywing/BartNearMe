package googleapi1.yee.interview.bartnearme;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import googleapi1.yee.interview.bartnearme.Fragment.GoogleMapFragment;

/**
 * Created by Yee on 2/2/16.
 */
public class MainActivity extends FragmentActivity {

    android.support.v4.app.FragmentManager mSupportManager;
    FragmentManager mManager;
    GoogleMapFragment mGoogleMapFragment;

    public static void makeToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_portait);
        mSupportManager = getSupportFragmentManager();
        mManager = getFragmentManager();
        mGoogleMapFragment = new GoogleMapFragment();
        mSupportManager.beginTransaction().add(R.id.map, mGoogleMapFragment).commit();
    }


    //TODO: onConfigurationChanged
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MainActivity.makeToast(this, "activity");
        if (mGoogleMapFragment != null && mGoogleMapFragment.isAdded()) mSupportManager.beginTransaction().remove
                (mGoogleMapFragment).commit();
    }
}
