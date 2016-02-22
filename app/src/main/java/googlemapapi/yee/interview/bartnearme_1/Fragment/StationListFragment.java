package googlemapapi.yee.interview.bartnearme_1.Fragment;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import googlemapapi.yee.interview.bartnearme_1.Data.Station;
import googlemapapi.yee.interview.bartnearme_1.StationDetailsManager;
import googlemapapi.yee.interview.bartnearme_1.Adapter.StationAdapter;
import googleapi1.yee.interview.bartnearme_1.R;

/**
 * Created by Yee on 2/4/16.
 */
public class StationListFragment extends ListFragment {

    StationDetailsFragment mDetailFragment;
    FragmentManager mManager;
    Station mStation = null;
    public static final String TAG = "stationTransaction";

    public static StationListFragment newInstance(Station station) {
        Bundle args = new Bundle();
        args.putParcelable(TAG, station);
        StationListFragment fragment = new StationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StationAdapter adapter = new StationAdapter(getActivity(), R.layout.station_adapter, Station.getData());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mDetailFragment != null && mDetailFragment.isAdded()) mManager.beginTransaction().remove(mDetailFragment)
                .commit();
        mStation = Station.getData().get(position);
        mDetailFragment = StationDetailsFragment.newInstance(mStation);
        mManager.beginTransaction().add(R.id.stationDetail, mDetailFragment).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        StationDetailsManager manager = new StationDetailsManager(getActivity());
        manager.saveStationDetails(mStation);
    }

    @Override
    public void onResume() {
        super.onResume();
        mManager = getActivity().getFragmentManager();
        mStation = getArguments().getParcelable(TAG);
        if (mStation == null) {
            StationDetailsManager manager = new StationDetailsManager(getActivity());
            mStation = manager.getSavedStation();
        }
        if (mStation != null) {
            mDetailFragment = StationDetailsFragment.newInstance(mStation);
            mManager.beginTransaction().add(R.id.stationDetail, mDetailFragment).commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDetailFragment != null && mDetailFragment.isAdded())
            mManager.beginTransaction().remove(mDetailFragment).commitAllowingStateLoss();
    }
}
