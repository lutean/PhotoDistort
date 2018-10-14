package com.prepod.photodistort;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;


public class FilterItem implements Distortable{

    private Bitmap image;
    private Filter filter;

    public FilterItem(Bitmap image, Filter filter) {
        this.image = image;
        this.filter = filter;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
