package com.prepod.photodistort.models;

import android.graphics.Bitmap;

import com.prepod.photodistort.Distortable;
import com.zomato.photofilters.imageprocessors.Filter;


public class FilterItem implements Distortable {

    private Bitmap image;
    private Filter filter;
    private String title;

    public FilterItem(Bitmap image, Filter filter, String title) {
        this.image = image;
        this.filter = filter;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
