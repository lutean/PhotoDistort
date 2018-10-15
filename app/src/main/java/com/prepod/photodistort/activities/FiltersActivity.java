package com.prepod.photodistort.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.prepod.photodistort.Const;
import com.prepod.photodistort.R;
import com.prepod.photodistort.fragments.FiltersFragment;

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ImageView toolbarBtn = findViewById(R.id.image_toolbar);
        toolbarBtn.setVisibility(View.VISIBLE);
        toolbarBtn.setOnClickListener(view -> finish());

        if (getIntent().getExtras() != null) {
            String imagePath = getIntent().getStringExtra(Const.KEY_EXTRA_PATH);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FiltersFragment.newInstance(imagePath))
                    .commit();
        }

    }
}
