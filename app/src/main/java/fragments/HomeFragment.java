package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.google.common.collect.Sets;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import adapters.UrlListViewAdapter;
import classes.Constants;
import classes.Data;
import classes.InstagramResponse;
import classes.RestClient;
import interfaces.AsyncResponse;
import model.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tasks.AmazonRekognitionTask;
import tasks.GoogleCustomSearchTask;
import xu_aaabeck.tattoohub.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by root on 15.03.18.
 */

public class HomeFragment extends Fragment implements AsyncResponse{

    private static final String start_index ="&start=";
    private int default_index;
    private EditText etSearch;
    private ListView lvFeed;
    private String query;
    private UrlListViewAdapter lvAdapter;
    private ArrayList<Data> data = new ArrayList<>();
    private View view;
    private String[] urls;
    private ArrayList<String> datas = new ArrayList<>();
    private String access_token = "";
    private CognitoCachingCredentialsProvider credentialsProvider;
    private String path;


    private static int CAMERA_RESULT_LOAD_IMAGE = 0;
    private static int GALLERY_RESULT_LOAD_IMAGE = 1;


    public static HomeFragment newInstance() {
        HomeFragment fragmentFirst = new HomeFragment();
        Bundle args = new Bundle();
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        default_index = 1;

        //HIDE keyboard when the fragment is created
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Ignore URI exposed error
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //AWS credentials
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),
                "us-east-1:7abffe46-aaa6-40a4-934c-e2e707f38fdc", // Identity pool ID
                Regions.US_EAST_1 // Region
        );
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
        etSearch.setActivated(false);
        lvAdapter= new UrlListViewAdapter(getActivity(),0,datas);
        //lvAdapter = new SimpleListViewAdapter(getActivity(), 0, data);
        lvFeed.setAdapter(lvAdapter);


        FloatingActionButton galleryBtn = view.findViewById(R.id.file);
        FloatingActionButton cameraBtn = view.findViewById(R.id.camera);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i , CAMERA_RESULT_LOAD_IMAGE);
            }
        });


        galleryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_RESULT_LOAD_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etSearch.setTextColor(Color.parseColor("#FFFFFF"));
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // Don't search if the etSearch is emtpy when pressing the done button
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if(etSearch.getText().length() <= 0){
                        Toast.makeText(getActivity().getApplicationContext(), "Enter a search tag", Toast.LENGTH_SHORT).show();

                    } else {
                        query = etSearch.getText().toString().replace(" ", "+");
                        lvAdapter.clearListView();
                        new GoogleCustomSearchTask(HomeFragment.this).execute(query);
                        //fetchData(etSearch.getText().toString());
                        etSearch.setText(etSearch.getText().toString());
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

    @Override
    public void onResume(){
        super.onResume();
        lvFeed.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (lvFeed.getLastVisiblePosition() - lvFeed.getHeaderViewsCount() -
                        lvFeed.getFooterViewsCount()) >= (lvAdapter.getCount() - 1)) {
                        default_index = default_index +10;
                        new GoogleCustomSearchTask(HomeFragment.this).execute(query+start_index+String.valueOf(default_index));

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
            }
        });
    }

    @Override
    public void processFinish(String result){
        //ok share
        if(result.equals("Tattoo")){
            shareIntagramIntent("file://" + path);
        }
        //google download
        if(!result.equals("Tattoo") && result.length() > 1) {
            this.urls = result.split(" ");
            System.out.println(urls);
            for (int i = 0; i < urls.length; i++) {
                datas.add(urls[i]);
            }
            lvAdapter.notifyDataSetChanged();
        }
        else{
            //POP UP is not a tattoo!!
            showPopup();
        }
    }

    public boolean filter(Object[] tags){
        HashSet<Object> tags_set = Sets.newHashSet(tags);
        return true;
    }


    // Floating Buttons support methods


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_RESULT_LOAD_IMAGE){
            if (data != null) {

                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();

                this.path = cursor.getString(column_index_data);

                new AmazonRekognitionTask(this,credentialsProvider, path).execute();

            }
        }
        if(requestCode == GALLERY_RESULT_LOAD_IMAGE){

            if (resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                this.path = getRealPathFromURI(selectedImage);
                new AmazonRekognitionTask(this,credentialsProvider,path).execute();
            }
        }


    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void shareIntagramIntent(String path) {
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
            shareIntent.setType("image/*");

            startActivity(shareIntent);
        } else {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="
                    + "com.instagram.android"));
            startActivity(intent);
        }
    }

    public void showPopup() {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("It seems that there is any tattoo in this image/photo please zoom on the tattoo if we are wrong");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
