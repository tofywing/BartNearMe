package googleapi1.yee.interview.bartnearme.Fragment;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import googleapi1.yee.interview.bartnearme.Adapter.StationAdapter;
import googleapi1.yee.interview.bartnearme.CallBack.ServiceCallBack;
import googleapi1.yee.interview.bartnearme.R;
import googleapi1.yee.interview.bartnearme.Service.BartService;
import googleapi1.yee.interview.bartnearme.Station;

/**
 * Created by Yee on 2/4/16.
 */
public class StationListFragment extends ListFragment implements ServiceCallBack {

    List<Station> mList;
    BartService mService;
    ListView mListView;
    StationDetailsFragment mDetailFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = new BartService(getActivity(), this);
        mService.getStationInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mListView = getListView();
        final FragmentManager manager = getActivity().getFragmentManager();
//        mListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDetailFragment != null && mDetailFragment.isAdded()) manager.beginTransaction().remove
//                        (mDetailFragment).commit();
//                mDetailFragment = new StationDetailsFragment();
//                if (mDetailFragment.isAdded()) Toast.makeText(getActivity(), "added", Toast.LENGTH_LONG).show();
//            }
//        });-
    }

    @Override
    public void onActionSuccess(List<Station> list, ProgressDialog dialog) {
        mList = list;
        StationAdapter adapter = new StationAdapter(getActivity(), R.layout.station_adapter, mList);
        setListAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onActionFailed(Exception e, ProgressDialog dialog) {
        dialog.dismiss();
    }
}