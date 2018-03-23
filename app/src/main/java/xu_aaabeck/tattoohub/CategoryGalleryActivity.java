package xu_aaabeck.tattoohub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapters.SimpleGridViewAdapter;
import model.Category;

public class CategoryGalleryActivity extends AppCompatActivity {

    ArrayList<String> categoryPhotos;
    GridView Grid;
    SimpleGridViewAdapter adapter;
    String user;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_gallery);


        Intent i = getIntent();
        categoryName = i.getStringExtra("selectedCategory");
        user = i.getStringExtra("user");


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoriesRef = database.getReference("categories");

        categoryPhotos = new ArrayList<>();

        categoriesRef.orderByChild("composite_id").equalTo(String.valueOf((user + categoryName).hashCode()))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {

                    Category category = categorySnapshot.getValue(Category.class);

                    if(category.getUrl() != null)
                        categoryPhotos.add(category.getUrl());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Grid = (GridView) findViewById(R.id.MyGrid);
        adapter = new SimpleGridViewAdapter(getApplicationContext(), 0, categoryPhotos);

        Grid.setAdapter(adapter);

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
}
