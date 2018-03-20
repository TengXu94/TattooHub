package fragments;


import adapters.CategoryList;
import model.Category;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import xu_aaabeck.tattoohub.R;

/**
 * Created by root on 15.03.18.
 */

public class CategoriesFragment extends Fragment {

    ListView listViewCategory;
    DatabaseReference categoriesRef;
    List<Category> categoryList;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference("categories");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_fragment, container, false);

        listViewCategory = (ListView) view.findViewById(R.id.categoriesListView);

        categoryList = new ArrayList<>();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                categoryList.clear();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){

                    Category category = categorySnapshot.getValue(Category.class);

                    categoryList.add(category);

                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "***************" + category.getName(), duration);
                    toast.show();


                }

                CategoryList adapter = new CategoryList(getActivity(), categoryList);
                listViewCategory.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
