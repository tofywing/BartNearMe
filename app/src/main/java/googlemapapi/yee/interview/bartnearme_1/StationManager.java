package googlemapapi.yee.interview.bartnearme_1;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import googleapi1.yee.interview.bartnearme_1.R;
import googlemapapi.yee.interview.bartnearme_1.CallBack.YahooWeatherServiceCallBack;
import googlemapapi.yee.interview.bartnearme_1.Data.Channel;
import googlemapapi.yee.interview.bartnearme_1.Data.Forecast;
import googlemapapi.yee.interview.bartnearme_1.Data.Station;
import googlemapapi.yee.interview.bartnearme_1.Fragment.StationListFragment;
import googlemapapi.yee.interview.bartnearme_1.Service.WeatherService;

/**
 * Created by Yee on 2/10/16.
 */
public class StationManager {
    List<Station> mStations;
    List<Station> mResultStations;
    int size;
    Map<Double, Integer> mCloseStationMap = new TreeMap<>();
    WeatherService mWeatherService;
    Context mContext;
    YahooWeatherServiceCallBack mCallBack;
    Station mStation;
    StationListFragment mListFragment;
    FragmentManager mManager;
    int mIndex = 0;
    boolean end;

    public StationManager(List<Station> stations) {
        mStations = stations;
        size = stations.size();
    }

    public double getDistance(LatLng StartP, LatLng EndP) {
        // radius of earth in Km
        int Radius = 6371;
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

    public List<Station> getCloseStationsInCount(LatLng start, int count) {
        count = Math.min(count, size);
        mResultStations = new ArrayList<>();
        LatLng end;
        for (int i = 0; i < size; i++) {
            end = new LatLng(Double.parseDouble(mStations.get(i).getLatitude()), Double.parseDouble(mStations.get(i)
                    .getLongitude()));
            mCloseStationMap.put(getDistance(start, end), i);
        }
        for (Map.Entry<Double, Integer> map : mCloseStationMap.entrySet()) {
            if (count == 0) break;
            Station station = mStations.get(map.getValue());
            station.setDistance(getDistanceInKM(map.getKey()));
            mResultStations.add(station);
            count--;
        }
        return mResultStations;
    }

    void addWeatherInformation(Context context){
        mContext = context;

    }

//    public List<Station> addWeatherInformation(List<Station> stations, Context context) {
//        mContext = context;
//        mWeatherService = new WeatherService(context, this);
//        for (; mIndex < stations.size(); mIndex++) {
//            addWeatherToEach(stations.get(mIndex));
//        }
//        return stations;
//    }





    void addWeatherToEach(Station station) {
        mStation = station;
        mWeatherService.getWeather(station.getCity() + "," + station.getState());
    }


    public void start(List<Station> stations, Context context) {
//        mWeatherService = new WeatherService(context, this);
        mIndex = 0;
        add();
    }


    void add() {
        if (mIndex < 5) {
            mStation = mResultStations.get(mIndex);
            mWeatherService.getWeather(mStation.getCity() + "," + mStation.getState());

        }
    }


    String getDistanceInKM(double distance) {
        return String.format("%.2f km", distance);
    }


    void fragmentTransaction(StationListFragment mListFragment, FragmentManager mManager) {
        if (mListFragment != null && mListFragment.isAdded())
            mManager.beginTransaction().remove(mListFragment).commit();
        mListFragment = StationListFragment.newInstance(null);
        mManager.beginTransaction().add(R.id.stationList, mListFragment).commit();
    }


//    @Override
//    public void onWeatherActionSuccess(Channel channel) {
////        if(mIndex != 5){
////        Station station = Station.data.get(mIndex);
////        Forecast forecast = new Forecast();
////        forecast.parseJSON(channel.getForecastArray().optJSONObject(0));
////        station.setTempHigh(forecast.getHigh());
////        station.setTempLow(forecast.getLow());
////        station.setTempInGeneral(forecast.getInGeneral());
////        MainActivity.makeToast(mContext, mStation.getTempHigh());}
//
//
//        Forecast forecast = new Forecast();
//        forecast.parseJSON(channel.getForecastArray().optJSONObject(0));
//        if (mIndex < 5) {
//            mResultStations.get(mIndex).setTempHigh(forecast.getHigh());
//            if (mIndex == 4) {
//                Station.data = mResultStations;
//                fragmentTransaction(mListFragment, mManager);
//            }
//            MainActivity.makeToast(mContext, Station.data.get(0).getTempHigh());
//        }
//        mIndex++;
//        add();
//    }
//
//    @Override
//    public void onWeatherActionFailed(Exception e) {
//
//    }
}
