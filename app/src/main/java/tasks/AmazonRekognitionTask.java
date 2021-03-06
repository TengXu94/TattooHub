package tasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import interfaces.AsyncResponse;

public class AmazonRekognitionTask extends AsyncTask<String, Void, String> {

    private ProgressDialog mProgressDialog;
    private AsyncResponse delegate = null;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private String path;
    private AlertDialog alertDialog;
    private boolean tattoo_found;

    public AmazonRekognitionTask(AsyncResponse delegate, CognitoCachingCredentialsProvider credentialsProvider,
                                 String path, ProgressDialog dialog, AlertDialog alertDialog) {
        this.delegate = delegate;
        this.credentialsProvider = credentialsProvider;
        this.path = path;
        this.mProgressDialog = dialog;
        this.alertDialog = alertDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Set your ProgressBar Title
        mProgressDialog.setTitle("Task");
        // Set your ProgressBar Message
        mProgressDialog.setMessage("Recognizing any tattoos in the uploaded image... Please Wait!");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressNumberFormat(null);

        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Show ProgressBar
        mProgressDialog.setCancelable(false);
        //  mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
    @Override
    protected String doInBackground(String... strings) {
        tattoo_found = false;
        ByteBuffer imageBytes = null;

        String message = "";
        String output ="";
        try (InputStream inputStream = new FileInputStream(new File(path))) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (imageBytes != null) {
            AmazonRekognition rekognition = new AmazonRekognitionClient(credentialsProvider);

            DetectLabelsRequest request = new DetectLabelsRequest()
                    .withImage(new Image()
                            .withBytes(imageBytes))
                    .withMaxLabels(10)
                    .withMinConfidence(60F);

            try {

                DetectLabelsResult result = rekognition.detectLabels(request);
                List<Label> labels = result.getLabels();

                System.out.println("Detected labels for " + path);
                for (Label label : labels) {
                    System.out.println(label.getName() + ": " + label.getConfidence().toString());
                    message = message + label.getName() + ": " + label.getConfidence().toString() + "\n";
                    if(label.getName().equals("Tattoo")){
                        output = label.getName();
                        tattoo_found = true;
                    }
                }
                if(!tattoo_found){
                    message = message + " It seems there is any tattoo in the image, please take a photo again";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("FILE NOT FOUND");
        }
        alertDialog.setMessage(message);
        return output;


    }

    @Override
    protected void onPostExecute(String result) {
        mProgressDialog.setProgress(100);
        mProgressDialog.dismiss();
        alertDialog.show();
        this.delegate.processFinish(result);
    }
}