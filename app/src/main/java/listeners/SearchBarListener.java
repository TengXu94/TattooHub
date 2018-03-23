package listeners;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.SimpleListViewAdapter;
import classes.Data;
import classes.InstagramResponse;
import classes.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 22.03.18.
 */

public class SearchBarListener implements TextView.OnEditorActionListener {
    private Context context;
    private SimpleListViewAdapter lvAdapter;
    private EditText etSearch;
    private String access_token;
    private Activity activity;
    private ArrayList<Data> data = new ArrayList<>();

    public SearchBarListener(Context context, SimpleListViewAdapter lvAdapter, EditText etSearch, String access_token) {
        this.context = context;
        this.lvAdapter = lvAdapter;
        this.etSearch = etSearch;
        this.access_token = access_token;
        this.activity = (Activity)context;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        // Don't search if the etSearch is emtpy when pressing the done button
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if(etSearch.getText().length() <= 0){
                Toast.makeText(context, "Enter a search tag", Toast.LENGTH_SHORT).show();

            } else {
                lvAdapter.clearListView();
                fetchData(etSearch.getText().toString());
                etSearch.setText("");
                etSearch.clearFocus();
            }

            // Close the soft keyboard
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            return true;
        }
        return false;
    }

    public void fetchData(String tag) {
        Call<InstagramResponse> call = RestClient.getRetrofitService().getTagPhotos(tag, access_token);
        call.enqueue(new Callback<InstagramResponse>() {
            @Override
            public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {

                if (response.body() != null) {
                    for(int i = 0; i < response.body().getData().length; i++){
                        data.add(response.body().getData()[i]);
                    }
                    lvAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {
                //Handle failure
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
