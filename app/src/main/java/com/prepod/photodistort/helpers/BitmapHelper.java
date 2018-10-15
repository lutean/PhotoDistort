package com.prepod.photodistort.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.prepod.photodistort.Distortable;
import com.prepod.photodistort.R;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class BitmapHelper {

    public static Bitmap createBitmap(Context context, String imagePath) {
        if (imagePath == null) return null;
        Context activity = context;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        options.inSampleSize = 3;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
            if (bmp == null) return null;
            float size = activity.getResources().getDimension(R.dimen.filters_preview_img_size);
            int mult = Math.abs(bmp.getWidth() / (int) size);
            if (mult == 0) mult = 1;
            return Bitmap.createScaledBitmap(bmp, bmp.getWidth() / mult, bmp.getHeight() / mult, false);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createBitmapAndApplyFilter(Context context, String imagePath, Filter filter) {
        if (imagePath == null) return "";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        options.inSampleSize = 3;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
            if (bmp == null) return "";
            int mult = Math.abs(bmp.getWidth() / 512);
            if (mult == 0) mult = 1;
            bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / mult, bmp.getHeight() / mult, false);
            if (filter != null)
                applyFilter(bmp, filter);
            File file = new File(context.getExternalFilesDir(null), "photo_distorted.jpg");

            bmp.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
            return file.getAbsolutePath();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Bitmap applyFilter(Bitmap bitmap, Filter filter) {
        return filter.processFilter(bitmap);
    }

    public static <T extends Distortable> void applyFilters(List<T> items) {
        for (Distortable d : items) {
            Bitmap img = d.getImage();
            Filter filter = d.getFilter();
            if (filter == null) continue;
            d.setImage(filter.processFilter(img));
        }
    }

}
