package com.prepod.photodistort;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;

public interface Distortable {
    Filter getFilter();
    Bitmap getImage();
    void setImage(Bitmap image);
}
