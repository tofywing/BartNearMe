package googlemapapi.yee.interview.bartnearme_1.CallBack;

import android.app.ProgressDialog;

import java.util.List;

import googlemapapi.yee.interview.bartnearme_1.Data.Station;

/**
 * Created by Yee on 2/4/16.
 */
public interface BartServiceCallBack {

    void onMapActionSuccess(List<Station> list, ProgressDialog dialog);

    void onMapActionFailed(Exception e, ProgressDialog dialog);
}
