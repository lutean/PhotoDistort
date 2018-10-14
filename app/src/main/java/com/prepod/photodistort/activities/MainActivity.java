package com.prepod.photodistort.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prepod.photodistort.R;
import com.prepod.photodistort.fragments.TakePicFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TakePicFragment.newInstance())
                    .commit();
        }

    }
}
