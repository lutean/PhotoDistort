package com.prepod.photodistort.views;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepod.photodistort.OnCapturePictureListener;
import com.prepod.photodistort.R;
import com.prepod.photodistort.models.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends BaseFragment implements GalleryAdapter.GalleryInteractionListener {

    private final int PERMISSIONS_STORAGE_REQUEST = 13;
    private RecyclerView mPhotoGridRecycler;
    private GridLayoutManager gridLayoutManager;
    private GalleryAdapter galleryAdapter;
    private List<ImageItem> imageItemList = new ArrayList<>();
    private OnCapturePictureListener onCapturePictureListener;
    private GalleryViewModel galleryViewModel;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        mPhotoGridRecycler = view.findViewById(R.id.recycler_gallery_grid);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
            return;
        }
        init();
    }

    private void init() {
        LiveData<List<ImageItem>> imagesData = galleryViewModel.getImagesData();
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        mPhotoGridRecycler.setHasFixedSize(true);
        mPhotoGridRecycler.setLayoutManager(gridLayoutManager);

        galleryAdapter = new GalleryAdapter(getActivity(), imageItemList, this);
        mPhotoGridRecycler.setAdapter(galleryAdapter);
        imagesData.observe(this, imageItems -> {
            if (imageItems != null) {
                imageItemList.addAll(imageItems);
                galleryAdapter.notifyDataSetChanged();
            }
        });
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_STORAGE_REQUEST);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onCapturePictureListener = (OnCapturePictureListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCapturePictureListener = null;
    }

    @Override
    public void onImageClick(final ImageItem imageItem) {
        LiveData<String> fullImageData = galleryViewModel.getFullImage(imageItem.getImageId());
        fullImageData.observe(this, s -> onCapturePictureListener.onCapture(s));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_STORAGE_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                init();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
