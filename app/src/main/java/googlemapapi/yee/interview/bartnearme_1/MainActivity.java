package googlemapapi.yee.interview.bartnearme_1;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;

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
        clearApplicationData();
        mSupportManager = getSupportFragmentManager();
        mManager = getFragmentManager();
        mGoogleMapFragment = new GoogleMapFragment();
        mSupportManager.beginTransaction().add(R.id.map, mGoogleMapFragment).commit();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null && dir.delete();
    }
}


