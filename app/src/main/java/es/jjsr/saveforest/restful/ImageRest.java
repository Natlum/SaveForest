package es.jjsr.saveforest.restful;

import android.content.Context;
import android.util.Log;

//import net.gotev.uploadservice.MultipartUploadRequest;
//import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.UUID;

import es.jjsr.saveforest.resource.LoadAndSaveImage;
import es.jjsr.saveforest.resource.constants.GConstants;

/**
 * Created by José Juan Sosa Rodríguez on 27/12/2017.
 */

public class ImageRest {
    Context context;
    private static final String TAG = "IMAGEREST";
    private static final String UPLOAD_URL = GConstants.IMAGES_SERVER_ROUTE + "/upload";

    public ImageRest(Context context) {
        this.context = context;
    }

    /*public Boolean uploadImage(String fileName){
        try {
            String filePath = LoadAndSaveImage.getPathImage(context, fileName);
            //Bitmap bitmap = LoadAndSaveImage.loadImageFromStorageToSaveOnServer(context, fileName);

            String uploadId = UUID.randomUUID().toString();

            new MultipartUploadRequest(context, uploadId, UPLOAD_URL)
                .addFileToUpload(filePath, "uploadFile")
                .addParameter("uploadFile", fileName)
                .setNotificationConfig(new UploadNotificationConfig())
                .setMaxRetries(10)
                .startUpload();
            Log.i(TAG, "Upload Imagen OK");
            return true;
        } catch (FileNotFoundException e) {
            Log.i(TAG, "Fail load image to server\n" + e.getMessage());
            return false;
        } catch (MalformedURLException e) {
            Log.i(TAG, "Fail upload image to server\n" + e.getMessage());
            return false;
        }
    }*/
}
