package googleapi1.yee.interview.bartnearyou.CallBack;

import android.app.ProgressDialog;

import java.util.List;

import googleapi1.yee.interview.bartnearyou.Station;

/**
 * Created by Yee on 2/4/16.
 */
public interface ServiceCallBack {

    void onActionSuccess(List<Station> list, ProgressDialog dialog);

    void onActionFailed(Exception e, ProgressDialog dialog);
}
