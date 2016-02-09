package googleapi1.yee.interview.bartnearyou;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yee on 2/4/16.
 */
public class Station {
    String name;
    String abbr;
    String address;
    String city;
    String state;
    String zipCode;
    String latitude;
    String longitude;
    public static List<Station> mData= new ArrayList<>();
    public static List<Station> data;

//    public ArrayList<Station> getStations() {
//        for (int i = 0; i <= 4; i++) {
//            Station station = new Station();
//            station.setName(String.valueOf(i));
//            data.add(station);
//        }
//        return data;
//    }


    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String station_abbr) {
        this.abbr = station_abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String station_name) {
        this.name = station_name;
    }

}
