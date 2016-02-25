package googlemapapi.yee.interview.bartnearme_1.CallBack;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;

/**
 * Created by Yee on 2/24/16.
 */
public interface ImageServiceCallBack {

    void onImageActionSuccess(Bitmap bitmap, ProgressDialog dialog);

    void onImageActionFailed(ProgressDialog dialog);
}
