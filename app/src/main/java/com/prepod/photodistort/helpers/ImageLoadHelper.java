package com.prepod.photodistort.helpers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.prepod.photodistort.models.ImageItem;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Images.Media.query;

public class ImageLoadHelper {

    public static List<ImageItem> getImages(ContentResolver contentResolver){


        List<ImageItem> imageList = new ArrayList<ImageItem>();
        String[] cols = {MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};

        Cursor cursorThumb = MediaStore.Images.Thumbnails.queryMiniThumbnails(contentResolver,
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Thumbnails.MINI_KIND,
                cols);

        int idColumnIndex = cursorThumb.getColumnIndex(cols[0]);
        int thumbColumnIndex = cursorThumb.getColumnIndex(cols[1]);

            while (cursorThumb.moveToNext()) {
                String thumb = cursorThumb.getString(thumbColumnIndex);
                long id = cursorThumb.getLong(idColumnIndex);
//                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                imageList.add(new ImageItem(id, thumb));
            }

        return imageList;
    }

    public static String getFullImage(ContentResolver contentResolver, long imageId){

        String[] cols = {MediaStore.Images.Media.DATA};

        Cursor cursor = query(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                cols,
                MediaStore.Images.Media._ID + "=?",
                new String[] {String.valueOf(imageId)},
                null);
        int imageColumn = cursor.getColumnIndex(cols[0]);
        if (cursor.moveToNext()) {
                String fullImagePath = cursor.getString(imageColumn);
                cursor.close();
               // return Uri.parse("content://" + fullImageUri);
                return fullImagePath;
        } else {
            cursor.close();
            return "";
        }
    }

}
