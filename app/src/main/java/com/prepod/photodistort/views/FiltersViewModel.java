package com.prepod.photodistort.views;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.prepod.photodistort.helpers.BitmapHelper;
import com.prepod.photodistort.helpers.DispatchThread;
import com.prepod.photodistort.helpers.SingleLiveEvent;
import com.prepod.photodistort.models.FilterItem;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

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
            filterItems.add(createFilterItem(imagePath, null, "Normal"));
            filterItems.add(createFilterItem(imagePath, SampleFilters.getStarLitFilter(), "Starlit"));
            filterItems.add(createFilterItem(imagePath, SampleFilters.getBlueMessFilter(), "Bluemess"));
            filterItems.add(createFilterItem(imagePath, SampleFilters.getAweStruckVibeFilter(), "Awestruckvibe"));
            filterItems.add(createFilterItem(imagePath, SampleFilters.getLimeStutterFilter(), "Lime"));
            filterItems.add(createFilterItem(imagePath, SampleFilters.getNightWhisperFilter(), "Night Wisper"));
            BitmapHelper.applyFilters(filterItems);
            filterItemsData.postValue(filterItems);
        });
    }

    private FilterItem createFilterItem(String path, Filter filter, String filterName) {
        return new FilterItem(BitmapHelper.createBitmap(getApplication(), path), filter, filterName);
    }

    private Bitmap applyFilter(Bitmap bitmap, Filter filter) {
        return filter.processFilter(bitmap);
    }

    private void createBitmapAndApplyFilter(String imagePath, Filter filter) {
        dispatchThread.postRunnable((Runnable) () -> {
            String result = BitmapHelper.createBitmapAndApplyFilter(getApplication(), imagePath, filter);
            filteredImage.postValue(result);
        });

    }
}
