package adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import model.Category;
import xu_aaabeck.tattoohub.R;

/**
 * Created by White_Orchard on 15/03/2018.
 */

public class CategoryList extends ArrayAdapter<String>{

    private Activity context;
    private List<String> categoryList;


    public CategoryList(Activity context, List<String> categoryList) {
        super(context, R.layout.categories_fragment, categoryList);
        this.context = context;
        this.categoryList = categoryList;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.card_view, null, true); //categories item layout

        TextView textViewName = listViewItem.findViewById(R.id.textViewName); //textViewCategoryName

        String category = categoryList.get(position);

        textViewName.setText(category);


        return listViewItem;
    }
}
