package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import xu_aaabeck.tattoohub.R;

/**
 * Created by root on 08.03.18.
 */

public class SimpleGridViewAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> data;

    public SimpleGridViewAdapter(Context context, int textViewResourceId, ArrayList<String> data) {
        super(context, textViewResourceId, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View curView = convertView;
        if (curView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.grid_item, null);
        }

        final ImageView photo = (ImageView) curView.findViewById(R.id.imageID);

        Picasso.with(context)
                .load(data.get(position))
                .resize(100, 100)
                .centerInside()
                .into(photo);


        return curView;
    }

    public void clearListView() {
        data.clear();
    }
}
