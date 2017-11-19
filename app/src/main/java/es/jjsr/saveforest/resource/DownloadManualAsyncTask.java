package es.jjsr.saveforest.resource;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import es.jjsr.saveforest.R;

import static java.lang.Thread.sleep;


/**
 * Clase que permite descargar un pdf pasado por parámetros, y luego valida si hay algún visor pdf,
 * para mostrarlo.
 * Created by José Juan Sosa Rodríguez on 19/11/2017.
 */

public class DownloadManualAsyncTask extends AsyncTask<String, Integer, String>{

    private Context context;
    private ProgressDialog progressDialog;
    private String filepath = "";

    public DownloadManualAsyncTask(Context context, ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
        filepath = context.getApplicationContext().getFilesDir() + "/ManualApp.pdf";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... urlPDF) {
        String urlToDownload = urlPDF[0];
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            URL url = new URL(urlToDownload);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return context.getResources().getString(R.string.fail_connection_download);
            }

            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(filepath);

            int fileWeight = connection.getContentLength();
            byte[] data = new byte[1024];
            int count = 0;
            int progress = 0;

            while ((count = inputStream.read(data)) != -1){
                sleep(10);
                outputStream.write(data, 0, count);
                progress += count;
                publishProgress((int) (progress * 100 / fileWeight));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error: " +e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " +e.getMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return context.getResources().getString(R.string.correct_connection_download);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);
        progressDialog.dismiss();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        openFile();
    }

    private void openFile(){
        File file = new File(filepath);

        if (file.exists()){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(filepath));
            intent.setType("application/pdf");

            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            if (activities.size() > 0){
                context.startActivity(intent);
            }else {
                Toast.makeText(context, context.getResources().getString(R.string.not_found_pdf_viewer), Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(context, context.getResources().getString(R.string.fail_save_manual), Toast.LENGTH_LONG).show();
        }
    }
}
