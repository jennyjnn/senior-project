package com.jenny.medicationreminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

public class EditMedActivity extends AppCompatActivity {

    CardView cvAppbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);

        initInstances();
    }

    private void initInstances() {
        cvAppbar = findViewById(R.id.cvAppBarEditMed);
        cvAppbar.setBackgroundResource(R.drawable.bg_appbar);

    }
}
