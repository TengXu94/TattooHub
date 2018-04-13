package adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import classes.Constants;
import model.Category;
import xu_aaabeck.tattoohub.FullImageActivity;
import xu_aaabeck.tattoohub.R;

public class UrlListViewAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> data;
    private DatabaseReference categoriesRef;
    private Set<String> userCategories;
    private ArrayList<String> tempCategories;
    private String username;
    private Spinner spinner;

    public UrlListViewAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.data = objects;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference("categories");

        userCategories = new HashSet<>();
        tempCategories = new ArrayList<>();

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View curView = convertView;
        if (curView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.feed_list_view_item, null);
        }
        TextView tv_user_fullname = (TextView) curView.findViewById(R.id.tv_user_fullname);
        final ImageView iv_photo = (ImageView) curView.findViewById(R.id.iv_photo);

        username = ((Constants) context.getApplicationContext()).getUsername();


        tv_user_fullname.setText(data.get(position).split(",")[1]);

        iv_photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.category_save_pop);
                dialog.setTitle("Saving photo in a category...");

                spinner = dialog.findViewById(R.id.categoriesSpinner);

                final EditText newCat = dialog.findViewById(R.id.newCategoryTextEdit);
                newCat.setVisibility(View.INVISIBLE);
                Button saveButton= dialog.findViewById(R.id.saveButton);

                spinner.setPrompt("Select a category");

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String selectedCategory = (String) spinner.getSelectedItem();

                        if(selectedCategory.equals(context.getString(R.string.newCategory)))
                            newCat.setVisibility(View.VISIBLE);
                        else newCat.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                            newCat.setVisibility(View.INVISIBLE);
                    }
                });

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String selectedCategory = null;
                        String newCategory = null;

                        Category category = new Category();

                        selectedCategory = (String) spinner.getSelectedItem();

                        if(selectedCategory.equals(context.getString(R.string.newCategory))) {

                            newCategory = (String) newCat.getText().toString();

                            category = new Category(newCategory, username, data.get(position).split(",")[0]);

                        }

                        else {

                            category = new Category(selectedCategory, username, data.get(position).split(",")[0]);

                        }

                        Log.v("category new", newCategory);

                        if(newCategory != null && !newCategory.equals("")) {
                            categoriesRef.child(username).child(category.getName()).child(category.getId()).setValue(category);
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else Toast.makeText(context, "Specify new category name!", Toast.LENGTH_SHORT).show();
                    }
                });


                dialog.show();

                categoriesRef.child(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        userCategories.clear();

                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){

                            String categoryName = categorySnapshot.getKey();

                            if(categoryName != null)
                                userCategories.add(categoryName);

                        }

                        tempCategories.clear();

                        tempCategories.addAll(userCategories);
                        tempCategories.add(0, context.getString(R.string.newCategory));

                        // Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.getApplicationContext(),
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



        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullImageActivity.class);
                i.putExtra("photo", data.get(position).split(",")[0]);
                context.startActivity(i);
            }
        });



        Picasso.with(context)
                .load(data.get(position).split(",")[0]).fit()
                .into(iv_photo);

        return curView;
    }

    public void clearListView() {
        data.clear();
    }

}
