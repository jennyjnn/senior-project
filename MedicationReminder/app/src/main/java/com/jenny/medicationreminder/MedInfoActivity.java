package com.jenny.medicationreminder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jenny.medicationreminder.Model.Medicine;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class MedInfoActivity extends AppCompatActivity {

    CardView cvAppBarMedInfo, cvContent, cvOthers, cvSideEff;
    CardView cvWarning, cvPreserve;
    TextView expandable_text, tvMedName, tvOthers, tvNoMedInfo;
    View vOthers, vSideEff, vWarning, vPreserve;
//    YouTubePlayerView youTubeVdo;
    WebView wvVideo;

//    private final String API_KEY = "AIzaSyA9phrfpPpbiywCUWBhZ_ekX7n6Uc9pggk";
    String videoCode = "";
    String medName = new String();
    String intro = new String();
    String sideEffect = new String();
    String warning = new String();
    String preserve = new String();

    Boolean checkMedInfo = false;

    ProgressDialog progressDialog;

    FirebaseDatabase database;
    DatabaseReference infoRef;

    ExpandableTextView expandIntro, expandSideEff, expandWarning, expandPreserve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);

        Intent intent = getIntent();
        medName = intent.getStringExtra("medName");
        medName = medName.toLowerCase();

        initInstances();
    }

    private void initInstances() {
        tvNoMedInfo = findViewById(R.id.tvNoMedInfo);
        vSideEff = findViewById(R.id.vSideEff);
        cvSideEff = findViewById(R.id.cvSideEff);
        vWarning = findViewById(R.id.vWarning);
        cvWarning = findViewById(R.id.cvWarning);
        vPreserve = findViewById(R.id.vPreserve);
        cvPreserve = findViewById(R.id.cvPreserve);
        vOthers = findViewById(R.id.vOthers);
        cvContent = findViewById(R.id.cvContent);
        cvOthers = findViewById(R.id.cvOthers);
        tvOthers = findViewById(R.id.tvOthers);
        tvMedName = findViewById(R.id.tvMedName);
        expandable_text = findViewById(R.id.expandable_text);
        expandIntro = findViewById(R.id.expandIntro);
        expandWarning = findViewById(R.id.expandWarning);
        expandSideEff = findViewById(R.id.expandSideEff);
        expandPreserve = findViewById(R.id.expandPreserve);
//        youTubeVdo = findViewById(R.id.youTubeVdo);
        wvVideo = findViewById(R.id.wvVideo);

        // Set Font
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/th_sarabun.ttf");
//        tvMedName.setTypeface(typeface, Typeface.BOLD);
//        tvNoMedInfo.setTypeface(typeface, Typeface.BOLD);
//        tvOthers.setTypeface(typeface, Typeface.BOLD);
//        expandable_text.setTypeface(typeface, Typeface.BOLD);

        // set color for app bar
        cvAppBarMedInfo = findViewById(R.id.cvAppBarMedInfo);
        cvAppBarMedInfo.setBackgroundResource(R.drawable.bg_appbar);

        // Progress Dialog
        progressDialog = new ProgressDialog(MedInfoActivity.this);
        progressDialog.setMessage("กรุณารอสักครู่");
        progressDialog.show();

        // Read information of medication from firebase database
        database = FirebaseDatabase.getInstance();
        infoRef = database.getReference("Medicine");
        infoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Medicine medicine = snapshot.getValue(Medicine.class);
                        if (medicine.getMed_name().toLowerCase().contains(medName)) {
                            // Name
                            tvMedName.setText(medicine.getMed_name());
                            // Intro
                            intro = medicine.getMed_intro();
                            expandIntro.setText(intro);
                            // Side Effect
                            sideEffect = medicine.getMed_sideEffect();
                            if (sideEffect != null) {
                                vSideEff.setVisibility(View.VISIBLE);
                                cvSideEff.setVisibility(View.VISIBLE);
                                expandSideEff.setText(sideEffect);
                            }
                            // Warning
                            warning = medicine.getMed_warning();
                            if (warning != null) {
                                vWarning.setVisibility(View.VISIBLE);
                                cvWarning.setVisibility(View.VISIBLE);
                                expandWarning.setText(warning);
                            }
                            // Preserve
                            preserve = medicine.getMed_preserve();
                            if (preserve != null) {
                                vPreserve.setVisibility(View.VISIBLE);
                                cvPreserve.setVisibility(View.VISIBLE);
                                expandPreserve.setText(preserve);
                            }
                            // Others
                            videoCode = medicine.getMed_video();
                            if (videoCode != null) {
                                vOthers.setVisibility(View.VISIBLE);
                                cvOthers.setVisibility(View.VISIBLE);
                                tvOthers.setVisibility(View.VISIBLE);
                                wvVideo.setVisibility(View.VISIBLE);
                                // Show video

                                wvVideo.getSettings().setJavaScriptEnabled(true);
                                wvVideo.loadData("<iframe width=\"100%\" src=\"https://www.youtube.com/embed/WgfS1sFO40k\" frameborder=\"0\" allowfullscreen></iframe>",
                                        "text/html" , "utf-8");
                                wvVideo.setWebChromeClient(new WebChromeClient());

//                                youTubeVdo.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
//                                    @Override
//                                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                                        if (!b) {
//                                            youTubePlayer.cueVideo(videoCode);
//                                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                                        Toast.makeText(MedInfoActivity.this, youTubeInitializationResult.toString(),
//                                                Toast.LENGTH_LONG).show();
//                                    }
//                                });
                            }
                            Log.e("Med", medicine.getMed_name());
                            checkMedInfo = true;
                            cvContent.setVisibility(View.VISIBLE);
                        }
                    }
                    if (checkMedInfo == false) {
                        tvNoMedInfo.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    public void backBtn(View view) {
        finish();
    }

}
