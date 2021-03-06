package fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;

import adapters.InstaListViewAdapter;
import adapters.SpinnerViewAdapter;
import adapters.UrlListViewAdapter;
import classes.Data;
import classes.InstagramResponse;
import classes.RestClient;
import interfaces.AsyncResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tasks.AmazonRekognitionTask;
import tasks.GoogleCustomSearchTask;
import xu_aaabeck.tattoohub.HomeActivity;
import xu_aaabeck.tattoohub.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by root on 15.03.18.
 */

public class HomeFragment extends Fragment implements AsyncResponse, AdapterView.OnItemSelectedListener {

    private static final String start_index ="&start=";
    private int default_index;
    private EditText etSearch;
    private ListView lvFeed;
    private String query;
    private UrlListViewAdapter urlAdapter;
    private InstaListViewAdapter instaAdapter;
    private ArrayList<Data> data = new ArrayList<>();
    private View view;
    private String[] urls;
    private ArrayList<String> datas = new ArrayList<>();
    private String access_token = "";
    private Spinner spin;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private String path;
    private boolean instragram_search = false;
    private ProgressDialog dialog;
    private AlertDialog labels_dialog;

    private static int CAMERA_RESULT_LOAD_IMAGE = 0;
    private static int GALLERY_RESULT_LOAD_IMAGE = 1;
    private String[] searchEngine={"Google Custom Search", "Instagram"};


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

        //DIALOG
        dialog = new ProgressDialog(getActivity());

        //LABELS DIALOG
        labels_dialog = new AlertDialog.Builder(getActivity()).create();
        labels_dialog.setTitle("Labels Recognized");
        labels_dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

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
        lvFeed = view.findViewById(R.id.lv_feed);
        etSearch = view.findViewById(R.id.et_search);
        etSearch.setActivated(false);
        urlAdapter= new UrlListViewAdapter(getActivity(),0,datas);
        instaAdapter = new InstaListViewAdapter(getActivity(), 0, data);

        //DEFAULT ADAPTER
        lvFeed.setAdapter(urlAdapter);


        //SPINNER
        spin = view.findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);


        ArrayAdapter customAdapter=new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,
                searchEngine);
        customAdapter.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(customAdapter);
        spin.setSelection(0);

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
                        urlAdapter.clearListView();
                        instaAdapter.clearListView();
                        default_index =0;
                        fetchData(etSearch.getText().toString());
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
                if (!instragram_search && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (lvFeed.getLastVisiblePosition() - lvFeed.getHeaderViewsCount() -
                        lvFeed.getFooterViewsCount()) >= (urlAdapter.getCount() - 1)) {
                        default_index = default_index +10;
                        new GoogleCustomSearchTask(HomeFragment.this).execute(query+start_index+String.valueOf(default_index));

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getActivity().getApplicationContext(), searchEngine[position], Toast.LENGTH_LONG).show();
        if(searchEngine[position].equals("Instagram")){
            instragram_search = true;
            lvFeed.setAdapter(instaAdapter);
        }
        else {
            instragram_search = false;
            lvFeed.setAdapter(urlAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    public void fetchData(String tag) {

        if(instragram_search) {

            if(tag.split(" ").length>1){
                Toast.makeText(getActivity().getApplicationContext(), "With Instagram One Tag Search is allowed", Toast.LENGTH_LONG).show();
            }

            else {
                Call<InstagramResponse> call = RestClient.getRetrofitService().getTagPhotos(tag, access_token);
                call.enqueue(new Callback<InstagramResponse>() {
                    @Override
                    public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {

                        if (response.body() != null) {

                            for (int i = 0; i < response.body().getData().length; i++) {

                                //Double Tag Problem bypass
                                if (filter(response.body().getData()[i].getTags())) {
                                    data.add(response.body().getData()[i]);
                                }

                            }
                            instaAdapter.notifyDataSetChanged();
                        } else {
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
        }

        else {
            new GoogleCustomSearchTask(HomeFragment.this).execute(query);
        }
    }

    @Override
    public void processFinish(String result){
        //ok share
        if(result.equals("Tattoo")){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    labels_dialog.dismiss();
                    shareIntagramIntent("file://" + path);
                }
            }, 1000);
        }
        //google download
        if(!result.equals("Tattoo") && result.length() > 1) {
            this.urls = result.split(" ");
            for (int i = 0; i < urls.length; i++) {
                datas.add(urls[i]);
            }
            urlAdapter.notifyDataSetChanged();
        }
    }

    public boolean filter(Object[] tags){
        HashSet<Object> tags_set = Sets.newHashSet(tags);
        if(tags_set.contains("tattoo")) {
            return true;
        }
        else { return false;}
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

                new AmazonRekognitionTask(this,credentialsProvider, path, dialog, labels_dialog).execute();

            }
        }
        if(requestCode == GALLERY_RESULT_LOAD_IMAGE){

            if (resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                this.path = getRealPathFromURI(selectedImage);
                new AmazonRekognitionTask(this,credentialsProvider,path,dialog, labels_dialog).execute();
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
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="
                    + "com.instagram.android"));
            startActivity(intent);
        }
    }


}
