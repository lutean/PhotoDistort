package com.prepod.photodistort.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prepod.photodistort.R;
import com.prepod.photodistort.fragments.FiltersFragment;

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, FiltersFragment.newInstance(getExternalFilesDir(null) + "/photo.jpg"))
                .commit();

    }


}
