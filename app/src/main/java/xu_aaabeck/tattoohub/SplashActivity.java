package xu_aaabeck.tattoohub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by White_Orchard on 13/03/2018.
 */

public class SplashActivity extends AppCompatActivity{

    private static int SPLASH_TIME_OUT = 4000;
    private ImageView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        lv = (ImageView) findViewById(R.id.lv);
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
