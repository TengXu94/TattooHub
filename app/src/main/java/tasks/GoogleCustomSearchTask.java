package tasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import classes.Constants;
import interfaces.AsyncResponse;

public class GoogleCustomSearchTask  extends AsyncTask<String, Void, String> {


    private static final String google = Constants.google_api;
    public AsyncResponse delegate = null;
    private String info;

    public GoogleCustomSearchTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        try {
            URL url = new URL(google+strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            JSONObject topLevel = new JSONObject(builder.toString());
            JSONArray main = topLevel.getJSONArray("items");
            for (int i=0; i < main.length();i++) {
                result = main.getJSONObject(i).getString("link")+ ","+main.getJSONObject(i).getString("displayLink")+ " " + result;
            }
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
