package com.prepod.photodistort.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prepod.photodistort.OnCapturePictureListener;
import com.prepod.photodistort.R;
import com.prepod.photodistort.helpers.MainTabsAdapter;

public class MainActivity extends AppCompatActivity implements OnCapturePictureListener {

    private ViewPager mainPager;
    private MainTabsAdapter mainTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPager = findViewById(R.id.pager_main);

        mainTabsAdapter = new MainTabsAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.main_tab_titles));

        mainPager.setAdapter(mainTabsAdapter);

        TabLayout tabLayout = findViewById(R.id.tablayout_main);
        tabLayout.setupWithViewPager(mainPager);

    }

    @Override
    public void onCapture() {
        startActivity(new Intent(this, FiltersActivity.class));
    }
}
