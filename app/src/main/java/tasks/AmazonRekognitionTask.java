package tasks;

import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import interfaces.AsyncResponse;

public class AmazonRekognitionTask extends AsyncTask<String, Void, String> {


    public AsyncResponse delegate = null;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private String path;

    public AmazonRekognitionTask(AsyncResponse delegate, CognitoCachingCredentialsProvider credentialsProvider,
                                  String path) {
        this.delegate = delegate;
        this.credentialsProvider = credentialsProvider;
        this.path = path;
    }

    @Override
    protected String doInBackground(String... strings) {

        ByteBuffer imageBytes = null;

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
                    if(label.getName().equals("Tattoo")){
                        System.out.println("SAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        return label.getName();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("FILE NOT FOUND");
        }
        return "";


    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.processFinish(result);
    }
}