package googleapi1.yee.interview.bartnearme.Fragment;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import googleapi1.yee.interview.bartnearme.Adapter.StationAdapter;
import googleapi1.yee.interview.bartnearme.R;
import googleapi1.yee.interview.bartnearme.Station;

/**
 * Created by Yee on 2/4/16.
 */
public class StationListFragment extends ListFragment {

    public static final String TAG = "stationDetailedFragment";
    StationDetailsFragment mDetailFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StationAdapter adapter = new StationAdapter(getActivity(), R.layout.station_adapter, Station.getData());
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FragmentManager manager = getActivity().getFragmentManager();
        if (mDetailFragment != null && mDetailFragment.isAdded()) manager.beginTransaction().remove(mDetailFragment)
                .commit();
        mDetailFragment = StationDetailsFragment.newInstance(Station.getData().get(position));
        manager.beginTransaction().add(R.id.stationDetail, mDetailFragment).commit();
    }
}
