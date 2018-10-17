package com.prepod.photodistort.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.prepod.photodistort.Const;
import com.prepod.photodistort.R;
import com.prepod.photodistort.views.FiltersFragment;

import java.io.File;

public class FiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ImageView toolbarBtn = findViewById(R.id.image_toolbar);
        toolbarBtn.setVisibility(View.VISIBLE);
        toolbarBtn.setOnClickListener(view -> finish());

        ImageView shareBtn = findViewById(R.id.image_toolbar_share);
        shareBtn.setVisibility(View.VISIBLE);
        shareBtn.setOnClickListener(view -> {
            File file = new File(getExternalFilesDir(null), "photo_distorted.jpg");
            if (file.exists()) {
                Intent share = new Intent(Intent.ACTION_VIEW);
                share.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri fileURI = FileProvider.getUriForFile(
                        this,
                        getApplicationContext()
                                .getPackageName() + ".provider", file);
                share.setDataAndType(fileURI, "image/jpeg");
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(share);
            }
        });

        if (getIntent().getExtras() != null) {
            String imagePath = getIntent().getStringExtra(Const.KEY_EXTRA_PATH);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FiltersFragment.newInstance(imagePath))
                    .commit();
        }
    }
}
