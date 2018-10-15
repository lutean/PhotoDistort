package com.prepod.photodistort.views;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepod.photodistort.AutoFitTextureView;
import com.prepod.photodistort.Const;
import com.prepod.photodistort.OnCapturePictureListener;
import com.prepod.photodistort.R;
import com.prepod.photodistort.helpers.CameraController;

/**
 * Created by Anton on 13.10.2018.
 */
public class TakePicFragment extends BaseFragment implements View.OnClickListener {
    private AutoFitTextureView mTextureView;
    private CameraController cameraController;

    public static TakePicFragment newInstance() {
        return new TakePicFragment();
    }

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