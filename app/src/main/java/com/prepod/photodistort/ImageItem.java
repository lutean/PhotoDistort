package com.prepod.photodistort;

import android.net.Uri;

/**
 * Created by Anton on 13.10.2018.
 */
public class ImageItem {

    private String imageId;
    private String imageThumbPath;
    private Uri imageUri;

    public ImageItem(String imageId, String imageThumbUri) {
        this(imageId, imageThumbUri, null);
    }

    public ImageItem(String imageId, String imageThumbPath, Uri imageUri) {
        this.imageId = imageId;
        this.imageThumbPath = imageThumbPath;
        this.imageUri = imageUri;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageThumbPath() {
        return imageThumbPath;
    }

    public void setImageThumbPath(String imageThumbPath) {
        this.imageThumbPath = imageThumbPath;
    }
}
