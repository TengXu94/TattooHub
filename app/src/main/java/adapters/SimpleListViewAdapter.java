package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

import classes.Data;
import xu_aaabeck.tattoohub.FullImageActivity;
import xu_aaabeck.tattoohub.HomeActivity;
import xu_aaabeck.tattoohub.LoginActivity;
import xu_aaabeck.tattoohub.R;

/**
 * Created by root on 08.03.18.
 */

public class SimpleListViewAdapter extends ArrayAdapter<Data> {

    private Context context;
    private ArrayList<Data> data;
    private boolean isImageFitToScreen = false;

    public SimpleListViewAdapter(Context context, int textViewResourceId, ArrayList<Data> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View curView = convertView;
        if (curView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.feed_list_view_item, null);
        }

        TextView tv_user_fullname = (TextView) curView.findViewById(R.id.tv_user_fullname);
        final ImageView iv_photo = (ImageView) curView.findViewById(R.id.iv_photo);
        ImageView iv_profile = (ImageView) curView.findViewById(R.id.iv_profile);

        tv_user_fullname.setText(data.get(position).getUser().getFull_name());

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();

            }
        });

        iv_photo.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, FullImageActivity.class);
                i.putExtra("photo", data.get(position).getImages().getStandard_resolution().getUrl());
                context.startActivity(i);
            }
        });



        Picasso.with(context)
                .load(data.get(position).getUser().getProfile_picture())
                .resize(100, 100)
                .centerInside()
                .into(iv_profile);
        Picasso.with(context)
                .load(data.get(position).getImages().getStandard_resolution().getUrl())
                .into(iv_photo);

        return curView;
    }

    public void clearListView() {
        data.clear();
    }
}
