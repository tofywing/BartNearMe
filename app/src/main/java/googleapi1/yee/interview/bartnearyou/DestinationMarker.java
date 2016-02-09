package googleapi1.yee.interview.bartnearyou;

import java.util.ArrayList;

/**
 * Created by Yee on 2/8/16.
 */
public class DestinationMarker {

    String locality;
    String latitude;
    String longitude;
    ArrayList<DestinationMarker> data = new ArrayList<>();

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocality() {
        return locality;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
