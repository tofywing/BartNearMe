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
    FragmentManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StationAdapter adapter = new StationAdapter(getActivity(), R.layout.station_adapter, Station.getData());
        mManager = getActivity().getFragmentManager();
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mDetailFragment != null && mDetailFragment.isAdded()) mManager.beginTransaction().remove(mDetailFragment)
                .commit();
        mDetailFragment = StationDetailsFragment.newInstance(Station.getData().get(position));
        mManager.beginTransaction().add(R.id.stationDetail, mDetailFragment).commit();
    }

    @Override
    public void onDestroy() {
        if (mDetailFragment != null && mDetailFragment.isAdded()) mManager.beginTransaction().remove(mDetailFragment)
                .commit();
        super.onDestroy();
    }
}
