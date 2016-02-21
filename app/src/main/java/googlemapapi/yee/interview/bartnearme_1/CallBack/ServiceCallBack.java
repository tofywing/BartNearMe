package googlemapapi.yee.interview.bartnearme_1.CallBack;

import android.app.ProgressDialog;

import java.util.List;

import googlemapapi.yee.interview.bartnearme_1.Station;

/**
 * Created by Yee on 2/4/16.
 */
public interface ServiceCallBack {

    void onActionSuccess(List<Station> list, ProgressDialog dialog);

    void onActionFailed(Exception e, ProgressDialog dialog);
}
