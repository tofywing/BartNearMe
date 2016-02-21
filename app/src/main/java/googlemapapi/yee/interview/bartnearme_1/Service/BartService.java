package googlemapapi.yee.interview.bartnearme_1.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import googlemapapi.yee.interview.bartnearme_1.CallBack.ServiceCallBack;
import googlemapapi.yee.interview.bartnearme_1.Station;
import googleapi1.yee.interview.bartnearme_1.R;

/**
 * Created by Yee on 2/4/16.
 */
public class BartService {

    Context mContext;
    ServiceCallBack mCallBack;
    private final String BART_STATION_URL = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";
    ProgressDialog mDialog;

    public BartService(Context context, ServiceCallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    public void getStationInfo() {
        showDialog(mContext);
        new AsyncTask<String, Void, List<Station>>() {
            @Override
            protected List<Station> doInBackground(String... params) {
                List<Station> data = new ArrayList<>();
                try {
                    URL url = new URL(BART_STATION_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);
                    data = parseXML(parser, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return data;
            }

            @Override
            protected void onPostExecute(List<Station> data) {
                super.onPostExecute(data);
                // Not defined the exception yet
                if (data.size() == 0) mCallBack.onActionFailed(new Exception(), mDialog);
                else mCallBack.onActionSuccess(data, mDialog);
            }
        }.execute();
    }

    private List<Station> parseXML(XmlPullParser xmlPullParser, List<Station> list) throws XmlPullParserException,
            IOException {
        int evenType = xmlPullParser.getEventType();
        Station station = null;
        while (evenType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (evenType) {
                case XmlPullParser.START_DOCUMENT:
                    list = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = xmlPullParser.getName();
                    if (name.equals("station")) station = new Station();
                    else if (station != null) switch (name) {
                        case "name":
                            station.setName(xmlPullParser.nextText());
                            break;
                        case "abbr":
                            station.setAbbr(xmlPullParser.nextText());
                            break;
                        case "gtfs_latitude":
                            station.setLatitude(xmlPullParser.nextText());
                            break;
                        case "gtfs_longitude":
                            station.setLongitude(xmlPullParser.nextText());
                            break;
                        case "address":
                            station.setAddress(xmlPullParser.nextText());
                            break;
                        case "city":
                            station.setCity(xmlPullParser.nextText());
                            break;
                        case "state":
                            station.setState(xmlPullParser.nextText());
                            break;
                        case "zipcode":
                            station.setZipCode(xmlPullParser.nextText());
                            break;
                        default:
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = xmlPullParser.getName();
                    if (name.equalsIgnoreCase("station") && station != null) list.add(station);
                    break;
                default:
                    break;
            }
            evenType = xmlPullParser.next();
        }
        return list;
    }

    void showDialog(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.show();
    }
}
