package googlemapapi.yee.interview.bartnearme_1.Data;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Yee on 2/21/16.
 */
public class Channel implements ParseData {
    JSONArray forecastArray;
    JSONObject Channel;

    public JSONObject getChannel() {
        return Channel;
    }

    public JSONArray getForecastArray() {
        return forecastArray;
    }

    @Override
    public void parseJSON(JSONObject object) {
        this.Channel = object;
        forecastArray = object.optJSONObject("item").optJSONArray("forecast");
    }
}