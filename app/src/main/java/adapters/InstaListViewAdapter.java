package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

import classes.Data;
import xu_aaabeck.tattoohub.FullImageActivity;
import xu_aaabeck.tattoohub.R;

/**
 * Created by root on 08.03.18.
 */

public class InstaListViewAdapter extends ArrayAdapter<Data> {

    private Context context;
    private ArrayList<Data> data;

    public InstaListViewAdapter(Context context, int textViewResourceId, ArrayList<Data> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        InstaSearchHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.feed_list_view_item, null);
            holder = new InstaSearchHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_user_fullname);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_photo);
            holder.user_photo = (ImageView) convertView.findViewById(R.id.iv_profile);
            convertView.setTag(holder);
        }
        else {
            holder = (InstaSearchHolder)convertView.getTag();
        }


        holder.name.setText(data.get(position).getUser().getFull_name());

        holder.user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();

            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
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
                .into(holder.user_photo);
        Picasso.with(context)
                .load(data.get(position).getImages().getStandard_resolution().getUrl())
                .into(holder.image);

        return convertView;
    }

    public void clearListView() {
        data.clear();
    }


    static class InstaSearchHolder {
        private TextView name;
        private ImageView image;
        private ImageView user_photo;
    }
}
