package xu_aaabeck.tattoohub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by root on 20.03.18.
 */

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);
        Intent i = getIntent();

        ImageView fullView = (ImageView) findViewById(R.id.full_photo);
        Picasso.with(getApplicationContext())
                .load(getIntent().getStringExtra("photo"))
                .into(fullView);
    }
}
