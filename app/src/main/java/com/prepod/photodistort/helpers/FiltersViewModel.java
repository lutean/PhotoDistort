package com.prepod.photodistort.helpers;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.prepod.photodistort.Distortable;
import com.prepod.photodistort.R;
import com.prepod.photodistort.models.FilterItem;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FiltersViewModel extends AndroidViewModel {

    MutableLiveData<List<FilterItem>> filterItemsData;
    SingleLiveEvent<String> filteredImage;
    private DispatchThread dispatchThread = new DispatchThread("Filters");

    public FiltersViewModel(@NonNull Application application) {
        super(application);
        filteredImage = new SingleLiveEvent<>();
    }

    public LiveData<List<FilterItem>> createFiltersPreviewList(String imagePath) {
        if (filterItemsData == null) {
            filterItemsData = new MutableLiveData<>();
            createList(imagePath);
        }
        return filterItemsData;
    }

    public LiveData<String> getFilteredImage(String imagePath, Filter filter) {
        createBitmapAndApplyFilter(imagePath, filter);
        return filteredImage;
    }

    private void createList(final String imagePath) {
        dispatchThread.postRunnable(() -> {
            List<FilterItem> filterItems = new ArrayList<>();
            filterItems.add(new FilterItem(createBitmap(imagePath), null, "Normal"));
            filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getStarLitFilter(), "Starlit"));
            filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getBlueMessFilter(), "Bluemess"));
            filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getAweStruckVibeFilter(), "Awestruckvibe"));
            filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getLimeStutterFilter(), "Lime"));
            filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getNightWhisperFilter(), "Night Wisper"));
            applyFilters(filterItems);
            filterItemsData.postValue(filterItems);
        });
    }

    private Bitmap applyFilter(Bitmap bitmap, Filter filter) {
        return filter.processFilter(bitmap);
    }

    private <T extends Distortable> void applyFilters(List<T> items) {
        for (Distortable d : items) {
            Bitmap img = d.getImage();
            Filter filter = d.getFilter();
            if (filter == null) continue;
            d.setImage(filter.processFilter(img));
        }
    }

    private Bitmap createBitmap(String imagePath) {
        if (imagePath == null) return null;
        Context activity = getApplication();
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

    private void createBitmapAndApplyFilter(String imagePath, Filter filter) {
        dispatchThread.postRunnable((Runnable) () -> {
            if (imagePath == null) return;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inDither = true;
            options.inSampleSize = 3;
            try {
                Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
                if (bmp == null) return;
                int mult = Math.abs(bmp.getWidth() / 512);
                if (mult == 0) mult = 1;
                bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / mult, bmp.getHeight() / mult, false);
                if (filter != null)
                    applyFilter(bmp, filter);
                File file = new File(getApplication().getExternalFilesDir(null), "photo_distorted.jpg");

                bmp.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
                filteredImage.postValue(file.getAbsolutePath());
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

    }
}
