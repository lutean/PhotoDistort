package com.prepod.photodistort;

import android.net.Uri;

/**
 * Created by Anton on 13.10.2018.
 */
public class ImageItem {

    private String imageId;
    private Uri imageThumbUri;
    private Uri imageUri;

    public ImageItem(String imageId, Uri imageThumbUri) {
        this.imageId = imageId;
        this.imageThumbUri = imageThumbUri;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Uri getImageThumbUri() {
        return imageThumbUri;
    }

    public void setImageThumbUri(Uri imageThumbUri) {
        this.imageThumbUri = imageThumbUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
