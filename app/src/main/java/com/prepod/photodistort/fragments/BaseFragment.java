package com.prepod.photodistort.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prepod.photodistort.R;

public class BaseFragment extends Fragment {


    protected void setupToolbar(Activity view, String title, boolean showCloseBtn){
        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        ImageView toolbarIcon = view.findViewById(R.id.image_toolbar);
        TextView toolbarText = view.findViewById(R.id.text_toolbar);
        if (toolbarText == null) return;
        toolbarText.setText(title);
        if (toolbarIcon == null) return;
        int visibility = showCloseBtn ? View.VISIBLE : View.GONE;
        toolbarIcon.setVisibility(visibility);
    }
}
