package com.prepod.photodistort.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prepod.photodistort.helpers.GalleryAdapter;
import com.prepod.photodistort.models.ImageItem;
import com.prepod.photodistort.helpers.ImageLoadHelper;
import com.prepod.photodistort.R;

import java.util.List;

public class GalleryFragment extends BaseFragment implements GalleryAdapter.GalleryInteractionListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int PERMISSIONS_STORAGE_REQUEST = 13;


    private String mParam1;
    private String mParam2;

    private RecyclerView mPhotoGridRecycler;
    private GridLayoutManager gridLayoutManager;
    private GalleryAdapter galleryAdapter;
    private List<ImageItem> imageItems;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mPhotoGridRecycler = view.findViewById(R.id.recycler_gallery_grid);
        mPhotoGridRecycler.setHasFixedSize(true);
        mPhotoGridRecycler.setLayoutManager(gridLayoutManager);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
            return;
        }
        imageItems = ImageLoadHelper.getImages(getActivity().getContentResolver());
        galleryAdapter = new GalleryAdapter(getActivity(), imageItems, this);
        mPhotoGridRecycler.setAdapter(galleryAdapter);

    }

    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            new CameraFragment.ConfirmationDialog().show(getChildFragmentManager(), "dialog");
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_STORAGE_REQUEST);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onImageClick(ImageItem imageItem) {
       /* getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, FiltersFragment.newInstance(imageItem.getImageThumbPath()))
                .commit();*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_STORAGE_REQUEST) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}
