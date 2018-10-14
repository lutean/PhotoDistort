package com.prepod.photodistort.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.prepod.photodistort.Distortable;
import com.prepod.photodistort.models.FilterItem;
import com.prepod.photodistort.helpers.FiltersAdapter;
import com.prepod.photodistort.R;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class FiltersFragment extends Fragment implements View.OnClickListener, FiltersAdapter.OnFilterClickListener {

    private static final String KEY_PATH = "path";

    private ImageView resultImage;
    private String imagePath;
    private RequestManager mGlide;
    private RecyclerView filtersLsit;
    private List<FilterItem> filterItems;
    private Bitmap originalImage;
    private Bitmap filteredImage;

    public FiltersFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("1", "handleMessage: ");
            switch (msg.what) {
                case 1:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    resultImage.setImageBitmap(bitmap);
                    break;
            }
        }
    };

    public static FiltersFragment newInstance(String path) {
        FiltersFragment filtersFragment = new FiltersFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PATH, path);
        filtersFragment.setArguments(bundle);
        return filtersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imagePath = getArguments().getString(KEY_PATH);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filtersLsit.setLayoutManager(layoutManager);
        filtersLsit.setHasFixedSize(true);
        if (filterItems == null)
            filterItems = createFiltersPreviewList(imagePath);
        FiltersAdapter filtersAdapter = new FiltersAdapter(getActivity(), filterItems, this);
        filtersLsit.setAdapter(filtersAdapter);

        mGlide = Glide.with(getActivity());
        loadImage(imagePath);
    }

    private void loadImage(String imagePath){
        File file = new File(imagePath);
        mGlide.load(file).apply(new RequestOptions().centerCrop()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .override(512, 512)).into(resultImage);
    }

    private List<FilterItem> createFiltersPreviewList(String imagePath) {
        List<FilterItem> filterItems = new ArrayList<>();
        //Bitmap result = createBitmap(imagePath);
        filterItems.add(new FilterItem(createBitmap(imagePath), null, "Normal"));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getStarLitFilter(), "Starlit"));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getBlueMessFilter(), "Bluemess"));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getAweStruckVibeFilter(), "Awestruckvibe"));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getLimeStutterFilter(), "Lime"));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getNightWhisperFilter(), "Night Wisper"));
        applyFilters(filterItems);
        return filterItems;
    }

    private Bitmap applyFilter(Bitmap bitmap, Filter filter){
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

    private String createBitmapAndApplyFilter(String imagePath, Filter filter){
        if (imagePath == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        options.inSampleSize = 3;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
            if (bmp == null) return null;
            bmp = Bitmap.createScaledBitmap(bmp, 512, 512, false);
            if (filter != null)
                applyFilter(bmp, filter);
            File file = new File(getActivity().getExternalFilesDir(null), "photo_distorted.jpg");

            bmp.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
            return file.getAbsolutePath();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap createBitmap(String imagePath) {
        if (imagePath == null) return null;
        Activity activity = getActivity();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        options.inSampleSize = 3;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
            if (bmp == null) return null;
            float size = activity.getResources().getDimension(R.dimen.filters_preview_img_size);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(imagePath));
            return Bitmap.createScaledBitmap(bmp, (int) size, (int) size, false);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onClick(View view) {
     /*   Bitmap res = null;
        switch (view.getId()){
            case R.id.btn_filter_gray:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap res = createBitmap(imagePath);
                        int[] pix = new int[res.getWidth() * res.getHeight()];
                        res.getPixels(pix, 0, res.getWidth(), 0, 0, res.getWidth(),
                                res.getHeight());
                        ndkEmboss(pix, res.getWidth(), res.getHeight());
                        Bitmap out = Bitmap.createBitmap(res.getWidth(), res.getHeight(),
                                Bitmap.Config.ARGB_8888);
                        out.setPixels(pix, 0, res.getWidth(), 0, 0, res.getWidth(),
                                res.getHeight());
                        Message m = myHandler.obtainMessage(1, out);
                        myHandler.sendMessage(m);

                        pix = null;
                        res.recycle();

                    }
                }).start();
                break;
        }*/
    }

    @Override
    public void onFilterClick(int pos) {
        String path = createBitmapAndApplyFilter(imagePath, filterItems.get(pos).getFilter());
        loadImage(path);
    }
}
