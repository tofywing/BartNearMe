package googlemapapi.yee.interview.bartnearme_1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Yee on 2/4/16.
 */
public class Station implements Parcelable {
    //Creator of Parcel
    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
    public static List<Station> data;
    String name;
    String abbr;
    String address;
    String city;
    String state;
    String zipCode;
    String latitude;
    String longitude;
    String distance;

    public Station() {

    }

    //Constructor invoked by Creator of Parcel
    protected Station(Parcel in) {
        name = in.readString();
        abbr = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zipCode = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static List<Station> getData() {
        return data;
    }

    public static void setData(List<Station> data) {
        Station.data = data;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String station_abbr) {
        this.abbr = station_abbr;
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

    public String getName() {
        return name;
    }

    public void setName(String station_name) {
        this.name = station_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(abbr);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipCode);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
