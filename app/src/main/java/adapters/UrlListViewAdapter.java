package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import classes.Data;
import xu_aaabeck.tattoohub.CategorySavePop;
import xu_aaabeck.tattoohub.FullImageActivity;
import xu_aaabeck.tattoohub.R;

public class UrlListViewAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> data;
    private String url;
    private String owner;

    public UrlListViewAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.data = objects;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View curView = convertView;
        if (curView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.feed_list_view_item, null);
        }
        url = data.get(position).split(",")[0];

        owner = data.get(position).split(",")[1];
        TextView tv_user_fullname = (TextView) curView.findViewById(R.id.tv_user_fullname);
        final ImageView iv_photo = (ImageView) curView.findViewById(R.id.iv_photo);

        tv_user_fullname.setText(owner);


        iv_photo.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, CategorySavePop.class);
                Activity activity = (Activity)context;
                i.putExtra("photo", url);

                context.startActivity(i);

                return true;
            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, FullImageActivity.class);
                i.putExtra("photo", url);
                context.startActivity(i);
            }
        });



        Picasso.with(context)
                .load(url).fit()
                .into(iv_photo);

        return curView;
    }

    public void clearListView() {
        data.clear();
    }
}
