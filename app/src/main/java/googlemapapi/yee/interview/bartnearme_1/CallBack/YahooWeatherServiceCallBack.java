package googlemapapi.yee.interview.bartnearme_1.CallBack;

import android.app.ProgressDialog;

import org.json.JSONException;

import googlemapapi.yee.interview.bartnearme_1.Data.Channel;
import googlemapapi.yee.interview.bartnearme_1.Service.WeatherService;

/**
 * Created by Yee on 2/21/16.
 */
public interface YahooWeatherServiceCallBack {

    void onWeatherActionSuccess(Channel channel) throws JSONException;

    void onWeatherActionFailed(Exception e);
}
