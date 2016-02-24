package googlemapapi.yee.interview.bartnearme_1;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import googlemapapi.yee.interview.bartnearme_1.Fragment.GoogleMapFragment;
import googleapi1.yee.interview.bartnearme_1.R;

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
        setContentView(R.layout.main_layout);
        mSupportManager = getSupportFragmentManager();
        mManager = getFragmentManager();
        
        mGoogleMapFragment = new GoogleMapFragment();
        mSupportManager.beginTransaction().add(R.id.map, mGoogleMapFragment).commit();
    }
}
