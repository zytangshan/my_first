package com.example.spreadtrumdavidzhao.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-13.
 */
public class PictureUtil {

    //scaled down
    public static BitmapDrawable getScaledDrawable(Activity activity, String path){
        Display display = activity.getWindowManager().getDefaultDisplay();
        float destwidth = display.getWidth();
        float destheight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float srcwidth = options.outWidth;
        float srcheight = options.outHeight;

        int inSampleSize = 1;

        if (srcheight > destheight || srcwidth > destwidth) {
            if (srcwidth > srcheight) {
                inSampleSize = Math.round(srcheight/srcwidth);
            } else {
                inSampleSize = Math.round(srcwidth/srcheight);
            }

        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path,options);


        return new BitmapDrawable(activity.getResources(),bitmap);

    }

    public static void cleanImageView(ImageView imageView) {

        // free bitmap ,maybe not must . free bitmap when gc,but no one know time
        // we should recycle() to free bitmap

        if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
            return;
        }

        BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
        bitmapDrawable.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }
}
