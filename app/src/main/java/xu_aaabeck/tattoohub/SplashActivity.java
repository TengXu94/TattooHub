package xu_aaabeck.tattoohub;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.amazonaws.mobile.client.AWSMobileClient;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.squareup.picasso.Picasso;

import classes.Constants;
import interfaces.AsyncResponse;
import model.Category;
import tasks.GetUserSelfInfoTask;

/**
 * Created by White_Orchard on 13/03/2018.
 */

public class SplashActivity extends AppCompatActivity implements AsyncResponse {

    private static int SPLASH_TIME_OUT = 2800;
    private ImageView lv;
    private SharedPreferences prefs = null;
    private String username;
    protected Intent jump_to_home;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AWSMobileClient.getInstance().initialize(this).execute();
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("shared",MODE_PRIVATE);
        lv = findViewById(R.id.lv);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.logo_transition);
        lv.startAnimation(anim);
        boolean logged = prefs.getBoolean("logged",false);
        if(logged) {
            this.access_token= prefs.getString("access_token",null);
            new GetUserSelfInfoTask(this,"username").execute(access_token);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(jump_to_home);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else{
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

    @Override
    public void processFinish(String result){
        this.username = String.valueOf(result);
        ((Constants)this.getApplication()).setUsername(String.valueOf(this.username.hashCode()));
        ((Constants)this.getApplication()).setHash(this.username);
        this.jump_to_home = new Intent(this, HomeActivity.class);
        this.jump_to_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.jump_to_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.jump_to_home.putExtra("access_token", access_token);
    }
}
