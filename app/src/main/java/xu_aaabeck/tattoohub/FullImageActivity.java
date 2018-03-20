package xu_aaabeck.tattoohub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by root on 20.03.18.
 */

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);

        Intent i = getIntent();

        // Selected image id
        int position1 = i.getExtras().getInt("id");

        ImageView imageView = (ImageView) findViewById(R.id.full_photo);
        imageView.setImageResource();
    }
}
