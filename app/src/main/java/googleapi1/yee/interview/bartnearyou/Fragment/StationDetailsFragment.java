package googleapi1.yee.interview.bartnearyou.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import googleapi1.yee.interview.bartnearyou.R;

/**
 * Created by Yee on 2/4/16.
 */
public class StationDetailsFragment extends Fragment {

    FloatingActionButton mButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.station_details_fragment, container,false);
        mButton = (FloatingActionButton)view.findViewById(R.id.findMe);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
