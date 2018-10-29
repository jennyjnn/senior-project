package com.jenny.medicationreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

public class MenuActivity extends AppCompatActivity {

    CardView cvAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initInstances();

    }

    private void initInstances() {
        cvAppBar = findViewById(R.id.cvAppBar);
        cvAppBar.setBackgroundResource(R.drawable.bg_appbar);
    }
}
