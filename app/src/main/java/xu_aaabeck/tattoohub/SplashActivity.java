package xu_aaabeck.tattoohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.squareup.picasso.Picasso;

import model.Category;

/**
 * Created by White_Orchard on 13/03/2018.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;
    private ImageView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("categories");

        final Category category = new Category("tattoo brutti", "Valerio Tomassi", "https://www.gstatic.com/webp/gallery/1.jpg");


        myRef.child(category.getId()).setValue(category);

        lv = (ImageView) findViewById(R.id.lv);

        Category category1;

        myRef.orderByChild("id").equalTo("-2032392465").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Category cat = dataSnapshot.getValue(Category.class);
                System.out.println(dataSnapshot.getKey() + " was OOOOOOOOOOOOOOOOO" + cat.getUrl() + " meters tall.");

                Picasso.with(getApplicationContext())
                        .load(cat.getUrl())
                        .resize(100, 100)
                        .centerInside()
                        .into(lv);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        Animation anim = AnimationUtils.loadAnimation(this, R.anim.logo_transition);
        lv.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
