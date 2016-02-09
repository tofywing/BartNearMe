package googleapi1.yee.interview.bartnearme.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import googleapi1.yee.interview.bartnearme.R;
import googleapi1.yee.interview.bartnearme.Station;

/**
 * Created by Yee on 2/4/16.
 */
public class StationAdapter extends ArrayAdapter<Station> {

    private List<Station> mDataSet;
    private Context mContext;

    public StationAdapter(Context context, int resource, List<Station> dataSet) {
        super(context, resource, dataSet);
        mDataSet = dataSet;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Station station = mDataSet.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = inflater.inflate(R.layout.station_adapter, null);
        TextView textView = (TextView) view.findViewById(R.id.stationName);
        textView.setText(String.valueOf(station.getName()));
        return view;
    }
}
