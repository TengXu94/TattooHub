package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xu_aaabeck.tattoohub.R;

/**
 * Created by root on 15.03.18.
 */

public class CategoriesFragment extends Fragment {

    // newInstance constructor for creating fragment with arguments
    public static CategoriesFragment newInstance() {
        CategoriesFragment fragmentFirst = new CategoriesFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_list_fragment, container, false);

        return view;
    }
}
