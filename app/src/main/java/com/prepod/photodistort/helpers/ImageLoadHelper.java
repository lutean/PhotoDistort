package com.prepod.photodistort.helpers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.prepod.photodistort.models.ImageItem;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Images.Media.query;
import static android.support.constraint.Constraints.TAG;

public class ImageLoadHelper {

    private static String getFilterPath() {
        return ("/DCIM/");//Camera
    }

    public static List<ImageItem>  addLocalItems(ContentResolver contentResolver) {
        ContentResolver resolver = contentResolver;
        List<ImageItem> imageList = new ArrayList<ImageItem>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC};
        String selection = MediaStore.Images.Media.DATA + " like '%%%" + getFilterPath() + "%%'";
        String[] selectionArgs = {};
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC," + MediaStore.Images.Media._ID + " DESC";//MediaStore.Images.Media.DEFAULT_SORT_ORDER;

        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);

        String path;
        int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        int imageIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int thumbIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MINI_THUMB_MAGIC);

        int initedLocalCount = 0;
        while(cursor.moveToNext()) {
            String thumb = cursor.getString(dataIndex);
            long id = cursor.getLong(imageIDIndex);
            imageList.add(new ImageItem(id, thumb));
        }

        cursor.close();
        return imageList;
    }



    public static List<ImageItem> getImages(ContentResolver contentResolver) {
        List<ImageItem> imageList = new ArrayList<ImageItem>();
        String[] cols = {MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};

        Cursor cursorThumb = MediaStore.Images.Thumbnails.queryMiniThumbnails(contentResolver,
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Thumbnails.MICRO_KIND,
                cols);

        int idColumnIndex = cursorThumb.getColumnIndex(cols[0]);
        int thumbColumnIndex = cursorThumb.getColumnIndex(cols[1]);

        while (cursorThumb.moveToNext()) {
            String thumb = cursorThumb.getString(thumbColumnIndex);
            long id = cursorThumb.getLong(idColumnIndex);
            imageList.add(new ImageItem(id, thumb));
        }
        cursorThumb.close();
        return imageList;
    }

    public static String getFullImage(ContentResolver contentResolver, long imageId) {
        String[] cols = {MediaStore.Images.Media.DATA};

        Cursor cursor = query(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                cols,
                MediaStore.Images.Media._ID + "=?",
                new String[]{String.valueOf(imageId)},
                null);
        int imageColumn = cursor.getColumnIndex(cols[0]);
        if (cursor.moveToNext()) {
            String fullImagePath = cursor.getString(imageColumn);
            cursor.close();
            return fullImagePath;
        } else {
            cursor.close();
            return "";
        }
    }

}
