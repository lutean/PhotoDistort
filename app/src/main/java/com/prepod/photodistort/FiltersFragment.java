package com.prepod.photodistort;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class FiltersFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_PATH = "path";

    private ImageView resultImage;
    private String imagePath;
    private RequestManager mGlide;

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
        view.findViewById(R.id.btn_filter_invert).setOnClickListener(this);
        view.findViewById(R.id.btn_filter_gray).setOnClickListener(this);
        view.findViewById(R.id.btn_filter_blur).setOnClickListener(this);
        resultImage = view.findViewById(R.id.image_filters);
        mGlide = Glide.with(getActivity());
        File file = new File(imagePath);
        mGlide.load(file).apply(new RequestOptions().centerCrop()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .override(512, 512)).into(resultImage);
    }

    private Bitmap compressFile(String imagePath) {
        if (imagePath == null) return null;
        File file = new File(imagePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = true;
        options.inSampleSize = 3;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
            if (bmp == null) return null;
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(imagePath));
            return bmp;

        } catch (FileNotFoundException | OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onClick(View view) {
        Bitmap res = null;
        switch (view.getId()){
            case R.id.btn_filter_invert:
                res = doInvert(compressFile(imagePath));
                break;
            case R.id.btn_filter_gray:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap res = doGreyScale(compressFile(imagePath));
                        Message m = myHandler.obtainMessage(1, res);
                        myHandler.sendMessage(m);
                    }
                }).start();
                break;
            case R.id.btn_filter_blur:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap res = applyGaussianBlur(compressFile(imagePath));
                        Message m = myHandler.obtainMessage(1, res);
                        myHandler.sendMessage(m);
                    }
                }).start();
                break;
        }
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
