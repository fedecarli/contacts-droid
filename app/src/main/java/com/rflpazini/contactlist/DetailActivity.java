package com.rflpazini.contactlist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rflpazini.contactlist.model.User;
import com.rflpazini.contactlist.utils.Constants;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final User user = (User) getIntent().getSerializableExtra(Constants.SER_KEY);


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(user.getName());

        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Resources resources = getResources();
        int permissionCheck = ContextCompat.checkSelfPermission(DetailActivity.this,
                Manifest.permission.CALL_PHONE);
        if (permissionCheck != 0) {
            requestPermission();
        }

        final TextView phoneDetail = (TextView) findViewById(R.id.phone_detail);
        phoneDetail.setText(user.getPhone());
        phoneDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+"+user.getPhone()));
                startActivity(callIntent);
            }
        });

        TextView emailDetail = (TextView) findViewById(R.id.email_detail);
        emailDetail.setText(user.getEmail());

        /**
         * Put image to the AppBar
         */
//        TypedArray placePictures = resources.obtainTypedArray(R.array.places_picture);
//        ImageView placePicutre = (ImageView) findViewById(R.id.image);
//        placePicutre.setImageDrawable(placePictures.getDrawable(postion % placePictures.length()));
//        placePictures.recycle();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(DetailActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DetailActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    Constants.MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }
}
