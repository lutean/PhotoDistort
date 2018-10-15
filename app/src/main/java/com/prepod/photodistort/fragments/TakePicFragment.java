package com.prepod.photodistort.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.prepod.photodistort.AutoFitTextureView;
import com.prepod.photodistort.Const;
import com.prepod.photodistort.OnCapturePictureListener;
import com.prepod.photodistort.PhotoDistort;
import com.prepod.photodistort.R;
import com.prepod.photodistort.helpers.CameraController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anton on 13.10.2018.
 */
public class TakePicFragment extends BaseFragment implements View.OnClickListener {
    public static TakePicFragment newInstance() {
        return new TakePicFragment();
    }


    private AutoFitTextureView mTextureView;
    private CameraController cameraController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.btn_camera_takepic).setOnClickListener(this);
        view.findViewById(R.id.btn_camera_switchcam).setOnClickListener(this);
        mTextureView = view.findViewById(R.id.texture_camera);
        cameraController.setTextureView(mTextureView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cameraController = new CameraController(getActivity());
        cameraController.setOnCapturePictureListener((OnCapturePictureListener) context);
        getLifecycle().addObserver(cameraController);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cameraController.removeOnCapturePictureListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_takepic:
                cameraController.takePicture();
                break;
            case R.id.btn_camera_switchcam:
                cameraController.switchCam();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Const.PERMISSIONS_CAMERA_REQUEST) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                CameraFragment.ErrorDialog.newInstance(getString(R.string.request_permission))
//                        .show(getChildFragmentManager(), "dialog");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}