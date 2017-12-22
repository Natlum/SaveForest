package es.jjsr.saveforest.resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Created by José Juan Sosa Rodríguez on 22/12/2017.
 */

public class UploadImageAsyncTask extends AsyncTask<Void, Void, Void>{

    private final String TAG = "JJSR Upload";
    private Context context;
    private String urlServer = GConstants.IMAGES_SERVER_ROUTE + "/upload/";
    private Bitmap bitmap;
    private String filename;
    MultipartEntity reqEntity;

    public UploadImageAsyncTask(Context context, Bitmap bitmap, String filename) {
        this.context = context;
        this.bitmap = bitmap;
        this.filename = filename;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), filename);

        reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("uploadFile", contentPart);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String result = uploadImage();
        if (result != null){
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, context.getResources().getString(R.string.fail_upload_image), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private String uploadImage(){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.addRequestProperty("Content-length", reqEntity.getContentLength()+"");
            connection.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = connection.getOutputStream();
            reqEntity.writeTo(os);
            os.close();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                return readStream(connection.getInputStream());
            }
        }catch (IOException e){
            Log.e(TAG, "Multipart Post error" + e + "(" + urlServer + ")");
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    private static String readStream(InputStream in){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
