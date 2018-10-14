package com.prepod.photodistort.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Filter;
import android.widget.TextView;

import com.prepod.photodistort.Const;
import com.prepod.photodistort.OnCapturePictureListener;
import com.prepod.photodistort.R;
import com.prepod.photodistort.helpers.MainTabsAdapter;

public class MainActivity extends AppCompatActivity implements OnCapturePictureListener {

    private ViewPager mainPager;
    private MainTabsAdapter mainTabsAdapter;
    private TextView toolbarTitle;
    private String[] tabTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPager = findViewById(R.id.pager_main);
        toolbarTitle = findViewById(R.id.text_toolbar);

        tabTitle = getResources().getStringArray(R.array.main_tab_titles);
        mainTabsAdapter = new MainTabsAdapter(getSupportFragmentManager(), tabTitle);

        mainPager.setAdapter(mainTabsAdapter);

        TabLayout tabLayout = findViewById(R.id.tablayout_main);
        tabLayout.setupWithViewPager(mainPager);

        setToolbarTitle(tabTitle[0]);

        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setToolbarTitle(tabTitle[i]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void onCapture(String path) {
        Intent intent = new Intent(this, FiltersActivity.class);
        intent.putExtra(Const.KEY_EXTRA_PATH, path);
        startActivity(intent);
    }

    private void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
}
