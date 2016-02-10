package googleapi1.yee.interview.bartnearme.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.zip.Inflater;

import googleapi1.yee.interview.bartnearme.Station;

/**
 * Created by Yee on 2/10/16.
 */
public class DetailAdapter extends ArrayAdapter<Station> {

    Context mContext;
    Station mData;
    String[] mTitleSet = new String[]{
            "Name",
            "Address",
            "City",
            "State",
            "Zip"
    };

    public DetailAdapter(Context context, int resource, Station data) {
        super(context, resource);
        mContext = context;
        mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Inflater inflater = (Inflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return super.getView(position, convertView, parent);
    }
}
