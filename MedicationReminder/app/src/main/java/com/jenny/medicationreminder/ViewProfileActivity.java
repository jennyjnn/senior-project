package com.jenny.medicationreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class ViewProfileActivity extends AppCompatActivity {
    CardView cvAppBarViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        initInstances();
    }

    private void initInstances() {
        // set color for app bar
        cvAppBarViewProfile = findViewById(R.id.cvAppBarViewProfile);
        cvAppBarViewProfile.setBackgroundResource(R.drawable.bg_appbar);
    }

    public void backBtn(View view) {
        finish();
    }

    public void editProfile(View view) {
        Intent intent = new Intent(ViewProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
