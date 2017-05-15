package siliconsolutions.cpptourapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

import siliconsolutions.cpptourapp.Model.Building;
import siliconsolutions.cpptourapp.R;

/**
 * Created by user on 5/14/17.
 */

public class FavoritesListAdapter extends ArrayAdapter<Marker> {
    List<Marker> favorites;

    public FavoritesListAdapter(Context context, int resource, List<Marker> favorites) {
        super(context, resource, favorites);
        this.favorites = favorites;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.locations_list_item_layout,parent,false);
        }


        convertView.setTag(position);
        return convertView;
    }
}
