package com.prepod.photodistort.views;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.prepod.photodistort.Const;
import com.prepod.photodistort.R;
import com.prepod.photodistort.models.FilterItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FiltersFragment extends Fragment implements View.OnClickListener, FiltersAdapter.OnFilterClickListener {

    private ImageView resultImage;
    private String imagePath;
    private RequestManager mGlide;
    private RecyclerView filtersLsit;
    private List<FilterItem> filterItemList = new ArrayList<>();
    private FiltersViewModel filtersViewModel;
    private FiltersAdapter filtersAdapter;

    public FiltersFragment() {
        // Required empty public constructor
    }

    public static FiltersFragment newInstance(String path) {
        FiltersFragment filtersFragment = new FiltersFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Const.KEY_EXTRA_PATH, path);
        filtersFragment.setArguments(bundle);
        return filtersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imagePath = getArguments().getString(Const.KEY_EXTRA_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultImage = view.findViewById(R.id.image_item);
        filtersLsit = view.findViewById(R.id.recycler_filters);
        filtersViewModel = ViewModelProviders.of(this).get(FiltersViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filtersLsit.setLayoutManager(layoutManager);
        filtersLsit.setHasFixedSize(true);
        filtersAdapter = new FiltersAdapter(getActivity(), filterItemList, this);
        filtersLsit.setAdapter(filtersAdapter);

        filtersViewModel.createFiltersPreviewList(imagePath).observe(this, filterItems -> {
            if (filterItems != null) {
                filterItemList.addAll(filterItems);
                filtersAdapter.notifyDataSetChanged();
            }
        });

        mGlide = Glide.with(getActivity());
        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
        File file = new File(imagePath);
        mGlide.load(file).apply(new RequestOptions().centerCrop()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .override(512, 512)).into(resultImage);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFilterClick(int pos) {
        LiveData<String> fullImageData = filtersViewModel
                .getFilteredImage(imagePath, filterItemList.get(pos).getFilter());
        fullImageData.observe(this, this::loadImage);
    }
}
