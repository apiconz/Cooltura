package pe.gdgopenlima.cooltura.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.gdgopenlima.cooltura.R;
import pe.gdgopenlima.cooltura.entities.Place;

/**
 * Created by armando on 21/09/14.
 */
public class PlaceListAdapter extends ArrayAdapter<Place> {

    private List<Place> placeList;
    private Activity context;

    public PlaceListAdapter(Activity context, List<Place> placeList) {
        super(context, R.layout.fragment_list_item, placeList);
        this.placeList = placeList;
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.fragment_list_item, null);

            final ViewHolder holder = new ViewHolder();
            holder.imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            holder.txtPlaceTitle = (TextView) view.findViewById(R.id.txtPlaceTitle);
            holder.txtPlaceAddress = (TextView) view.findViewById(R.id.txtPlaceAddress);

            holder.txtPlaceId = (TextView) view.findViewById(R.id.txtPlaceId);
            holder.txtPlaceDescription = (TextView) view.findViewById(R.id.txtPlaceDescription);
            holder.txtPlaceDistrict = (TextView) view.findViewById(R.id.txtPlaceDistrict);

            view.setTag(holder);
            holder.imgCategory.setTag(this.placeList.get(position));

        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).imgCategory.setTag(this.placeList
                    .get(position));
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        Place place = this.placeList.get(position);
        holder.imgCategory.setImageResource(R.drawable.ic_action_place);
        holder.txtPlaceTitle.setText(place.name);
        holder.txtPlaceAddress.setText(place.address);
        holder.txtPlaceDescription.setText(place.category);
        holder.txtPlaceDistrict.setText(place.district);

        return view;
    }

    private class ViewHolder {
        public ImageView imgCategory;
        public TextView txtPlaceTitle;
        public TextView txtPlaceAddress;
        public TextView txtPlaceId;
        public TextView txtPlaceDescription;
        public TextView txtPlaceDistrict;

    }
}
