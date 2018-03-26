package xu_aaabeck.tattoohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import classes.AuthenticationDialog;
import classes.Constants;
import interfaces.AsyncResponse;
import interfaces.AuthenticationListener;
import tasks.GetUserSelfInfoTask;

public class LoginActivity extends AppCompatActivity implements AuthenticationListener, AsyncResponse{

    private AuthenticationDialog auth_dialog;
    private Button btn_get_access_token;
    public String username;
    private String access_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_get_access_token = findViewById(R.id.btn_get_access_token);

        btn_get_access_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth_dialog = new AuthenticationDialog(LoginActivity.this, LoginActivity.this);
                auth_dialog.setCancelable(true);
                auth_dialog.show();
            }
        });
    }

    @Override
    public void onCodeReceived(String access_token) {
        if (access_token == null) {
            auth_dialog.dismiss();
        }
        this.access_token = access_token;
        new GetUserSelfInfoTask(this,"username").execute(access_token);


    }

    @Override
    public void processFinish(String result){

        this.username = String.valueOf(result.hashCode());
        ((Constants)this.getApplication()).setUsername(this.username);
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("access_token", access_token);
        startActivity(i);
    }

}
