package com.prepod.photodistort.helpers;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.prepod.photodistort.models.ImageItem;

import java.util.List;

public class GalleryViewModel extends AndroidViewModel {

    MutableLiveData<List<ImageItem>> imagesData;
    SingleLiveEvent<String> fullImageData;
    private DispatchThread dispatchThread = new DispatchThread("Gallery");
    private ContentResolver contentResolver;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        contentResolver = getApplication().getContentResolver();
        fullImageData = new SingleLiveEvent<>();
    }

    public LiveData<List<ImageItem>> getImagesData() {
        if (imagesData == null) {
            imagesData = new MutableLiveData<>();
            loadData();
        }
        return imagesData;
    }

    public LiveData<String> getFullImage(long imageId) {
        loadFullImageData(imageId);
        return fullImageData;
    }

    private void loadData() {
        dispatchThread.postRunnable(new Runnable() {
            @Override
            public void run() {
                List<ImageItem> imageItems = ImageLoadHelper.getImages(contentResolver);
                imagesData.postValue(imageItems);
            }
        });
    }

    private void loadFullImageData(final long imageId) {
        dispatchThread.postRunnable(new Runnable() {
            @Override
            public void run() {
                String fullImagePath = ImageLoadHelper.getFullImage(contentResolver, imageId);
                fullImageData.postValue(fullImagePath);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (dispatchThread != null)
            dispatchThread.interrupt();
    }
}
