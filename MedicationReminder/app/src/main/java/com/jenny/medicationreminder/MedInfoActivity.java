package com.jenny.medicationreminder;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.VideoView;

public class MedInfoActivity extends AppCompatActivity {

    CardView cvAppBarMedInfo;
//    VideoView vdMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);

        initInsatances();
    }

    private void initInsatances() {
        // set color for app bar
        cvAppBarMedInfo = findViewById(R.id.cvAppBarMedInfo);
        cvAppBarMedInfo.setBackgroundResource(R.drawable.bg_appbar);

        //  set url to video
//        vdMed = findViewById(R.id.vdMed);

        String path = "https://www.youtube.com/watch?v=WgfS1sFO40k";
        Uri uri = Uri.parse(path);

//        vdMed.setVideoURI(uri);
//        vdMed.start();

    }

    public void backBtn(View view) {
        finish();
    }
}
