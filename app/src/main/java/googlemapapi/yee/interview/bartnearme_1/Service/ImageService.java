package googlemapapi.yee.interview.bartnearme_1.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;

import com.facebook.login.LoginResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import googleapi1.yee.interview.bartnearme_1.R;
import googlemapapi.yee.interview.bartnearme_1.CallBack.ImageServiceCallBack;

/**
 * Created by Yee on 2/24/16.
 */
public class ImageService {

    Context mContext;
    ImageServiceCallBack mCallBack;
    ProgressDialog mDialog;

    public ImageService(Context context, ImageServiceCallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    public void getImageInfo(LoginResult loginResult) {
        showDialog(mContext);
        new AsyncTask<LoginResult, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(LoginResult... params) {
                URL url = null;
                try {
                    url = new URL("https://graph.facebook.com/" + params[0].getAccessToken().getUserId() +
                            "/picture?type=large");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    if (url != null) {
                        return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) mCallBack.onImageActionSuccess(bitmap, mDialog);
                else mCallBack.onImageActionFailed(mDialog);
            }
        }.execute(loginResult);
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
                sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }

    void showDialog(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.map_loading));
        mDialog.setCancelable(false);
        mDialog.show();
    }
}
