package xu_aaabeck.tattoohub;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Category;

/**
 * Created by White_Orchard on 20/03/2018.
 */

public class CategorySavePop extends Activity implements AdapterView.OnItemSelectedListener{

    DatabaseReference categoriesRef;
    Set<String> userCategories;
    String username;
    String photo;
    Spinner spinner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_save_pop);

        photo = getIntent().getStringExtra("photo");
        username = getIntent().getStringExtra("username");

        spinner = (Spinner) findViewById(R.id.categoriesSpinner);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference("categories");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8),(int) (height * .6));

        userCategories = new HashSet<>();


        final EditText newCat = (EditText) findViewById(R.id.newCategoryTextEdit);
        Button saveButton= (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedCategory = (String) spinner.getSelectedItem();

                if(selectedCategory.equals(getString(R.string.newCategory))) {
                    String newCategory = "";

                    newCategory = (String) newCat.getText().toString();

                    if (newCategory.isEmpty())
                        Toast.makeText(getApplicationContext(), "Specify a New Category", Toast.LENGTH_SHORT).show();
                    else {

                        final Category category = new Category(newCategory, username, photo);

                        categoriesRef.child(category.getId()).setValue(category);
                    }
                }

                else {

                    final Category category = new Category(selectedCategory, username, photo);

                    categoriesRef.child(category.getId()).setValue(category);
                }


            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        categoriesRef.orderByChild("user").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userCategories.clear();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){

                    Category category = categorySnapshot.getValue(Category.class);

                    userCategories.add(category.getName());

                }

                ArrayList<String> tempCategories = new ArrayList<>();
                tempCategories.addAll(userCategories);
                tempCategories.add(0, getString(R.string.newCategory));

                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
