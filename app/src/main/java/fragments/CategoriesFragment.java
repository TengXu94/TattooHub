package fragments;


import adapters.CategoryList;
import classes.Constants;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class CategoriesFragment extends Fragment{

    private ListView listViewCategory;
    private DatabaseReference categoriesRef;
    private Set<String> userCategories;
    private List<String> categoryList;
    private String username;


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
        this.username = ((Constants)getActivity().getApplication()).getUsername();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference("categories");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_fragment, container, false);

        listViewCategory = (ListView) view.findViewById(R.id.categoriesListView);

        listViewCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                Log.v("long clicked","pos: " + pos);
                final String selectedCategory = (String) listViewCategory.getItemAtPosition(pos);
                Log.v("long clicked","pos: " + selectedCategory + username);

                // custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.delete_category_dialog);
                dialog.setTitle("Deleting Category...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Do you want to delete category \"" + selectedCategory + "\"?");


                Button dialogButtonYES = (Button) dialog.findViewById(R.id.dialogButtonYES);
                // if button is clicked, close the custom dialog
                dialogButtonYES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference toDelete = categoriesRef.child(username).child(selectedCategory);
                        toDelete.removeValue();

                        dialog.dismiss();


                        Toast.makeText(getContext(), "Category Deleted", Toast.LENGTH_LONG).show();
                    }
            });


                dialog.show();

                return true;
            }
        });


        userCategories = new HashSet<>();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume(){
        super.onResume();

        categoriesRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                categoryList = new ArrayList<>();

                userCategories.clear();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){

                    String categoryName = categorySnapshot.getKey();

                    userCategories.add(categoryName);

                }

                categoryList.clear();
                categoryList.addAll(userCategories);


                if(getActivity() != null) {
                    CategoryList adapter = new CategoryList(getActivity(), categoryList);
                    listViewCategory.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                Intent go = new Intent(getContext(), CategoryGalleryActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                go.putExtra("selectedCategory", selectedItem);
                startActivity(go);
            }
        });

    }


}
