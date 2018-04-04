package tasks;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
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

public class MYAmazonRekognition {
    public static void main(String[] args) throws Exception {

        String photo="tattoo.jpg";

        AWSCredentials credentials;
        try {
            credentials = new BasicAWSCredentials("AKIAISFJEVJFEZQAJQBQ", "lt7Omv0Qu3FhSrGj0u1ZV2RytdAfiP4HQYScQzFd");
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Usersuserid.aws/credentials), and is in a valid format.", e);
        }
        ByteBuffer imageBytes;
        InputStream inputStream =new FileInputStream(new File(photo));

        imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));



        AmazonRekognition rekognitionClient = new AmazonRekognitionClient(credentials);

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMaxLabels(10)
                .withMinConfidence(77F);

        try {

            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();

            System.out.println("Detected labels for " + photo);
            for (Label label: labels) {
                System.out.println(label.getName() + ": " + label.getConfidence().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
