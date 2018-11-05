package com.jenny.medicationreminder;

import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.CheckBox;

public class EditMedActivity extends AppCompatActivity {

    CardView cvAppbar;
    CheckBox cbMorning, cbLunch, cbEveing, cbBeforeBed;

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
