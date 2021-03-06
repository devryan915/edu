package org.shenit.tutorial.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 江老师 on 5/25/16.
 */
public final class TutorialUtils {
    private static final Gson GSON;
    static{
        GSON = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public static void bind(final Activity act,int rid, final Class<?> actClass) {
        Button btn = (Button) act.findViewById(rid);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.startActivity(new Intent(act, actClass));
            }
        });
    }

    /**
     * Get gson instance
     * @return
     */
    public static Gson gson(){
        return GSON;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Save url resource to file.
     * @param urlStr URL string
     * @param dir Dir to save the file
     * @param bw Bandwidth to use.
     */
    public static File saveToFile(String urlStr, File dir, int bw){
        FileOutputStream fos = null;
        URL url = null;
        URLConnection conn = null;
        File file = null;
        try {
            url = new URL(urlStr);
            //get the last part as file name in url path
            String fileName = url.getPath();
            fileName = fileName.substring(fileName.lastIndexOf("/"));

            file = new File(dir,fileName);
            fos =  new FileOutputStream(file);
            conn = url.openConnection();
            InputStream is = conn.getInputStream();
            byte[] buff = new byte[bw];
            int read;
            while(is.available()> 0){
                read = is.read(buff);
                fos.write(buff,0,read);
                fos.flush();
            }
        } catch (IOException e) {
            Log.e(TutorialUtils.class.getSimpleName(),"Error saving resources["+urlStr+"] to file["+file+"]",e);
        }finally{
            IOUtils.close(conn);
            IOUtils.closeQuietly(fos);
        }
        return file;
    }

    /**
     * Get bitmap from a resource by its id
     * @param ctx
     * @param rid
     * @return
     */
    public static Bitmap bitmap(Context ctx, int rid) {
        return ((BitmapDrawable)ctx.getDrawable(rid)).getBitmap();
    }

    /**
     * Fill text view with a specific text
     * @param act
     * @param id
     * @param text
     */
    public static void fillText(Activity act,int id,String text) {
        TextView textView = (TextView)act.findViewById(id);
        textView.setText(text);
    }

    public static void toggleVisible(int disappearFlag, View... views) {
        for(View v : views){
            if(v != null) v.setVisibility(isVisible(v) ? disappearFlag: View.VISIBLE );
        }
    }

    /**
     * Toggle Invisible value from views
     * @param views
     */
    public static void toggleInvisible(View... views) {
        toggleVisible(View.INVISIBLE, views);
    }

    /**
     * Toggle Gone value from views
     * @param views
     */
    public static void toggleGone(View... views) {
        toggleVisible(View.GONE, views);
    }

    public static boolean isVisible(View v){
        return v != null && v.getVisibility() == View.VISIBLE;
    }
}
