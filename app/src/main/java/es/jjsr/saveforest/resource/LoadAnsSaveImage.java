package es.jjsr.saveforest.resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Clase que permite guardar una imagen en la memoria del dispositivo.
 * También permite obtener una imagen del dispositivo.
 * Created by José Juan Sosa Rodríguez on 12/11/2017.
 */

public class LoadAnsSaveImage {

    private final static String NAME_FOLDER_IMAGES = "Images";

    public static void loadImageFromStorage(Context ctx, String fileName, ImageView imageView) throws FileNotFoundException {
        //File file = ctx.getFileStreamPath(fileName);
        File file = new File(ctx.getFilesDir() + File.separator + NAME_FOLDER_IMAGES + File.separator + fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        imageView.setImageBitmap(bitmap);
    }

    public static void saveImage(Bitmap image, Context ctx, String fileName) throws IOException {
        /*FileOutputStream fileOutputStream = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
        image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();*/
        File folder = new File(ctx.getFilesDir() + File.separator + NAME_FOLDER_IMAGES);
        if (!folder.exists()){
            folder.mkdir();
        }

        File file = new File(ctx.getFilesDir() + File.separator + NAME_FOLDER_IMAGES + File.separator + fileName);

        FileOutputStream outputStream;

        if (!file.exists()){
            file.createNewFile();
        }

        outputStream = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.close();
    }

    public static Bitmap loadImageFromStorageToSaveOnServer(Context ctx, String fileName) throws FileNotFoundException {
        //File file = ctx.getFileStreamPath(fileName);
        File file = new File(ctx.getFilesDir() + File.separator + NAME_FOLDER_IMAGES + File.separator + fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        return bitmap;
    }

}
