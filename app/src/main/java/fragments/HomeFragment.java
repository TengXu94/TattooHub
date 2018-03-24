package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;

import classes.Data;
import classes.InstagramResponse;
import classes.RestClient;
import adapters.SimpleListViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xu_aaabeck.tattoohub.R;

/**
 * Created by root on 15.03.18.
 */

public class HomeFragment extends Fragment {

    private EditText etSearch;
    private ListView lvFeed;
    int count = 0;

    private boolean hasFocus;
    private SimpleListViewAdapter lvAdapter;
    private ArrayList<Data> data = new ArrayList<>();
    private View view;
    private String access_token = "";

    public static HomeFragment newInstance() {
        HomeFragment fragmentFirst = new HomeFragment();
        Bundle args = new Bundle();
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("access_token", access_token);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);
        Intent i = getActivity().getIntent();
        access_token = i.getStringExtra("access_token");
        lvFeed = (ListView) view.findViewById(R.id.lv_feed);
        etSearch = (EditText) view.findViewById(R.id.et_search);

        lvAdapter = new SimpleListViewAdapter(getActivity(), 0, data);
        lvFeed.setAdapter(lvAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // Don't search if the etSearch is emtpy when pressing the done button
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if(etSearch.getText().length() <= 0){
                        Toast.makeText(getActivity().getApplicationContext(), "Enter a search tag", Toast.LENGTH_SHORT).show();

                    } else {

                        lvAdapter.clearListView();
                        fetchData(etSearch.getText().toString());
                        etSearch.setText("");
                        etSearch.clearFocus();
                    }

                    // Close the soft keyboard
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }


    public void fetchData(String tag) {
        Call<InstagramResponse> call = RestClient.getRetrofitService().getTagPhotos(tag, access_token);
        call.enqueue(new Callback<InstagramResponse>() {
            @Override
            public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {

                if (response.body() != null) {

                    for(int i = 0; i < response.body().getData().length; i++){

                        //Double Tag Problem bypass
                        if(filter(response.body().getData()[i].getTags())) {
                            data.add(response.body().getData()[i]);
                        }

                    }
                    lvAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {
                //Handle failure
                Toast.makeText(getActivity().getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                System.out.println(t);
            }
        });
    }

    public boolean filter(Object[] tags){
        HashSet<Object> tags_set = Sets.newHashSet(tags);
        return tags_set.contains("tattoo");
    }

}
