package tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import interfaces.AsyncResponse;

/**
 * Created by root on 20.03.18.
 */

public class GetUserSelfInfoTask extends AsyncTask<String, Void, String> {


    private static final String GET_SELF_URL = "https://api.instagram.com/v1/users/self/?access_token=";
    public AsyncResponse delegate = null;
    private String info;

    public GetUserSelfInfoTask(AsyncResponse delegate, String infoNeeded) {
        this.info = infoNeeded;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "UNDEFINED";
        try {
            URL url = new URL(GET_SELF_URL+strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            JSONObject topLevel = new JSONObject(builder.toString());
            JSONObject main = topLevel.getJSONObject("data");
            result = String.valueOf(main.getString(info));

            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}