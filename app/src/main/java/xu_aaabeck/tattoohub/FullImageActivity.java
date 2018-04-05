package xu_aaabeck.tattoohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by root on 20.03.18.
 */

public class FullImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);

        ImageView fullView = findViewById(R.id.full_photo);
        Picasso.with(getApplicationContext())
                .load(getIntent().getStringExtra("photo"))
                .into(fullView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
