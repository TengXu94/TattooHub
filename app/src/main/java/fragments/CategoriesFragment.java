package fragments;


import adapters.CategoryList;
import model.Category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xu_aaabeck.tattoohub.CategoryGalleryActivity;
import xu_aaabeck.tattoohub.R;

/**
 * Created by root on 15.03.18.
 */

public class CategoriesFragment extends Fragment {

    ListView listViewCategory;
    DatabaseReference categoriesRef;
    Set<String> userCategories;
    List<String> categoryList;
    String user = "Valerio Tomassi";

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

        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                Intent go = new Intent(getContext(), CategoryGalleryActivity.class);
                go.putExtra("selectedCategory", selectedItem);
                go.putExtra("user", user);
                startActivity(go);
            }
        });


        userCategories = new HashSet<>();
        categoryList = new ArrayList<>();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        categoriesRef.orderByChild("user").equalTo(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userCategories.clear();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){

                    Category category = categorySnapshot.getValue(Category.class);

                    userCategories.add(category.getName());

                }

                categoryList.clear();
                categoryList.addAll(userCategories);

                CategoryList adapter = new CategoryList(getActivity(), categoryList);
                listViewCategory.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
