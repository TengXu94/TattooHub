package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import xu_aaabeck.tattoohub.R;

public class SpinnerViewAdapter extends BaseAdapter{
    private Context context;
    private String[] searchEngines;

    public SpinnerViewAdapter(Context applicationContext, String[] searchEngines) {
        this.context = applicationContext;
        this.searchEngines = searchEngines;
    }

    @Override
    public int getCount() {
        return searchEngines.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View curView = convertView;
        if (curView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.custom_spinner_items, null);
        }
        TextView names = curView.findViewById(R.id.textView);
        names.setText(searchEngines[i]);
        return curView;
    }
}
