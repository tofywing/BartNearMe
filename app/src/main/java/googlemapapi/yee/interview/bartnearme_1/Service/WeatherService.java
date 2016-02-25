package googlemapapi.yee.interview.bartnearme_1.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import googleapi1.yee.interview.bartnearme_1.R;
import googlemapapi.yee.interview.bartnearme_1.CallBack.YahooWeatherServiceCallBack;
import googlemapapi.yee.interview.bartnearme_1.Data.Channel;

/**
 * Created by Yee on 2/21/16.
 */
public class WeatherService {

    YahooWeatherServiceCallBack mCallback;
    Context mContext;

    public WeatherService(Context context, YahooWeatherServiceCallBack callback) {
        this.mCallback = callback;
        this.mContext = context;
    }

    public void getWeatherInfo(String location) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo" +
                        ".places(1) where text=\"%s\")", params[0]);
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri
                        .encode(YQL));
                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder data = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) data.append(line);
                    return data.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mContext.getString(R.string.service_error);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonData = new JSONObject(s);
                    JSONObject parseQuery = jsonData.optJSONObject("query");
                    int count = parseQuery.optInt("count");
                    //Check general input error including empty city and state
                    if (count == 0) {
                        mCallback.onWeatherActionFailed(new Exception());
                    } else {
                        Channel channel = new Channel();
                        channel.parseJSON(parseQuery.optJSONObject("results").optJSONObject("channel"));
                        mCallback.onWeatherActionSuccess(channel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(location);
    }
}
