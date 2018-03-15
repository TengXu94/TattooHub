package model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import xu_aaabeck.tattoohub.R;

/**
 * Created by White_Orchard on 15/03/2018.
 */

public class CategoryList extends ArrayAdapter<Category>{

    private Activity context;
    private List<Category> categoryList;


    public CategoryList(Activity context, List<Category> categoryList) {
        super(context, R.layout.categories_list_fragment, categoryList);
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.categories_list_fragment, null, true);

        Category category = categoryList.get(position);
    }
}
