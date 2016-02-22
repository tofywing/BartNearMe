package googlemapapi.yee.interview.bartnearme_1.Data;

import org.json.JSONObject;

/**
 * Created by Yee on 2/21/16.
 */
public class Forecast implements ParseData {

    private String day;
    private String date;
    private String high;
    private String low;
    private String inGeneral;

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getInGeneral() {
        return inGeneral;
    }

    @Override
    public void parseJSON(JSONObject object) {
        this.day = object.optString("day");
        this.date = object.optString("date");
        this.high = String.valueOf(object.optInt("high"));
        this.low = String.valueOf(object.optInt("low"));
        this.inGeneral = object.optString("text");
    }
}

