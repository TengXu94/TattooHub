package xu_aaabeck.tattoohub;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import adapters.SimpleGridViewAdapter;
import classes.Constants;
import model.Category;

public class CategoryGalleryActivity extends AppCompatActivity {

    private ArrayList<String> categoryPhotos = new ArrayList<>();
    private GridView Grid;
    private SimpleGridViewAdapter adapter;
    private String username;
    private String categoryName;
    private DatabaseReference categoriesRef;
    private Set<String> userCategories;
    private ArrayList<String> tempCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_gallery);

        tempCategories = new ArrayList<>();
        userCategories = new HashSet<>();


        Intent i = getIntent();
        categoryName = i.getStringExtra("selectedCategory");
        username = ((Constants)getApplication()).getUsername();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference("categories");

        categoriesRef.child(username).child(categoryName)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                categoryPhotos.clear();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {

                    Category category = categorySnapshot.getValue(Category.class);

                    if(category.getUrl() != null)
                        categoryPhotos.add(category.getUrl());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



                adapter.notifyDataSetChanged();

            }
        });

        Grid = findViewById(R.id.MyGrid);
        adapter = new SimpleGridViewAdapter(getApplicationContext(), 0, categoryPhotos);
        adapter.notifyDataSetChanged();
        Grid.setAdapter(adapter);

        Grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                final String url = (String) Grid.getItemAtPosition(position);


                // custom dialog
                final Dialog dialog = new Dialog(CategoryGalleryActivity.this);
                dialog.setContentView(R.layout.delete_and_move_photo_dialog);
                dialog.setTitle("Modifying photo...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Do you want to remove this photo from the gallery?");

                Button dialogButtonYES = (Button) dialog.findViewById(R.id.dialogButtonYES);
                // if button is clicked, close the custom dialog
                dialogButtonYES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference toDelete = categoriesRef.child(username).child(categoryName)
                                .child(String.valueOf((username + categoryName + url).hashCode()));
                        toDelete.removeValue();

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Photo Deleted", Toast.LENGTH_LONG).show();
                    }
                });

                final Spinner spinner = dialog.findViewById(R.id.categoriesSpinner);

                Button dialogButtonMoveTo = (Button) dialog.findViewById(R.id.UpdateBtn);
                // if button is clicked, close the custom dialog
                dialogButtonMoveTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference toDelete = categoriesRef.child(username).child(categoryName)
                                .child(String.valueOf((username + categoryName + url).hashCode()));
                        toDelete.removeValue();

                        String selectedCategory = (String) spinner.getSelectedItem();

                        Category category = new Category(selectedCategory, username, url);
                        categoriesRef.child(username).child(selectedCategory).child(category.getId()).setValue(category);

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Photo Moved", Toast.LENGTH_LONG).show();
                    }
                });

                dialog.show();

                categoriesRef.child(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        userCategories.clear();

                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){

                            String nameCat = categorySnapshot.getKey();

                            if(nameCat != null && !nameCat.equals(categoryName))
                                userCategories.add(nameCat);

                        }

                        tempCategories.clear();

                        tempCategories.addAll(userCategories);

                        // Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryGalleryActivity.this,
                                android.R.layout.simple_spinner_item, tempCategories);

                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        // Apply the adapter to the spinner
                        spinner.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                return true;
            }
        });

        Grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final Object[] images = categoryPhotos.toArray();

                Intent go = new Intent(getApplicationContext(), FullImageActivity.class);
                go.putExtra("photo", (String) images[position]);
              //By position Clicked
               startActivity(go);
           }
        });

    }


    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        //finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        adapter.notifyDataSetChanged();
        finish();
    }

}
