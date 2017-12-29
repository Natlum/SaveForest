package es.jjsr.saveforest.volley;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Created by José Juan Sosa Rodríguez on 29/12/2017.
 */

public class UploadImage {

    private static final String TAG = "JJSR IMAGEHTTP";
    private static final String UPLOAD_URL = GConstants.IMAGES_SERVER_ROUTE + "/upload";
    private static long totalSize = 0;

    public static void upload(String filePath, String fileName){
        new UploadFileToServer(filePath, fileName).execute();
    }

    /**
     * Uploading the file to server
     * */
    private static class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        private String filePath;
        private String fileName;

        public UploadFileToServer(String filePath, String fileName) {
            this.filePath = filePath;
            this.fileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.i(TAG, "Progress UPLOAD: " + progress);
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("uploadFile", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("fileName", new StringBody(fileName));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            super.onPostExecute(result);
        }

    }

}
