package googleapi1.yee.interview.bartnearme.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import googleapi1.yee.interview.bartnearme.R;
import googleapi1.yee.interview.bartnearme.Station;

/**
 * Created by Yee on 2/4/16.
 */
public class StationDetailsFragment extends Fragment {

    public static final String TAG = "stationSelected";
    TextView mName;
    TextView mDistance;
    TextView mAddress;
    TextView mCity;
    TextView mState;
    TextView mZip;

    public static StationDetailsFragment newInstance(Station station) {
        Bundle args = new Bundle();
        args.putParcelable(TAG, station);
        StationDetailsFragment fragment = new StationDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Station station = getArguments().getParcelable(TAG);
        mName = (TextView) getActivity().findViewById(R.id.name);
        mDistance = (TextView) getActivity().findViewById(R.id.distance);
        mAddress = (TextView) getActivity().findViewById(R.id.address);
        mCity = (TextView) getActivity().findViewById(R.id.city);
        mState = (TextView) getActivity().findViewById(R.id.state);
        mZip = (TextView) getActivity().findViewById(R.id.zip);

        if (station != null) {
            mName.setText(station.getName());
            mAddress.setText(station.getAddress());
            mDistance.setText(station.getDistance());
            mCity.setText(station.getCity());
            mState.setText(station.getState());
            mZip.setText(station.getZipCode());
        } else {
            mName.setText(getText(R.string.name_unavailable));
            mDistance.setText(getText(R.string.distance_unavailable));
            mAddress.setText(getText(R.string.address_unavailable));
            mCity.setText(getText(R.string.city_unavailable));
            mState.setText(getText(R.string.state_unavailable));
            mZip.setText(getText(R.string.zip_unavailable));
        }
    }
}
