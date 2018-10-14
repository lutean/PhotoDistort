package com.prepod.photodistort.fragments;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.prepod.photodistort.ConvolutionMatrix;
import com.prepod.photodistort.Distortable;
import com.prepod.photodistort.models.FilterItem;
import com.prepod.photodistort.helpers.FiltersAdapter;
import com.prepod.photodistort.R;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FiltersFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_PATH = "path";

    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();
    public static native void ndkEmboss(int[] data, int width,
                                        int height);

    private ImageView resultImage;
    private String imagePath;
    private RequestManager mGlide;
    private RecyclerView filtersLsit;
    private List<FilterItem> filterItems;

    public FiltersFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i("1", "handleMessage: ");
            switch (msg.what){
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
        resultImage = view.findViewById(R.id.image_filters);
        filtersLsit = view.findViewById(R.id.recycler_filters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filtersLsit.setLayoutManager(layoutManager);
        filtersLsit.setHasFixedSize(true);
        if (filterItems == null)
            filterItems = createFiltersPreviewList(imagePath);
        FiltersAdapter filtersAdapter = new FiltersAdapter(getActivity(), filterItems);
        filtersLsit.setAdapter(filtersAdapter);

        mGlide = Glide.with(getActivity());
        File file = new File(imagePath);
        mGlide.load(file).apply(new RequestOptions().centerCrop()
//                .signature(new ObjectKey(System.currentTimeMillis()))
                .override(512, 512)).into(resultImage);
    }

    private List<FilterItem> createFiltersPreviewList(String imagePath){
        List<FilterItem> filterItems = new ArrayList<>();
        //Bitmap result = createBitmap(imagePath);
        filterItems.add(new FilterItem(createBitmap(imagePath), null));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getStarLitFilter()));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getBlueMessFilter()));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getAweStruckVibeFilter()));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getLimeStutterFilter()));
        filterItems.add(new FilterItem(createBitmap(imagePath), SampleFilters.getNightWhisperFilter()));
        applyFilters(filterItems);
        return filterItems;
    }

    private <T extends Distortable> void applyFilters(List<T> items){
        for (Distortable d : items) {
            Bitmap img = d.getImage();
            Filter filter = d.getFilter();
            if (filter == null) continue;
            d.setImage(filter.processFilter(img));
        }

    }

    private Bitmap createBitmap(String imagePath) {
        if (imagePath == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        options.inSampleSize = 3;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
            if (bmp == null) return null;
            float size = getActivity().getResources().getDimension(R.dimen.filters_preview_img_size);

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

    public Bitmap doInvert(Bitmap originalImage) {
        Bitmap resultingBitmap =
                Bitmap.createBitmap(originalImage.getWidth(), originalImage.getHeight(),
                        originalImage.getConfig());
        int A, R, G, B;
        int pixelColor;
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelColor = originalImage.getPixel(x, y);
                A = Color.alpha(pixelColor);
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                resultingBitmap.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return resultingBitmap;
    }

    public Bitmap doGreyScale(Bitmap originalImage) {
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;
        Bitmap resultingBitmap =
                Bitmap.createBitmap(originalImage.getWidth(), originalImage.getHeight(),
                        originalImage.getConfig());
        int A, R, G, B;
        int pixel;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixel = originalImage.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                resultingBitmap.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return resultingBitmap;
    }

    public Bitmap applyGaussianBlur(Bitmap originalImage) {
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 }
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        return ConvolutionMatrix.computeConvolution3x3(originalImage, convMatrix);
    }


}
